package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Provides methods to interact with the Canvas API to retrieve information
 * needed for functions that need them.
 */
public class CanvasGet {
    Database database;

    public CanvasGet(Database database) {
        this.database = database;
    }

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
    protected JSONArray canvasAPIGetter(HttpURLConnection connection) throws IOException {
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
            return new JSONArray();
        }
    }

    /**
     * Retrieves a JSONArray of all courses from the Canvas API using the "courses"
     * endpoint.
     *
     * @return A JSONArray of all classes from the Canvas API
     * @throws Exception If there is an error retrieving the course from the API
     */
    public JSONArray getCourses() throws Exception {
        String url = "https://csus.instructure.com/api/v1/courses?include.per_page_1000&enrollment_state=active&enrollment_role_id=3";
        HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
//        return canvasAPIGetter(connection);
        JSONArray response = canvasAPIGetter(connection);
        for (int i = response.length() - 1; i >= 0; i--) {
            JSONObject jsonObject = response.getJSONObject(i);
            int courseId = jsonObject.getInt("id");
            if (courseId <= 100000) {
                response.remove(i);
            }
        }
        return response;
    }

    /**
     * Retrieves a JSONArray of all homework assignments for all enrolled courses.
     * <p>
     * MUST HAVE an already populated `App.courses` in order access assignments for each course.
     *
     * @return A JSONArray of all assignments from all currently enrolled courses
     * @throws Exception If there is an error retrieving the homework assignments from the API
     */
    public JSONArray getAllAssignments(Database db) throws Exception {
        if (db.getCourses().isEmpty()) {
            return new JSONArray();
        }

        JSONArray allAssignments = new JSONArray();
        int[] courseIds = new int[db.getCourses().size()];

        // Building list of course ids to use to build urls
        // Canvas only allows grabbing assignments from one course ata time
        for (int i = 0; i < db.getCourses().size(); i++) {
            courseIds[i] = db.getCourses().get(i).getCourseID();
        }

        for (int courseId : courseIds) {
            // TODO: Fix API response error - Some classes don't have a courseId?. Temp fix by filtering for large course IDs.
            // Pagination fix - Grabs assignments by page until nothing is returned
            int page = 1;
            boolean moreAssignments = true;
            while (moreAssignments) {
                String url = "https://csus.instructure.com/api/v1/courses/" + courseId + "/assignments?per_page=50&page=" + page;
                HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
                JSONArray assignmentsSingleCourse = canvasAPIGetter(connection);

                // Unpacking each JSONArray received from each url and
                // Recombining into a single mega-JSONArray
                for (int i = 0; i < assignmentsSingleCourse.length(); i++) {
                    JSONObject assignment = assignmentsSingleCourse.getJSONObject(i);
                    allAssignments.put(assignment);
                }

                // Check if there are more assignments to retrieve
                if (assignmentsSingleCourse.length() == 0) {
                    moreAssignments = false;
                } else {
                    page++;
                }
            }
        }
        return allAssignments;
    }

}
