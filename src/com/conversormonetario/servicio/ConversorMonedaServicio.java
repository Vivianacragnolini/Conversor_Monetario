package com.conversormonetario.servicio;

import com.conversormonetario.modelo.Moneda;
import com.conversormonetario.api.Monedas;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ConversorMonedaServicio {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/3f4db645abd7e2c221a660a8";

    public List<Moneda> obtenerListaDeMonedas() throws Exception {
        String urlStr = API_URL + "/codes";
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Leer respuesta
        InputStreamReader reader = new InputStreamReader(con.getInputStream());
        Gson gson = new Gson();
        Map<String, Object> respuesta = gson.fromJson(reader, Map.class);

        // Convertir el resultado a una lista de objetos Moneda
        List<List<String>> codigosMonedas = (List<List<String>>) respuesta.get("supported_codes");
        List<Moneda> listaDeMonedas = new ArrayList<>();

        for (List<String> moneda : codigosMonedas) {
            listaDeMonedas.add(new Moneda(moneda.get(0), moneda.get(1)));
        }
        return listaDeMonedas;
    }

    public double convertir(String monedaOrigen, String monedaDestino, double cantidad) throws Exception {
        String urlStr = API_URL + "/pair/" + monedaOrigen + "/" + monedaDestino;
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Leer respuesta
        InputStreamReader reader = new InputStreamReader(con.getInputStream());
        Gson gson = new Gson();
        Map<String, Object> respuesta = gson.fromJson(reader, Map.class);

        double tasaCambio = (double) respuesta.get("conversion_rate");

        return cantidad * tasaCambio;
    }
}
