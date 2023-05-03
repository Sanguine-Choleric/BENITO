package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        jsonCourses = new JSONArray();

        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "CSC101 Course 1");

        jsonCourses.put(jsonCourse);

        App.db.courseLOAD(jsonCourses);

        jsonAssignments = new JSONArray();
        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1000);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", "2023-04-23T06:59:59Z");
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);

        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1010);
        jsonAssignment.put("name", "Assignment 2");
        jsonAssignment.put("due_at", "2023-04-24T06:59:59Z");
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);

        App.db.assLOAD(jsonAssignments);

        String s = messageBuilder.convert(App.db.getAllAss_AL()).get(0);
        assertEquals("`04/22 23:59 | CSC101 |` Assignment 1\n" + "`04/23 23:59 | CSC101 |` Assignment 2", s);
    }
}