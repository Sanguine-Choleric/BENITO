package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MessageBuilderTest {
    MessageBuilder messageBuilder;
    Database database;
    JSONArray jsonCourses;
    JSONObject jsonCourse;
    JSONArray jsonAssignments;
    JSONObject jsonAssignment;
    Course course;

    @BeforeEach
    void setUp() {
        database = new Database();
        jsonCourses = new JSONArray();
        jsonAssignments = new JSONArray();
        messageBuilder = new MessageBuilder(database);

    }

    @Test
    void convertCourses() {
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "Course 1");
        jsonCourses.put(jsonCourse);

        jsonCourse = new JSONObject();
        jsonCourse.put("id", 200);
        jsonCourse.put("name", "Course 2");
        jsonCourses.put(jsonCourse);

        database.courseLoad(jsonCourses);

        ArrayList<String> expected = new ArrayList<>();
        expected.add("Course 1");
        expected.add("Course 2");

        messageBuilder.setCourses(database.getCourses());
        assertEquals(expected, messageBuilder.getCourses(), "Testing convertCourses");
//        System.out.println(messageBuilder.convertCourses(database.getCourses()));
    }

    @Test
    void coursesToMessages() {
        // 250 loops because 2000/8 chars. "Course 1" is 8 chars. No, it's not perfect
        for (int i = 0; i < 250; i++) {
            jsonCourse = new JSONObject();
            jsonCourse.put("id", i);
            jsonCourse.put("name", "Course" + i);
            jsonCourses.put(jsonCourse);
        }

        database.courseLoad(jsonCourses);
        messageBuilder.setCourses(database.getCourses());
        ArrayList<String> actual = messageBuilder.stringsToMessages(messageBuilder.getCourses());

        assertEquals(2, actual.size());

        // check for \n char. String end shouldn't be "\n```"
        String s = actual.get(0).substring(actual.get(0).length() - 4);
        assertFalse(s.equals("\n"));
//        System.out.println(actual.get(0).length());
    }

//    @Test
//    void formatAssignmentDueDate() {
//        jsonAssignment = new JSONObject();
//        jsonAssignment.put("id", 1000);
//        jsonAssignment.put("name", "Assignment 1");
//        jsonAssignment.put("due_at", "2020-12-31T23:59:00Z");
//        jsonAssignment.put("course_id", 100);
//        jsonAssignment.put("has_submitted_submissions", true);
//        jsonAssignments.put(jsonAssignment);
//
//        // Test due date with width 0
//        Assignment assignment = new Assignment(jsonAssignment);
//        String actual = messageBuilder.formatAssignmentDueDate(assignment, 0);
//        assertEquals("12/31 03:59", actual, "Testing assignment no width");
//
//        // Test due date with width 12
//        assignment = new Assignment(jsonAssignment);
////        String expected = String.format("%-12s", "12/31 03:59");
//        String expected = "12/31 03:59 ";
//        actual = messageBuilder.formatAssignmentDueDate(assignment, 12);
//        assertEquals(expected, actual, "Testing assignment long width");
//    }
//
//    @Test
//    void formatAssignmentDueDate_null() {
//        jsonAssignment = new JSONObject();
//        jsonAssignment.put("id", 1000);
//        jsonAssignment.put("name", "Assignment 1");
//        jsonAssignment.put("due_at", JSONObject.NULL);
//        jsonAssignment.put("course_id", 100);
//        jsonAssignment.put("has_submitted_submissions", true);
//        jsonAssignments.put(jsonAssignment);
//
//        // Test null due date with width 0
//        Assignment assignment = new Assignment(jsonAssignment);
//        String actual = messageBuilder.formatAssignmentDueDate(assignment, 0);
//        assertEquals("", actual, "Testing assignment null date no width");
//
//        // Test null due date with width 12 - longer than expected "MM/dd hh:mm" format
//        String expected = String.format("%-12s", "");
//        actual = messageBuilder.formatAssignmentDueDate(assignment, 12);
//        assertEquals(expected, actual, "Testing assignment null date long width");
//    }

//    @Test
//    void formatAssignmentCourseName() {
//        jsonCourse = new JSONObject();
//        jsonCourse.put("id", 100);
//        jsonCourse.put("name", "CSC131 Software Engineering");
//        jsonCourses.put(jsonCourse);
//        database.courseLoad(jsonCourses);
//
//        jsonAssignment = new JSONObject();
//        jsonAssignment.put("id", 1000);
//        jsonAssignment.put("name", "Assignment 1");
//        jsonAssignment.put("due_at", JSONObject.NULL);
//        jsonAssignment.put("course_id", 100);
//        jsonAssignment.put("has_submitted_submissions", true);
//
//        // Test assignment's course name with width 0
//        Assignment assignment = new Assignment(jsonAssignment);
//        String actual = messageBuilder.formatAssignmentCourseName(assignment, 0);
//        assertEquals("CSC131", actual, "Testing assignment course name no width");
//
//        // Should be max length for course like BIOL400 - 7 chars
//        actual = messageBuilder.formatAssignmentCourseName(assignment, 7);
//        assertEquals("CSC131 ", actual, "Testing assignment course name long width");
//    }
//
//    @Test
//    void formatAssignmentName() {
//        jsonAssignment = new JSONObject();
//        jsonAssignment.put("id", 1000);
//        jsonAssignment.put("name", "Assignment 1");
//        jsonAssignment.put("due_at", JSONObject.NULL);
//        jsonAssignment.put("course_id", 100);
//        jsonAssignment.put("has_submitted_submissions", true);
//
//        Assignment assignment = new Assignment(jsonAssignment);
//        String actual = messageBuilder.formatAssignmentName(assignment, 0);
//        assertEquals("Assignment 1", actual, "Testing assignment name");
//    }

    @Test
    void calculateAssignmentColumnWidths() {
        // Course name width tests
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "CSC131 Software Engineering");
        jsonCourses.put(jsonCourse);
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 200);
        jsonCourse.put("name", "BIOL400 Biology");
        jsonCourses.put(jsonCourse);
        database.courseLoad(jsonCourses);

        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1000);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", JSONObject.NULL);
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);
        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1000);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", "2020-12-31T23:59:00Z");
        jsonAssignment.put("course_id", 200);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignments.put(jsonAssignment);
        database.assignmentLoad(jsonAssignments);

        messageBuilder.setCourses(database.getCourses());
        messageBuilder.setAssignments(database.getAssignments());
        int[] actual = messageBuilder.calculateAssignmentColumnWidths(database.getAssignments());
        int[] expected = {11, "BIOL400".length(), "Assignment 1".length()};

        assertArrayEquals(expected, actual, "Testing assignment column widths");


    }
}