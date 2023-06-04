package org.bot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// TODO: Another redesign

public class MessageBuilder {
    Database database;
    List<String> courses = new ArrayList<>();
    List<String> assignments = new ArrayList<>();

    public MessageBuilder(Database database) {
        this.database = database;
    }

//    public List<String> getAssignments() {
//        assignments = convertAssignments(database.getAssignments());
//        return assignments;
//    }
//
//    public List<String> getCourses() {
//        courses = convertCourses(database.getCourses());
//        return courses;
//    }

    /**
     * Converts an arraylist of assignments into pretty strings. Works with
     * Discord's 2000-character limit.
     *
     * @param assignments ArrayList of assignments to convert to a string
     * @return ArrayList of strings, each of which is a message to be sent to
     * Discord
     */
    public ArrayList<String> convertAssignments(ArrayList<Assignment> assignments) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int charCount = 0;

        // I am the way into the city of woe
        for (Assignment assignment : assignments) {

            // I am the way into eternal pain
            String assignmentString = assignmentToString(assignment);

            // I am the way to go among the lost
            if (charCount + assignmentString.length() + 1 > 1994) {

                // Justice caused my high architect to move
                strings.add("```" + sb + "```");
                sb = new StringBuilder();
                charCount = 0;
            }

            // Divine omnipotence created me
            if (charCount > 0) {

                // The highest wisdom, and the primal love
                sb.append("\n");
                charCount += 1;
            }

            // Before me there were no created things
            sb.append(assignmentString);
            charCount += assignmentString.length();
        }

        // But those that last forever - as do I
        if (sb.length() > 0) {
            strings.add("```" + sb + "```");
        }

        // Abandon all hope, ye who enter here
        return strings;
    }

    public ArrayList<String> convertCourses(ArrayList<Course> courses) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int charcount = 0;
        for (Course course : courses) {
            String courseString = courseToString(course);
            if (charcount > 0) {
                sb.append("\n");
            }

            sb.append(courseString);
            charcount += courseString.length();
        }
        strings.add("```" + sb + "```");

        return strings;
    }

    private String courseToString(Course course) {
        return String.format("%s", course.getCourseName());
    }

    private String assignmentToString(Assignment assignment) {
        String dueAt = formatDueDate(assignment.getDateFormat());
        String courseName = formatCourseName(assignment.getCourseID());
        String name = formatAssignmentName(assignment.getAssName());
        return String.format("%s | %s | %s", dueAt, courseName, name);
    }

    private String formatDueDate(LocalDateTime dueDate) {
        if (dueDate == null) {
            return "           "; // 11 spaces for "HH:MM AM/PM"
        }
        return dueDate.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
    }

    private String formatCourseName(int courseId) {
        String courseName;

        for (Course c : database.getCourses()) {
            if (c.getCourseID() == courseId) {
                courseName = c.getCourseName().split("\\s+")[0]; // Only take the first word of the course name
                return courseName;
            }
        }
        return "      "; // 7 spaces - temporary fix for when course name is not found
    }

    private static String formatAssignmentName(String AssignmentName) {
        int MAX_LENGTH = 50;
        String stripped = AssignmentName.replaceAll("[\\\\*_`\\[\\]]", "\\\\$0");
        if (stripped.length() > MAX_LENGTH) {
            return stripped.substring(0, MAX_LENGTH) + "...";
        }
        return stripped;
    }
}