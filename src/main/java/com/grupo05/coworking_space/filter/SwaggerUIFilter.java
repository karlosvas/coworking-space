package com.grupo05.coworking_space.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Filtro personalizado para modificar la interfaz de Swagger UI.
 * Este filtro intercepta las respuestas HTTP de la página de Swagger UI
 * e inyecta un script JavaScript personalizado antes del cierre del tag body.
 * Esto permite extender y personalizar la funcionalidad de Swagger UI sin
 * modificar directamente los archivos originales.
 * 
 * Implementa la interfaz Filter de Jakarta Servlet para integrarse en el
 * ciclo de vida de procesamiento de peticiones HTTP.
 */
public class SwaggerUIFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        CustomResponseWrapper wrapper = new CustomResponseWrapper(httpResponse);

        chain.doFilter(request, wrapper);

        String content = wrapper.getContent();
        String originalContentType = wrapper.getContentType();

        // Solo modificar si es HTML y contiene </body>
        if (originalContentType != null && originalContentType.contains("text/html") && content.contains("</body>")) {
            String modifiedContent = content.replace(
                    "</body>",
                    "<script src=\"/api/js/swagger-personal.js\" charset=\"UTF-8\"></script></body>"
            );

            // Mantener el Content-Type original
            response.setContentType(originalContentType); 
            byte[] modifiedBytes = modifiedContent.getBytes(StandardCharsets.UTF_8);
            response.setContentLength(modifiedBytes.length);

            ServletOutputStream out = response.getOutputStream();
            out.write(modifiedBytes);
            out.flush();
        } else {
            // Para contenido no HTML, pasar la respuesta original sin modificar
            if (originalContentType != null) {
                response.setContentType(originalContentType);
            }
                ServletOutputStream out = response.getOutputStream();
                out.write(content.getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
    }

    private static class CustomResponseWrapper extends HttpServletResponseWrapper {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        private PrintWriter writer;
        private ServletOutputStream servletOutputStream;

        public CustomResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (servletOutputStream != null) {
                throw new IllegalStateException("OutputStream ya se ha usado");
            }
            if (writer == null) {
                writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            }
            return writer;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (writer != null) {
                throw new IllegalStateException("Writer ya se ha usado");
            }
            if (servletOutputStream == null) {
                servletOutputStream = new ServletOutputStream() {
                    @Override
                    public void write(int b) {
                        outputStream.write(b);
                    }

                    @Override
                    public boolean isReady() {
                        return true;
                    }

                    @Override
                    public void setWriteListener(WriteListener listener) {
                    }
                };
            }
            return servletOutputStream;
        }

        public String getContent() throws IOException {
            if (writer != null) {
                writer.flush(); // Añade esto antes de cerrar
                writer.close();
            }
            if (servletOutputStream != null) {
                servletOutputStream.flush(); // Añade esto también
            }
            return outputStream.toString(StandardCharsets.UTF_8.name());
        }
    }
}