package com.example.lluismasdeu.pprog2_p_final.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase encargada de la gestión con el Webservice.
 * @author Eloy Alberto López
 * @author Lluís Masdeu
 */
public class HttpRequestHelper {
    private static final String TAG = "HttpRequestHelper";
    private static final String WEBSERVICE_URL =
            "http://testapi-pprog2.azurewebsites.net/api/locations.php?method=getLocations";
    private static final String WEBSERVICE_SEARCH_METHOD = "&s=";
    private static final String WEBSERVICE_LATITUDE_METHOD = "&lat=";
    private static final String WEBSERVICE_LONGITUDE_METHOD = "&lon=";
    private static final String WEBSERVICE_DISTANCE_METHOD = "&dist=";
    private final int DEFAULT_TIMEOUT = 500;

    private static HttpRequestHelper instance = null;

    /**
     * Constructor de la clase.
     */
    private HttpRequestHelper() {}

    /**
     * Getter de la instancia de la clase.
     * @return Instancia de la clase.
     */
    public static HttpRequestHelper getInstance() {
        if (instance == null)
            instance = new HttpRequestHelper();

        return instance;
    }

    /**
     * Método encargado de llevar a cabo la petición al Webservice.
     * @param url Petición a enviar.
     * @return Respuesta del Webservice.
     */
    public JSONArray doHttpRequest(String url) {
        HttpURLConnection c = null;
        JSONArray jsonArray = new JSONArray();

        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(DEFAULT_TIMEOUT);
            c.setReadTimeout(DEFAULT_TIMEOUT);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null)
                        sb.append(line).append("\n");

                    br.close();
                    jsonArray = new JSONArray(sb.toString());
            }
        } catch (Exception ex) {
            Log.e(getClass().getName(), "Exception", ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Log.e(getClass().getName(), "Exception", ex);
                }
            }
        }

        return jsonArray;
    }

    /**
     * Método encargado de generar la petición de búsqueda por campo de texto.
     * @param search Búsqueda a llevar a cabo.
     * @return Petición preparada para ser enviada.
     */
    public String generateHTTPSearchRequest(String search) {
        String finalSearch = search.replaceAll("\\s+","+");

        StringBuilder builder = new StringBuilder();
        builder.append(WEBSERVICE_URL).append(WEBSERVICE_SEARCH_METHOD).append(finalSearch);

        return builder.toString();
    }

    /**
     * Método encargado de generar la petición de búsqueda por localización.
     * @param latitude Latitud.
     * @param longitude Longitud.
     * @param radius Radio de búsqueda.
     * @return
     */
    public String generateHTTPLocationRequest(double latitude, double longitude, int radius){
        StringBuilder builder = new StringBuilder();
        builder.append(WEBSERVICE_URL).append(WEBSERVICE_LATITUDE_METHOD).append(latitude)
                .append(WEBSERVICE_LONGITUDE_METHOD).append(longitude)
                .append(WEBSERVICE_DISTANCE_METHOD).append(radius);

        return builder.toString();
    }
}
