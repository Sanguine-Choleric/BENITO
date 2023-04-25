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

    @BeforeEach
    void setUp() {
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "Course 1");
        jsonCourse.put("description", "This is a course, but this description shouldn't show up in course list.");
        course = new Course(jsonCourse);
        courses.add(course);
    }

    @Test
    void testMessageBuilder() {
        String message = MyListener.messageBuilder(courses, "name");
        assertEquals("Course 1\n", message, "Testing message builder");
    }
}
