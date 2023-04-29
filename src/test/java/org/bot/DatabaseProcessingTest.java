package org.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseProcessingTest {
    JSONObject assignment;
    ArrayList<Assignment> assignments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        assignment = new JSONObject();
        assignment.put("id", 100);
        assignment.put("name", "Test Assignment");
        assignment.put("due_at", "2021-04-20T23:59:00Z");
        assignment.put("course_id", 12345);
        assignment.put("has_submitted_submissions", true);

        assignments.add(new Assignment(assignment));

        assignment = new JSONObject();
        assignment.put("id", 200);
        assignment.put("name", "Test Assignment 2");
        assignment.put("due_at", "9999-04-21T23:59:00Z");
        assignment.put("course_id", 12345);
        assignment.put("has_submitted_submissions", true);

        assignments.add(new Assignment(assignment));

        assignment = new JSONObject();
        assignment.put("id", 300);
        assignment.put("name", "Test Assignment 3");
        assignment.put("due_at", "9999-04-22T23:59:00Z");
        assignment.put("course_id", 12345);
        assignment.put("has_submitted_submissions", true);

        assignments.add(new Assignment(assignment));
    }

    @Test
    void testUpcomingDue() {
        ArrayList<Assignment> upcoming = Database.upcomingDue(assignments);
        assertEquals(2, upcoming.size(), "Only the two assignments due in the future should be returned");
        assertEquals("Test Assignment 2", upcoming.get(0).getAssName(), "Test Assignment 2 should be first");
        assertEquals("Test Assignment 3", upcoming.get(1).getAssName(), "Test Assignment 3 should be second");
    }

    @Test
    void testOverDue() {

        assignment = new JSONObject();
        assignment.put("id", 100);
        assignment.put("name", "Test Overdue Assignment");
        assignment.put("due_at", "2021-04-20T23:59:00Z");
        assignment.put("course_id", 12345);
        assignment.put("has_submitted_submissions", false);

        assignments.add(new Assignment(assignment));

        ArrayList<Assignment> overdue = Database.overDue(assignments);
        assertEquals(1, overdue.size(), "Only the one assignment in past and unsubmitted should appear");
        assertEquals("Test Overdue Assignment", overdue.get(0).getAssName(), "Test Overdue Assignment should be first");
    }

    @Test
    void testPastSubmitted() {
        assignment = new JSONObject();
        assignment.put("id", 100);
        assignment.put("name", "Test Past Submitted Assignment");
        assignment.put("due_at", "2021-04-19T23:59:00Z");
        assignment.put("course_id", 12345);
        assignment.put("has_submitted_submissions", true);

        assignments.add(new Assignment(assignment));

        ArrayList<Assignment> pastSubmitted = Database.pastSubmitted(assignments);
        assertEquals(2, pastSubmitted.size(), "2 should appear, the one in this test and the one in setup");
        assertEquals("Test Assignment", pastSubmitted.get(0).getAssName(), "Test Assignment should be first");
        assertEquals("Test Past Submitted Assignment", pastSubmitted.get(1).getAssName(),
                "Test Past Submitted Assignment should be second");
    }
}
