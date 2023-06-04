package org.bot;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CanvasGetTest {
    Database database = new Database();
    CanvasGet canvasGet = new CanvasGet(database);

    @Test
    public void testCanvasAPIGetter() throws IOException {
        // Mock the HttpURLConnection object
        HttpURLConnection connection = mock(HttpURLConnection.class);

        // Set up the expected response from the API
        JSONArray expectedResponse = new JSONArray("[{\"id\":1,\"name\":\"John\"}]");
        InputStream responseStream = new ByteArrayInputStream(
                expectedResponse.toString().getBytes(StandardCharsets.UTF_8));

        // Mock the response from the API
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getInputStream()).thenReturn(responseStream);

        // Call the method being tested
        JSONArray actualResponse = canvasGet.canvasAPIGetter(connection);

        // Assert that the actual response matches the expected response
        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }

    @Test
    public void testCanvasAPIGetterBadHTTP() throws IOException {
        // Mock the HttpURLConnection object
        HttpURLConnection connection = mock(HttpURLConnection.class);

        // Set up the expected response from the API
        JSONArray expectedResponse = new JSONArray();
        InputStream responseStream = new ByteArrayInputStream(
                expectedResponse.toString().getBytes(StandardCharsets.UTF_8));

        // Mock the response from the API
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN);
        when(connection.getInputStream()).thenReturn(responseStream);

        // Call the method being tested
        JSONArray actualResponse = canvasGet.canvasAPIGetter(connection);

        // Assert that the actual response matches the expected response
        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }

    /**
     * Good tests for coverage, but not mocked, and are very slow (~10s). Uncomment if you want to run them.
     */
    // Testing that the method returns something. If this fails, you fucked up
    // (or Canvas did).
    @Test
    void testGetCourses() throws Exception {
        // Real courses
        JSONArray courses = canvasGet.getCourses();
        assertNotNull(courses);
    }

    // Testing that the method fails on failed get. If this fails, you fucked up
    // (or Canvas did)
    @Test
    void testGetAllAssignmentsNoCourses() throws Exception {
        database.clear();
        JSONArray assignments = canvasGet.getAllAssignments(database);
        assertNotNull(assignments);
        assertEquals(0, assignments.length());
    }

    // Testing that the method returns something. If this fails, you fucked up
    // (or Canvas did)
    @Test
    void testGetAllAssignments() throws Exception {
        database.courseLOAD(canvasGet.getCourses());
        JSONArray assignments = canvasGet.getAllAssignments(database);
        assertNotNull(assignments);
    }
}
