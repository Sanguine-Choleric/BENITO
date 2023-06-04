package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {
    JSONArray jsonCourses = new JSONArray();
    JSONArray jsonAssignments = new JSONArray();
    JSONObject jsonCourse;
    JSONObject jsonAssignment;
    Database db;

    @BeforeEach
    void setUp() {
        db = new Database();
        // Setting up dummy JSONArrays for testing
        // Each has an extra unnecessary field to make sure it's not being used
        // Update tests when new fields are needed
        jsonCourse = new JSONObject();
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "Course 1");
        jsonCourse.put("description", "This is a course, but this description shouldn't show up in course list.");
        jsonCourses.put(jsonCourse);

        jsonCourse = new JSONObject();
        jsonCourse.put("id", 101);
        jsonCourse.put("name", "Course 2");
        jsonCourse.put("description", "This is a course, but this description shouldn't show up in course list.");
        jsonCourses.put(jsonCourse);

        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1000);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", "2023-04-23T06:59:59Z");
        jsonAssignment.put("course_id", 100);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignment.put("description",
                "This is an assignment, but this description shouldn't show up in assignment list.");
        jsonAssignments.put(jsonAssignment);

        jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 1010);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", "2023-04-24T06:59:59Z");
        jsonAssignment.put("course_id", 101);
        jsonAssignment.put("has_submitted_submissions", true);
        jsonAssignment.put("description",
                "This is an assignment, but this description shouldn't show up in assignment list.");
        jsonAssignments.put(jsonAssignment);
    }

    @Test
    void getPastSubmittedAss_AL() {
        db.assignmentLoad(jsonAssignments);

        assertNotNull(db.getSubmitted());
    }

    @Test
    void setPastSubmittedAss_AL() {
        db.setSubmittedAssignments(new ArrayList<>());

        assertNotNull(db.getSubmitted());
    }

    @Test
    void getOverdueAss_AL() {
        db.assignmentLoad(jsonAssignments);

        assertNotNull(db.getOverdue());
    }

    @Test
    void setOverdueAss_AL() {
        db.setOverdueAssignments(new ArrayList<>());

        assertNotNull(db.getOverdue());
    }

    @Test
    void clear() {
        db.assignmentLoad(jsonAssignments);
        db.setOverdueAssignments(db.overdueAssignments(db.getAssignments()));
        db.setUpcomingAssignments(db.upcomingAssignments(db.getAssignments()));
        db.setSubmittedAssignments(db.submittedAssignments(db.getAssignments()));

        db.clear();
        assertEquals(0, db.getAssignments().size());
        assertEquals(0, db.getOverdue().size());
        assertEquals(0, db.getUpcoming().size());
        assertEquals(0, db.getSubmitted().size());
    }

    // Testing that getters and setters return the right type
    @Test
    void testGetCourses_AL() {
        db.courseLOAD(jsonCourses);

        assertNotNull(db.getCourses());
    }

    @Test
    void testGetAllAss_AL() {
        db.assignmentLoad(jsonAssignments);

        assertNotNull(db.getAssignments());
    }

    @Test
    void testGetUpcomingAss_AL() {
        db.assignmentLoad(jsonAssignments);

        assertNotNull(db.getUpcoming());
    }

    @Test
    void testSetUpcomingAss_AL() {
        db.setUpcomingAssignments(new ArrayList<>());

        assertNotNull(db.getUpcoming());
    }

    @Test
    void getUndatedAss_AL() {
        db.assignmentLoad(jsonAssignments);

        assertNotNull(db.getUndated());
    }

    @Test
    void setUndatedAss_AL() {
        db.setUndatedAssignments(new ArrayList<>());

        assertNotNull(db.getUndated());
    }

    @Nested
    class courseLOADTests {

        @Test
        void testCourseLOAD() {
            db.courseLOAD(jsonCourses);

            assertEquals(2, db.getCourses().size());

            // Checking that the courses were loaded correctly
            assertEquals(100, db.getCourses().get(0).getCourseID(), "Testing ID getter");
            assertEquals("Course 1", db.getCourses().get(0).getCourseName(), "Testing name getter");
            assertEquals(101, db.getCourses().get(1).getCourseID(), "Testing ID getter");
            assertEquals("Course 2", db.getCourses().get(1).getCourseName(), "Testing name getter");
        }

        @Test
        void testCourseLOADEmpty() {
            db.courseLOAD(new JSONArray());

            assertEquals(0, db.getCourses().size());
        }

        @Test
        void testCourseLOADMissingField() {
            jsonCourse = new JSONObject();
            jsonCourse.put("id", 100);
            jsonCourses.put(jsonCourse);
            db.courseLOAD(jsonCourses);

            assertEquals(2, db.getCourses().size(), "Database should refuse to load courses with missing fields");
        }

        @Test
        void testCourseLOADNullField() {
            jsonCourse = new JSONObject();
            jsonCourse.put("id", 100);
            jsonCourse.put("name", JSONObject.NULL);
            jsonCourses.put(jsonCourse);
            db.courseLOAD(jsonCourses);

            assertEquals(2, db.getCourses().size(), "Database should refuse to load courses with null fields");
        }
    }

    @Nested
    class assLOADTests {

        @Test
        void testAssLOAD() {
            db.assignmentLoad(jsonAssignments);

            assertEquals(2, db.getAssignments().size());

            // Checking that the assignments were loaded correctly
            assertEquals(1000, db.getAssignments().get(0).getAssID(), "Testing ID getter");
            assertEquals("Assignment 1", db.getAssignments().get(0).getAssName(), "Testing name getter");
            assertEquals("2023-04-23T06:59:59Z", db.getAssignments().get(0).getAssDate(),
                    "Testing date getter");
            assertEquals(100, db.getAssignments().get(0).getCourseID(), "Testing courseID getter");

            assertEquals(1010, db.getAssignments().get(1).getAssID(), "Testing ID getter");
            assertEquals("Assignment 1", db.getAssignments().get(1).getAssName(), "Testing name getter");
            assertEquals("2023-04-24T06:59:59Z", db.getAssignments().get(1).getAssDate(),
                    "Testing date getter");
            assertEquals(101, db.getAssignments().get(1).getCourseID(), "Testing courseID getter");
        }

        @Test
        void testAssLOADEmpty() {
            db.assignmentLoad(new JSONArray());

            assertEquals(0, db.getAssignments().size());
        }

        @Test
        void testAssLOADMissingField() {
            jsonAssignment = new JSONObject();
            jsonAssignment.put("id", 1000);
            // Missing name field
            jsonAssignments.put(jsonAssignment);
            db.assignmentLoad(jsonAssignments);

            assertEquals(2, db.getAssignments().size(), "Database should refuse to load assignments with missing fields");
        }

        @Test
        void testAssLOADNullField() {
            jsonAssignment = new JSONObject();
            jsonAssignment.put("id", 1000);
            jsonAssignment.put("name", JSONObject.NULL);
            jsonAssignments.put(jsonAssignment);
            db.assignmentLoad(jsonAssignments);

            assertEquals(2, db.getAssignments().size(), "Database should refuse to load assignments with null fields");
        }

    }

}
