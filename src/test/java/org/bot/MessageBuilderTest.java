package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MessageBuilderTest {
    MessageBuilder messageBuilder;
    Database database;
    JSONArray jsonCourses;
    JSONObject jsonCourse;
    Course course;

    @BeforeEach
    void setUp() {
        database = new Database();
        jsonCourse = new JSONObject();
        jsonCourses = new JSONArray();
        messageBuilder = new MessageBuilder();

    }

    @Test
    void convertCourses() {
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
        System.out.println(actual.get(0).length());
    }

}