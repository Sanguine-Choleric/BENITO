package org.bot;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {
    int CHAR_LIMIT = 2000;
    ArrayList<String> courses;

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = this.convertCourses(courses);
    }

    /**
     * Converts a course to a string
     *
     * @param course a Course object
     * @return String
     */
    private String courseToString(Course course) {
        return course.getCourseName();
    }

    /**
     * Converts a list of courses to a list of strings
     *
     * @param courses a list of Course objects
     * @return ArrayList<String>
     */
    private ArrayList<String> convertCourses(List<Course> courses) {
        ArrayList<String> stringCourses = new ArrayList<>();
        for (Course c : courses) {
            stringCourses.add(courseToString(c));
        }
        return stringCourses;
    }

    /**
     * Compacts a list of strings into a list of Discord optimized messages
     *
     * @param courses a list of Course names
     * @return A compacted list of Course names
     */
    public ArrayList<String> stringsToMessages(List<String> courses) {
        ArrayList<String> messages = new ArrayList<>();
        StringBuilder message = new StringBuilder();

        // Algorithm thought process
        // 1. Append strings until char limit is reached
        // 2. If char limit is reached, add message to messages
        // 3. Repeat until all strings are added
        for (String s : courses) {
            // Check if adding the string will exceed the char limit
            if (message.length() + s.length() + "```".length() + "```".length() >= CHAR_LIMIT) {
                messages.add("```" + message.toString() + "```");
                message = new StringBuilder();
            }

            // Newline check first to avoid trailing newline
            if (message.length() > 0) message.append("\n");

            // Append string
            message.append(s);
        }

        // Add leftovers since it likely won't meet the char limit
        if (message.length() > 0) {
            messages.add("```" + message.toString() + "```");
        }

        return messages;
    }
}
