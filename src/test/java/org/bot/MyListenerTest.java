package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyListenerTest {

    @Nested
    public class messageBuilderTests {
        JSONArray jsonArray = new JSONArray();
        JSONObject obj1 = new JSONObject();
        JSONObject obj2 = new JSONObject();

        @BeforeEach
        public void setup_messageBuilderTests() {
            obj1.put("name", "Assignment 1");
            obj1.put("value", "Task 1");
            obj2.put("name", "Assignment 2");
            obj2.put("value", "Task 2");
            jsonArray.put(obj1);
            jsonArray.put(obj2);
        }

        @Test
        public void testMessageBuilder_withNameKey() {
            // Create a test JSONArray with two JSONObjects containing the "name" key
            // Call the messageBuilder method with the "name" key
            String result = MyListener.messageBuilder(jsonArray, "name");

            // Check that the resulting string contains the expected values
            assertTrue(result.contains("Assignment 1"));
            assertTrue(result.contains("Assignment 2"));
        }

        @Test
        public void testMessageBuilder_withValueKey() {
            // Call the messageBuilder method with the "value" key
            String result = MyListener.messageBuilder(jsonArray, "value");

            // Check that the resulting string contains the expected values
            assertTrue(result.contains("Task 1"));
            assertTrue(result.contains("Task 2"));
        }

        @Test
        public void testMessageBuilder_withInvalidKey() {
            // Call the messageBuilder method with the "name" key
            String result = MyListener.messageBuilder(jsonArray, "badkey");

            // Check that the resulting string is empty
            assertEquals("", result);
        }

        @Test
        public void testMessageBuilder_withEmptyJSONArray() {
            // Create an empty test JSONArray
            JSONArray jsonArray = new JSONArray();

            // Call the messageBuilder method with the "name" key
            String result = MyListener.messageBuilder(jsonArray, "name");

            // Check that the resulting string is empty
            assertEquals("", result);
        }
    }
}