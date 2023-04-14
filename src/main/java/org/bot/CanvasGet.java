package org.bot;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Provides methods to interact with the Canvas API to retrieve information needed for functions that need them.
 */
public class CanvasGet {
    /**
     * Sends a GET request to a Canvas API endpoint and returns a JSONArray of the response
     *
     * @param url The URL of the Canvas API endpoint to send the GET request to
     * @return A JSONArray of the response from the Canvas API endpoint
     * @throws IOException If there is an error reading the response from the API endpoint
     */
    @Nullable
    private static JSONArray canvasAPIGetter(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + API_keys.CanvasKey);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            JSONArray courses = new JSONArray(response);
            reader.close();

            return courses;
        } else {
            return null;
        }
    }

    /**
     * Retrieves a JSONArray of all classes from the Canvas API using the "courses" endpoint.
     *
     * @return A JSONArray of all classes from the Canvas API
     * @throws Exception If there is an error retrieving the classes from the API
     */
    public static JSONArray getClasses() throws Exception {
        String url = "https://csus.instructure.com/api/v1/courses";
        return canvasAPIGetter(url);
    }

    /**
     * Retrieves a JSONArray of all homework assignments for a specific class from the Canvas API using the "assignments" endpoint.
     *
     * @return A JSONArray of all homework assignments for a specific class from the Canvas API
     * @throws Exception If there is an error retrieving the homework assignments from the API
     */
    public static JSONArray getHW() throws Exception {
        String url = "https://csus.instructure.com/api/v1/courses/" + 102203 + "/assignments";
        return canvasAPIGetter(url);
    }
}
