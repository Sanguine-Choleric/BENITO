package org.bot;

import org.json.JSONArray;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AppTest {
    // TODO: Figure out how to test API stuff if at all

    // Unit-test demo. Mocks a fake JSON source, compares with what's grabbed from Canvas
    // Extremely flawed test that really doesn't do anything
    @Test
    public void testCanvasAPIGetter() throws IOException {
        // Mock the HttpURLConnection object
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        InputStream inputStream = getClass().getResourceAsStream("/org/bot/allAssignments-test.json");
        when(connection.getInputStream()).thenReturn(inputStream);

        // Call the method being tested
        JSONArray assignments = CanvasGet.canvasAPIGetter(connection);

        // Verify the HTTP request was made with the correct parameters
        verify(connection).setRequestMethod("GET");
        verify(connection).setRequestProperty("Authorization", "Bearer " + API_keys.CanvasKey);

        // Verify the response was parsed correctly
        assertEquals(36, assignments.length());
        assertEquals("Core Quiz 1: Software Engineering Concepts Quiz", assignments.getJSONObject(0).getString("name"));
        assertEquals("Portfolio Scores and Advising Notes - Due Date is the date scores are released ", assignments.getJSONObject(35).getString("name"));
    }
}
