package org.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

import org.json.JSONArray;

/**
 * Provides methods to interact with the Canvas API to retrieve information
 * needed for functions that need them.
 */
public class CanvasGet {
    /**
     * Sends a GET request to the Canvas API using the specified HttpURLConnection
     * object and returns
     * the response as a JSONArray.
     *
     * @param connection the HttpURLConnection object to use for the request. Built
     *                   from a String url.
     * @return a JSONArray containing the response from the Canvas API
     * @throws IOException if an I/O error occurs while sending the request or
     *                     reading the response
     */
    private static JSONArray canvasAPIGetter(HttpURLConnection connection) throws IOException {
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + API_keys.CanvasKey);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            String response = responseBuilder.toString();

            return new JSONArray(response);
        } else {
            return null;
        }

    }

    /**
     * Retrieves a JSONArray of all courses from the Canvas API using the "courses"
     * endpoint.
     *
     * @return A JSONArray of all classes from the Canvas API
     * @throws Exception If there is an error retrieving the course from the API
     */
    public static JSONArray getCourses() throws Exception {
        String url = "https://csus.instructure.com/api/v1/courses";
        HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        return canvasAPIGetter(connection);
    }

    /**
     * Retrieves a JSONArray of all homework assignments for a specific course from
     * the Canvas API using the "assignments" endpoint.
     *
     * @return A JSONArray of all homework assignments for a specific class from the
     *         Canvas API
     * @throws Exception If there is an error retrieving the homework assignments
     *                   from the API
     */
    public static JSONArray getHW() throws Exception {
        String url = "https://csus.instructure.com/api/v1/courses/" + 102203 + "/assignments";
        HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        return canvasAPIGetter(connection);
    }
}