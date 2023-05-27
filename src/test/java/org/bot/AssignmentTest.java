package org.bot;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentTest {
    JSONObject jsonAssignment = new JSONObject();
    Assignment assignment;

    @BeforeEach
    void setUp() {
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("id", 100);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", "2023-04-23T06:59:59Z");
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignment.put("description", "This is an assignment, but this description shouldn't show up in assignment list.");
        assignment = new Assignment(jsonAssignment);
    }

    @Test
    void testGetAssDate() {
        assertEquals(jsonAssignment.getString("due_at"), assignment.getAssDate(), "Testing date getter");
    }

    @Test
    void testGetAssID() {
        assertEquals(jsonAssignment.getInt("id"), assignment.getAssID(), "Testing ID getter");
    }

    @Test
    void testGetAssName() {
        assertEquals(jsonAssignment.getString("name"), assignment.getAssName(), "Testing name getter");
    }

    @Test
    void testGetCourseID() {
        // Flip this unit test
        assertEquals(jsonAssignment.getInt("course_id"), assignment.getCourseID(), "Testing Course ID getter");
    }

    @Test
    void testGetDateFormat() {
        LocalDateTime localized = ZonedDateTime.parse(assignment.getAssDate(), DateTimeFormatter.ISO_DATE_TIME)
                .withZoneSameInstant(java.time.ZoneId.systemDefault()).toLocalDateTime();
        assertEquals(localized.toString(), assignment.getDateFormat().toString(), "Testing date format getter");
    }

    @Test
    void testGetDateNull() {
        jsonAssignment = new JSONObject();
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("id", 100);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignment.put("due_at", JSONObject.NULL);
        assignment = new Assignment(jsonAssignment);
        assertEquals("null", assignment.getAssDate());
        assertNull(assignment.getDateFormat(), "Testing date format getter with null date");

    }

    @Test
    void getHasBeenSubmitted() {
        // No submission
        jsonAssignment = new JSONObject();
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("id", 100);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("has_submitted_submissions", false);
        jsonAssignment.put("due_at", "2023-04-23T06:59:59Z");
        assignment = new Assignment(jsonAssignment);

        assertFalse(assignment.getHasBeenSubmitted(), "Testing hasBeenSubmitted getter with no submission");
    }
}
