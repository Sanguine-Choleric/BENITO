package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DueDateHandlerTest {
    JSONArray assignments = new JSONArray();
    JSONObject assignment = new JSONObject(),
            assignment2 = new JSONObject();

    @BeforeEach
    public void setup() {
        assignment.put("name", "Assignment 1");
        assignment.put("due_at", "2023-02-13T07:59:00Z");
        assignment.put("id", 100);

        assignment2.put("name", "Assignment 2");
        assignment2.put("due_at", "2023-02-12T07:59:00Z");
        assignment2.put("id", 200);

        assignments.put(assignment);
        assignments.put(assignment2);
    }

    @Test
    public void test() {
        Map<String, String[]> dueDates = DueDateHandler.upcomingDue(assignments);
        String message = "";

        // Create a list of map entries from the dueDates map
        List<Map.Entry<String, String[]>> sortedEntries = new ArrayList<>(dueDates.entrySet());

        // Sort the entries by due_at date, closest to current date first
        sortedEntries.sort((e1, e2) -> {
            LocalDate date1 = LocalDate.parse(e1.getValue()[1]);
            LocalDate date2 = LocalDate.parse(e2.getValue()[1]);
            return date1.compareTo(date2);
        });

        // Iterate through the sorted entries and print the due_at and name values
        for (Map.Entry<String, String[]> entry : sortedEntries) {
            String name = entry.getValue()[0];
            String dueAt = entry.getValue()[1];
            message = message.concat(dueAt + " " + name + "\n");
//            System.out.println(dueAt + " " + name);
        }
        System.out.println(message);
    }
}