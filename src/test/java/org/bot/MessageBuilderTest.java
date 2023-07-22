package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageBuilderTest {
    Database database;
    JSONArray jsonCourses;
    JSONObject jsonCourse;
    JSONArray jsonAssignments;
    JSONObject jsonAssignment;

    @BeforeEach
    void setUp() {
        database = new Database();
        jsonCourses = new JSONArray();
        jsonAssignments = new JSONArray();
    }

    @Nested
    class courseMessagesTests {
        @Test
        void courseMessages() {
            jsonCourse = new JSONObject();
            jsonCourse.put("id", 1);
            jsonCourse.put("name", "Course 1");
            jsonCourses.put(jsonCourse);

            jsonCourse = new JSONObject();
            jsonCourse.put("id", 1);
            jsonCourse.put("name", "Course 2");
            jsonCourses.put(jsonCourse);

            database.courseLoad(jsonCourses);
            List<Course> courses = database.getCourses();

            String expected = "```" +
                    "Course 1\n" +
                    "Course 2" +
                    "```";
            String actual = MessageBuilder.courseMessages(courses).get(0);
            assertEquals(expected, actual);
        }

        @Test
        void courseMessagesEmpty() {
            String expected = "```" +
                    "No courses found" +
                    "```";
            String actual = MessageBuilder.courseMessages(database.getCourses()).get(0);
            assertEquals(expected, actual);
        }

        @Test
        void courseMessagesMass() {
            for (int i = 0; i < 211; i++) {
                jsonCourse = new JSONObject();
                jsonCourse.put("id", i);
                jsonCourse.put("name", "Course " + i);
                jsonCourses.put(jsonCourse);
            }
            database.courseLoad(jsonCourses);

            List<String> actual = MessageBuilder.courseMessages(database.getCourses());
            assertEquals(2, actual.size());
            for (String s : actual) {
                assertTrue(s.length() <= 2000);
            }
        }
    }

    @Nested
    class assignmentMessagesTests {
        @Test
        void assignmentMessages() {
            jsonCourse = new JSONObject();
            jsonCourse.put("id", 1);
            jsonCourse.put("name", "Course1");
            jsonCourses.put(jsonCourse);

            jsonCourse = new JSONObject();
            jsonCourse.put("id", 10);
            jsonCourse.put("name", "Course10");
            jsonCourses.put(jsonCourse);

            jsonAssignment = new JSONObject();
            jsonAssignment.put("id", 10);
            jsonAssignment.put("name", "Assignment 1");
            jsonAssignment.put("due_at", "2020-01-01T00:00:00Z");
            jsonAssignment.put("has_submitted_submissions", true);
            jsonAssignment.put("course_id", 1);
            jsonAssignments.put(jsonAssignment);

            jsonAssignment = new JSONObject();
            jsonAssignment.put("id", 20);
            jsonAssignment.put("name", "Assignment 2");
            jsonAssignment.put("due_at", JSONObject.NULL);
            jsonAssignment.put("has_submitted_submissions", true);
            jsonAssignment.put("course_id", 10);
            jsonAssignments.put(jsonAssignment);

            database.courseLoad(jsonCourses);
            database.assignmentLoad(jsonAssignments);

            String message = "```" +
                    "12/31 04:00 | Course1  | Assignment 1\n" +
                    "            | Course10 | Assignment 2" +
                    "```";

            assertEquals(message, MessageBuilder.assignmentMessages(database.getAssignments(), database.getCourses()).get(0));
        }

        @Test
        void assignmentMessagesEmpty() {
            String expected = "```" +
                    "No assignments found" +
                    "```";
            String actual = MessageBuilder.assignmentMessages(database.getAssignments(), database.getCourses()).get(0);
            assertEquals(expected, actual);
        }

        @Test
        void assignmentMessagesMass() {
            jsonCourse = new JSONObject();
            jsonCourse.put("id", 1);
            jsonCourse.put("name", "Course1");
            jsonCourses.put(jsonCourse);

            for (int i = 0; i < 100; i++) {
                jsonAssignment = new JSONObject();
                jsonAssignment.put("id", i);
                jsonAssignment.put("name", "Assignment " + i);
                jsonAssignment.put("due_at", "2020-01-01T00:00:00Z");
                jsonAssignment.put("has_submitted_submissions", true);
                jsonAssignment.put("course_id", 1);
                jsonAssignments.put(jsonAssignment);
            }

            database.courseLoad(jsonCourses);
            database.assignmentLoad(jsonAssignments);

            List<String> actual = MessageBuilder.assignmentMessages(database.getAssignments(), database.getCourses());
            for (String s : actual) {
                assertTrue(s.length() <= 2000);
//                System.out.println(s);
            }
            assertEquals(2, actual.size());
        }
    }
}