package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageBuilderTest {
    Database database;
    MessageBuilder messageBuilder;
    JSONObject jsonCourse;
    JSONArray jsonCourses;
    JSONObject jsonAssignment;
    JSONArray jsonAssignments;

    @BeforeEach
    void setUp() {
        database = new Database();
        messageBuilder = new MessageBuilder(database);
    }

    // Test for courses
    @Test
    void convert_courses() {
        // Initializing test setup
        // Adds two courses
        database.clear();
        jsonCourses = new JSONArray();
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "Course 1");
        jsonCourses.put(jsonCourse);
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 101);
        jsonCourse.put("name", "Course 2");
        jsonCourses.put(jsonCourse);
        database.courseLoad(jsonCourses);

        // Test starts here
        List<String> s = messageBuilder.convertCourses(database.getCourses());
        assertEquals("```Course 1\n" +
                "Course 2```", s.get(0));
    }

    // Test for assignments
    @Test
    void convert_assignments() {
        database.clear();
        // Regular list of courses
        jsonCourses = new JSONArray();

        // Creating single course
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "CSC101 Course 1");

        jsonCourses.put(jsonCourse);

        database.courseLoad(jsonCourses);

        // Regular assignment
        jsonAssignments = new JSONArray();
        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1000);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", "2023-04-23T06:59:59Z");
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);

        // Fix for GitHub actions and changing timezones
        LocalDateTime localized = ZonedDateTime.parse(jsonAssignment.getString("due_at"), DateTimeFormatter.ISO_DATE_TIME)
                .withZoneSameInstant(java.time.ZoneId.systemDefault()).toLocalDateTime();
        String localized_s = localized.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));

        // Assignment with null date
        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1010);
        jsonAssignment.put("name", "Assignment 2");
        jsonAssignment.put("due_at", JSONObject.NULL);
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);

        // Assignment with long name
        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1020);
        jsonAssignment.put("name", "Assignment 3 with a very long name that should be truncated and have three periods appended to the end");
        jsonAssignment.put("due_at", JSONObject.NULL);
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);

        // Assignment with no matching course
        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1030);
        jsonAssignment.put("name", "Assignment 4");
        jsonAssignment.put("due_at", JSONObject.NULL);
        jsonAssignment.put("course_id", 101);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);

        database.assignmentLoad(jsonAssignments);

        String s = messageBuilder.convertAssignments(database.getAssignments()).get(0);

        assertEquals("```" + localized_s + " | CSC101 | Assignment 1\n" +
                "            | CSC101 | Assignment 2\n" +
                "            | CSC101 | Assignment 3 with a very long name that should be ...\n" +
                "            |        | Assignment 4```", s);
    }

    // Testing for large number of assignments
    @Test
    void convert_assignments_large() {
        // Initializing blank database with single course
        database.clear();
        jsonCourses = new JSONArray();
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "CSC101");
        jsonCourses.put(jsonCourse);
        database.courseLoad(jsonCourses);

        // Initializing list of assignments
        jsonAssignments = new JSONArray();

        // Creating 100 assignments to exceed 2000char limit
        for (int i = 0; i < 100; i++) {
            jsonAssignment = new JSONObject();
            jsonAssignment.put("id", 1000 + i);
            jsonAssignment.put("name", "Assignment " + i);
            jsonAssignment.put("due_at", JSONObject.NULL);
            jsonAssignment.put("course_id", 100);
            jsonAssignment.put("has_submitted_submissions", true);
            jsonAssignments.put(jsonAssignment);
        }

        database.assignmentLoad(jsonAssignments);

        List<String> sArr = messageBuilder.convertAssignments(database.getAssignments());
        assertTrue(sArr.size() > 1);

        for (String s : sArr) {
            assertTrue(s.length() < 2000);
        }
    }

}