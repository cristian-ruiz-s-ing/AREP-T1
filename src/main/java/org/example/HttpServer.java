package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpServer {
    public static void main(String[] args) throws IOException, URISyntaxException {

        System.out.println("Starting Server");
        ServerSocket servidor = new ServerSocket(35000);

        Socket cliente = null;
        boolean running = true;

        while (running) {
            cliente = servidor.accept();

            /*Captura datos de entrada y salida para leer y mandar los datos*/
            PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            String inputLine = "";
            Boolean firstLine = true;
            String reqURI = "";

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Llega: " + inputLine);
                if (firstLine) {
                    firstLine = false;
                    reqURI = inputLine.split(" ")[1];
                }
                if (!in.ready()) break;
            }

            URI uri = new URI(reqURI);
            if (uri.getPath().startsWith("/movie")) {
                out.println(getMovie(uri.getQuery().split("=")[1]));
            }else {
                out.println(getDefaultForm());
            }

            out.close();
            in.close();
            cliente.close();

        }
        servidor.close();

    }

    private static String getMovie(String s) throws IOException {
        String API = "http://www.omdbapi.com/?s="+s+"&plot=short&apikey=2f828999";
        URL url = new URL(API);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return parseResponse(response.toString());
    }

    private static String parseResponse(String response) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        JsonNode resp = obj.readTree(response);

        if (resp.has("Search") && resp.get("Search").isArray()&& resp.get("Search").size() > 0){
            JsonNode movie = resp.get("Search").get(0); //Primer resultado

            String poster = movie.get("Poster").asText();
            String title = movie.get("Title").asText();
            String year = movie.get("Year").asText();

            return "HTTP/1.1 200 OK \r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    "<img src="+poster+"><br>\n" +
                    "<label>Título:</label><p>"+title+"</p>\n" +
                    "<label>Año:</label><p>"+year+"</p>";
        }else{
            return "HTTP/1.1 200 OK \r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    "No se encontraron resultados";
        }
    }

    private static String getDefaultForm() {
        return "HTTP/1.1 200 OK \r\n"+
                "\r\n"+
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <title>Form Example</title>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Form</h1>\n" +
                "<form action=\"/mov\">\n" +
                "  <label>Película:</label><br>\n" +
                "  <input type=\"text\" id=\"movie\" name=\"movie\" value=\"\" placeholder=\"Nombre de película...\"><br><br>\n" +
                "  <input type=\"button\" value=\"Submit\" onclick=\"loadMovie()\"><br>\n" +
                "</form>\n" +
                "<div id=\"getmovie\"></div>\n" +
                "<!-- fetch(`http://www.omdbapi.com/?s=${movie}&page=1&apikey=2f828999`, {\n" +
                "        method: 'GET',\n" +
                "        mode: 'no-cors'\n" +
                "        }) -->\n" +
                "\n" +
                "<script>\n" +
                "  function loadMovie(){\n" +
                "    let nameVar = document.getElementById(\"movie\").value;\n" +
                "      const xhttp = new XMLHttpRequest();\n" +
                "      xhttp.onload = function() {\n" +
                "          document.getElementById(\"getmovie\").innerHTML =\n" +
                "          this.responseText;\n" +
                "      }\n" +
                "      xhttp.open(\"GET\", \"/movie?name=\"+nameVar);\n" +
                "      xhttp.send();\n" +
                "  }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
    }
}