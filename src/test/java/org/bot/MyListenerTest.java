package org.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MyListenerTest {
    JSONObject jsonCourse = new JSONObject();
    Course course;
    ArrayList<Course> courses = new ArrayList<Course>();
    ArrayList<Assignment> assignments = new ArrayList<Assignment>();

    @Test
    void testMessageBuilderCourse() {
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "Course 1");
        jsonCourse.put("description", "This is a course, but this description shouldn't show up in course list.");
        course = new Course(jsonCourse);
        courses.add(course);
        String message = MyListener.messageBuilder(courses, "name");
        assertEquals("Course 1\n", message, "Testing message builder");
    }

    @Test
    void testMessageBuilderAssignment() {
        JSONObject jsonAssignment = new JSONObject();
        jsonAssignment.put("id", 100);
        jsonAssignment.put("name", "Assignment 1");
        jsonAssignment.put("due_at", "2021-04-20T23:59:00Z");
        jsonAssignment.put("course_id", 100);
        Assignment assignment = new Assignment(jsonAssignment);
        assignments.add(assignment);
        String message = MyListener.messageBuilder(assignments, "name");
        assertEquals("Assignment 1\n", message, "Testing message builder");
    }
}
