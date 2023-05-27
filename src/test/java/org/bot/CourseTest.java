package org.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourseTest {
    JSONObject jsonCourse = new JSONObject();
    Course course;

    @BeforeEach
    void setUp() {
        jsonCourse.put("id", 100);
        jsonCourse.put("name", "Course 1");
        jsonCourse.put("description", "This is a course, but this description shouldn't show up in course list.");
        course = new Course(jsonCourse);
    }

    @Test
    void testGetCourseID() {
        assertEquals(course.getCourseID(), jsonCourse.getInt("id"), "Testing ID getter");

    }

    @Test
    void testGetCourseName() {
        assertEquals(course.getCourseName(), jsonCourse.getString("name"), "Testing name getter");
    }
}
