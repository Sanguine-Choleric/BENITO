package org.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
