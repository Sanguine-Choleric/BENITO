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
        db = new Database();
        db.assLOAD(jsonAssignments);

        assertNotNull(db.getPastSubmittedAss_AL());
    }

    @Test
    void setPastSubmittedAss_AL() {
        db = new Database();
        db.setPastSubmittedAss_AL(new ArrayList<>());

        assertNotNull(db.getPastSubmittedAss_AL());
    }

    @Test
    void getOverdueAss_AL() {
        db = new Database();
        db.assLOAD(jsonAssignments);

        assertNotNull(db.getOverdueAss_AL());
    }

    @Test
    void setOverdueAss_AL() {
        db = new Database();
        db.setOverdueAss_AL(new ArrayList<>());

        assertNotNull(db.getOverdueAss_AL());
    }

    @Test
    void clear() throws Exception {
        db = new Database();
        db.assLOAD(jsonAssignments);
        db.setOverdueAss_AL(Database.overDue(db.getAllAss_AL()));
        db.setUpcomingAss_AL(Database.upcomingDue(db.getAllAss_AL()));
        db.setPastSubmittedAss_AL(Database.pastSubmitted(db.getAllAss_AL()));

        db.clear();
        assertEquals(0, db.getAllAss_AL().size());
        assertEquals(0, db.getOverdueAss_AL().size());
        assertEquals(0, db.getUpcomingAss_AL().size());
        assertEquals(0, db.getPastSubmittedAss_AL().size());
    }

    // Testing that getters and setters return the right type
    @Test
    void testGetCourses_AL() {
        db = new Database();
        db.courseLOAD(jsonCourses);

        assertNotNull(db.getCourses_AL());
    }

    @Test
    void testGetAllAss_AL() {
        db = new Database();
        db.assLOAD(jsonAssignments);

        assertNotNull(db.getAllAss_AL());
    }

    @Test
    void testGetUpcomingAss_AL() {
        db = new Database();
        db.assLOAD(jsonAssignments);

        assertNotNull(db.getUpcomingAss_AL());
    }

    @Test
    void testSetUpcomingAss_AL() {
        db = new Database();
        db.setUpcomingAss_AL(new ArrayList<>());

        assertNotNull(db.getUpcomingAss_AL());
    }

    @Test
    void getUndatedAss_AL() {
        db = new Database();
        db.assLOAD(jsonAssignments);

        assertNotNull(db.getUndatedAss_AL());
    }

    @Test
    void setUndatedAss_AL() {
        db = new Database();
        db.setUndatedAss_AL(new ArrayList<>());

        assertNotNull(db.getUndatedAss_AL());
    }

    @Nested
    class courseLOADTests {

        @Test
        void testCourseLOAD() {
            db = new Database();
            db.courseLOAD(jsonCourses);

            assertEquals(2, db.getCourses_AL().size());

            // Checking that the courses were loaded correctly
            assertEquals(100, db.getCourses_AL().get(0).getCourseID(), "Testing ID getter");
            assertEquals("Course 1", db.getCourses_AL().get(0).getCourseName(), "Testing name getter");
            assertEquals(101, db.getCourses_AL().get(1).getCourseID(), "Testing ID getter");
            assertEquals("Course 2", db.getCourses_AL().get(1).getCourseName(), "Testing name getter");
        }

        @Test
        void testCourseLOADEmpty() {
            db = new Database();
            db.courseLOAD(new JSONArray());

            assertEquals(0, db.getCourses_AL().size());
        }

        @Test
        void testCourseLOADMissingField() {
            db = new Database();
            jsonCourse = new JSONObject();
            jsonCourse.put("id", 100);
            jsonCourses.put(jsonCourse);
            db.courseLOAD(jsonCourses);

            assertEquals(2, db.getCourses_AL().size(), "Database should refuse to load courses with missing fields");
        }

        @Test
        void testCourseLOADNullField() {
            db = new Database();
            jsonCourse = new JSONObject();
            jsonCourse.put("id", 100);
            jsonCourse.put("name", JSONObject.NULL);
            jsonCourses.put(jsonCourse);
            db.courseLOAD(jsonCourses);

            assertEquals(2, db.getCourses_AL().size(), "Database should refuse to load courses with null fields");
        }
    }

    @Nested
    class assLOADTests {

        @Test
        void testAssLOAD() {
            db = new Database();
            db.assLOAD(jsonAssignments);

            assertEquals(2, db.getAllAss_AL().size());

            // Checking that the assignments were loaded correctly
            assertEquals(1000, db.getAllAss_AL().get(0).getAssID(), "Testing ID getter");
            assertEquals("Assignment 1", db.getAllAss_AL().get(0).getAssName(), "Testing name getter");
            assertEquals("2023-04-23T06:59:59Z", db.getAllAss_AL().get(0).getAssDate(),
                    "Testing date getter");
            assertEquals(100, db.getAllAss_AL().get(0).getCourseID(), "Testing courseID getter");

            assertEquals(1010, db.getAllAss_AL().get(1).getAssID(), "Testing ID getter");
            assertEquals("Assignment 1", db.getAllAss_AL().get(1).getAssName(), "Testing name getter");
            assertEquals("2023-04-24T06:59:59Z", db.getAllAss_AL().get(1).getAssDate(),
                    "Testing date getter");
            assertEquals(101, db.getAllAss_AL().get(1).getCourseID(), "Testing courseID getter");
        }

        @Test
        void testAssLOADEmpty() {
            db = new Database();
            db.assLOAD(new JSONArray());

            assertEquals(0, db.getAllAss_AL().size());
        }

        @Test
        void testAssLOADMissingField() {
            db = new Database();
            jsonAssignment = new JSONObject();
            jsonAssignment.put("id", 1000);
            // Missing name field
            jsonAssignments.put(jsonAssignment);
            db.assLOAD(jsonAssignments);

            assertEquals(2, db.getAllAss_AL().size(), "Database should refuse to load assignments with missing fields");
        }

        @Test
        void testAssLOADNullField() {
            db = new Database();
            jsonAssignment = new JSONObject();
            jsonAssignment.put("id", 1000);
            jsonAssignment.put("name", JSONObject.NULL);
            jsonAssignments.put(jsonAssignment);
            db.assLOAD(jsonAssignments);

            assertEquals(2, db.getAllAss_AL().size(), "Database should refuse to load assignments with null fields");
        }

    }

}
