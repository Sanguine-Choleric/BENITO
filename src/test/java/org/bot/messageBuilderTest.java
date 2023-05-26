package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageBuilderTest {
    Database db;
    JSONObject jsonCourse;
    JSONArray jsonCourses;
    JSONObject jsonAssignment;
    JSONArray jsonAssignments;

    @BeforeEach
    void setUp() {

    }

    @Test
    void convert() {
        App.db = new Database();

        // Regular list of courses
        jsonCourses = new JSONArray();

        // Creating single course
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "CSC101 Course 1");

        jsonCourses.put(jsonCourse);

        App.db.courseLOAD(jsonCourses);

        // Regular assignment
        jsonAssignments = new JSONArray();
        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1000);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", "2023-04-23T06:59:59Z");
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);

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

        App.db.assLOAD(jsonAssignments);

        String s = messageBuilder.convert(App.db.getAllAss_AL()).get(0);
        assertEquals("""
                `04/22 23:59 | CSC101  |` Assignment 1
                `00/00 00:00 | CSC101  |` Assignment 2
                `00/00 00:00 | CSC101  |` Assignment 3 with a very long name that should be truncated and have three ...
                `00/00 00:00 |         |` Assignment 4""", s);
    }

    @Test
    void convert_large() {
        // Initializing blank database with single course
        App.db = new Database();
        jsonCourses = new JSONArray();
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "CSC101");
        jsonCourses.put(jsonCourse);
        App.db.courseLOAD(jsonCourses);

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

        App.db.assLOAD(jsonAssignments);

        ArrayList<String> sArr = messageBuilder.convert(App.db.getAllAss_AL());
        assertTrue(sArr.size() > 1);

        for (String s : sArr) {
            assertTrue(s.length() < 2000);
        }

    }
}