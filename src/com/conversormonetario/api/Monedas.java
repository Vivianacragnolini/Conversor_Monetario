package com.conversormonetario.api;

import com.conversormonetario.modelo.Moneda;
import com.conversormonetario.servicio.ConversorMonedaServicio;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

    // Configura las rutas para el servlet
    @WebServlet("/api")
    public class Monedas extends HttpServlet {

        private ConversorMonedaServicio servicio = new ConversorMonedaServicio();

        // Maneja las solicitudes GET
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String path = req.getPathInfo();

            // Si la ruta es "/monedas", devolverá la lista de monedas disponibles
            if (path != null && path.equals("/codes")) {
                obtenerMonedas(resp);
            }
            // Si la ruta es "/convertir", procesará la solicitud de conversión
            else if (path != null && path.equals("/pair")) {
                convertirMoneda(req, resp);
            }
            // Si la ruta no coincide con ninguna de las anteriores, devolverá un error
            else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Ruta no encontrada.");
            }
        }

        // Método para obtener la lista de monedas
        private void obtenerMonedas(HttpServletResponse resp) throws IOException {
            try {
                List<Moneda> monedas = servicio.obtenerListaDeMonedas(); // Llama al servicio para obtener las monedas
                String json = new Gson().toJson(monedas); // Convierte la lista a JSON
                resp.setContentType("application/json");
                resp.getWriter().write(json); // Envía la respuesta al cliente
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error al obtener las monedas: " + e.getMessage());
            }
        }

        // Método para realizar la conversión de monedas
        private void convertirMoneda(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            try {
                // Obtener parámetros de la solicitud
                String monedaOrigen = req.getParameter("origen");
                String monedaDestino = req.getParameter("destino");
                String cantidadStr = req.getParameter("cantidad");

                // Validar los parámetros de entrada
                if (monedaOrigen == null || monedaDestino == null || cantidadStr == null || monedaOrigen.isEmpty() || monedaDestino.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Parámetros inválidos.");
                    return;
                }

                // Convertir la cantidad a double
                double cantidad;
                try {
                    cantidad = Double.parseDouble(cantidadStr);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Cantidad inválida.");
                    return;
                }

                // Llama al servicio para hacer la conversión
                double resultado = servicio.convertir(monedaOrigen, monedaDestino, cantidad);
                resp.setContentType("application/json");
                resp.getWriter().write(new Gson().toJson(resultado)); // Envía el resultado en formato JSON

            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error al convertir la moneda: " + e.getMessage());
            }
        }
    }

}
