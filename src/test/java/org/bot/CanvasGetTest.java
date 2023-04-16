package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class CanvasGetTest {
    @Nested
    public class canvasAPIGetterTests {
        JSONArray jsonArray = new JSONArray();
        JSONObject obj1 = new JSONObject();
        JSONObject obj2 = new JSONObject();

        @BeforeEach
        public void setup_canvasAPIGetterTests() {
            obj1.put("name", "Assignment 1");
            obj1.put("value", "Task 1");
            obj1.put("id", 100);
            obj2.put("name", "Assignment 2");
            obj2.put("value", "Task 2");
            obj2.put("id", 200);
            jsonArray.put(obj1);
            jsonArray.put(obj2);
        }

        @Test
        public void testCanvasAPIGetter() throws IOException {
            HttpURLConnection mockConnection = mock(HttpURLConnection.class);
            String jsonArrString = jsonArray.toString();
            InputStream jsonArrIS = new ByteArrayInputStream(jsonArrString.getBytes());

            // Setting up fake API endpoint
            // Returns known json file to test against
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
            when(mockConnection.getInputStream()).thenReturn(jsonArrIS);

            JSONArray realJsonArray = CanvasGet.canvasAPIGetter(mockConnection);

            // Checking that http headers in API request are correct
            verify(mockConnection).setRequestMethod("GET");
            verify(mockConnection).setRequestProperty("Authorization", "Bearer " + API_keys.CanvasKey);

            // Checking contents of retrieved vs known json
            assert realJsonArray != null;
            assertEquals(jsonArray.toString(), realJsonArray.toString());
        }

        @Test
        public void testCanvasAPIGetter_noConnection() throws IOException {
            HttpURLConnection mockConnection = mock(HttpURLConnection.class);
            String jsonArrString = jsonArray.toString();
            InputStream jsonArrIS = new ByteArrayInputStream(jsonArrString.getBytes());

            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

            JSONArray nullJsonArray = CanvasGet.canvasAPIGetter(mockConnection);

            assertNull(nullJsonArray);
        }
    }
}
