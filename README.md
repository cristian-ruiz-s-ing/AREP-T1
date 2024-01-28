# Taller 1
#### Cristian Camilo Ruiz Santa

Este proyecto implementa un servidor HTTP simple que obtiene información sobre películas utilizando la API de OMDb. Incluye un cliente HTML básico que permite a los usuarios buscar películas y muestra detalles sobre el primer resultado.

## Cómo Ejecutar

1. **Configuración del Servidor:**

    - Compila y ejecuta la clase `HttpServer`.
    - El servidor se iniciará en el puerto 35000.

2. **Acceso al Cliente:**

    - Abre un navegador web y ve a `http://localhost:35000`.
    - El formulario HTML permite a los usuarios ingresar el nombre de una película y enviar la consulta.
    - El servidor responderá con detalles sobre la primera película encontrada, incluyendo una imagen, título y año.

## Detalles de la Implementación

### Servidor (`HttpServer` class)

- El servidor escucha en el puerto 35000 para conexiones entrantes.
- Captura flujos de entrada y salida para leer y enviar datos a los clientes.
- Cuando un cliente se conecta, lee la solicitud HTTP, extrae la URI solicitada y decide si buscar información sobre una película o mostrar el formulario predeterminado.
- El método `getMovie` realiza una consulta a la API de OMDb basada en el nombre de la película proporcionado en la URI.
- El método `parseResponse` procesa la respuesta de la API, extrae detalles de la película y construye una respuesta HTML.

### Cliente (Formulario HTML)

- El formulario HTML permite a los usuarios ingresar el nombre de una película y enviar la consulta.
- La función JavaScript `loadMovie` se activa al hacer clic en el botón, enviando una solicitud HTTP GET al servidor.
- El servidor responde con detalles de la película y el div `getmovie` se actualiza con la respuesta.

