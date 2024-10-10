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

@WebServlet("/api/*") // Ruta base para las solicitudes
public class MonedaApi extends HttpServlet {

    private ConversorMonedaServicio servicio = new ConversorMonedaServicio();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path != null && path.equals("/monedas")) {
            obtenerMonedas(resp);
        } else if (path != null && path.equals("/convertir")) {
            convertirMoneda(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Ruta no encontrada.");
        }
    }

    private void obtenerMonedas(HttpServletResponse resp) throws IOException {
        try {
            List<Moneda> monedas = servicio.obtenerListaDeMonedas();
            String json = new Gson().toJson(monedas);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al obtener las monedas: " + e.getMessage());
        }
    }

    private void convertirMoneda(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String monedaOrigen = req.getParameter("origen");
            String monedaDestino = req.getParameter("destino");
            String cantidadStr = req.getParameter("cantidad");

            if (monedaOrigen == null || monedaDestino == null || cantidadStr == null || monedaOrigen.isEmpty() || monedaDestino.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Parámetros inválidos.");
                return;
            }

            double cantidad;
            try {
                cantidad = Double.parseDouble(cantidadStr);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Cantidad inválida.");
                return;
            }

            double resultado = servicio.convertir(monedaOrigen, monedaDestino, cantidad);
            resp.setContentType("application/json");
            resp.getWriter().write(new Gson().toJson(resultado));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al
