package org.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

public class CanvasGetTest {

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
        JSONArray actualResponse = CanvasGet.canvasAPIGetter(connection);

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
        JSONArray actualResponse = CanvasGet.canvasAPIGetter(connection);

        // Assert that the actual response matches the expected response
        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }

    /**
     * Good tests for coverage, but not mocked, and are very slow (~10s). Uncomment if you want to run them.
     */
    // Testing that the method returns something. If this fails, you fucked up
    // (or Canvas did).
    // @Test
    // void testGetCourses() throws Exception {
    //     // Real courses
    //     JSONArray courses = CanvasGet.getCourses();
    //     assertNotNull(courses);
    // }

    // // Testing that the method fails on failed get. If this fails, you fucked up 
    // // (or Canvas did)
    // @Test
    // void testGetAllAssignmentsNoCourses() throws Exception {
    //     App.db.clear();
    //     JSONArray assignments = CanvasGet.getAllAssignments();
    //     assertNotNull(assignments);
    //     assertTrue(assignments.length() == 0);
    // }

    // // Testing that the method returns something. If this fails, you fucked up 
    // // (or Canvas did)
    // @Test
    // void testGetAllAssignments() throws Exception {
    //     App.db.courseLOAD(CanvasGet.getCourses());
    //     JSONArray assignments = CanvasGet.getAllAssignments();
    //     assertNotNull(assignments);
    // }
}
