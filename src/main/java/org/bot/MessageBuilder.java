package org.bot;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {
    static int CHAR_LIMIT = 2000;

    public MessageBuilder() {
    }

    private static ArrayList<String> convertAssignments(List<Assignment> assignments, List<Course> courses) {
        ArrayList<String> stringAssignments = new ArrayList<>();
        int[] widths = calculateAssignmentColumnWidths(assignments, courses);

        for (Assignment a : assignments) {
            stringAssignments.add(assignmentToString(a, courses, widths));
        }
        return stringAssignments;
    }

    /**
     * Calculates column width for pretty printing assignments
     *
     * @param assignments a list of Assignment objects
     * @return int[] an array of column widths. Index 0 is dueDate, 1 is courseName, 2 is assignmentName
     */
    private static int[] calculateAssignmentColumnWidths(List<Assignment> assignments, List<Course> courses) {
        int[] columnWidths = {0, 0, 0}; // dueDate, courseName, assignmentName

        for (Assignment a : assignments) {
            // Calculate dueDate width
            int dueDateWidth = formatAssignmentDueDate(a, 0).length();
            if (dueDateWidth > columnWidths[0]) {
                columnWidths[0] = dueDateWidth;
            }

            // Calculate courseName width
            int courseNameWidth = formatAssignmentCourseName(a, courses, 0).length();
            if (courseNameWidth > columnWidths[1]) {
                columnWidths[1] = courseNameWidth;
            }

            // Calculate assignmentName width
            int assignmentNameWidth = formatAssignmentName(a, 0).length();
            if (assignmentNameWidth > columnWidths[2]) {
                columnWidths[2] = assignmentNameWidth;
            }
        }
        return columnWidths;
    }

    private static String assignmentToString(Assignment assignment, List<Course> courses, int[] widths) {

        String dueDate = formatAssignmentDueDate(assignment, widths[0]);
        String courseName = formatAssignmentCourseName(assignment, courses, widths[1]);
        String assignmentName = formatAssignmentName(assignment, widths[2]);

        return String.format("%s | %s | %s", dueDate, courseName, assignmentName);
    }

    /**
     * Formats an assignment's due date to "MM/dd hh:mm"
     *
     * @param assignment an Assignment object
     * @param width      the width of the column
     * @return The formatted due date
     */
    private static String formatAssignmentDueDate(Assignment assignment, int width) {
        String dueDate = "";

        if (width == 0) {
            if (assignment.getDueDate() == null) {
                return dueDate;
            }
            dueDate = assignment.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd hh:mm"));
            return dueDate;

        } else {
            if (assignment.getDueDate() == null) {
                return String.format("%-" + width + "s", dueDate); // 11 spaces
            }
            dueDate = assignment.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd hh:mm"));
            return String.format("%-" + width + "s", dueDate);
        }
    }

    /**
     * Formats an assignment's corresponding course name
     *
     * @param assignment an Assignment object
     * @param width      the width of the column
     * @return The formatted course name
     */
    private static String formatAssignmentCourseName(Assignment assignment, List<Course> courses, int width) {
        String courseName = "";
        for (Course c : courses) {
            if (c.getCourseID() == assignment.getCourseID()) {
                courseName = c.getCourseName().split(" ")[0];
                break;
            }
        }

        // No null check unlike date
        if (width == 0) {
            return courseName;
        } else {
            return String.format("%-" + width + "s", courseName);
        }
    }

    /**
     * Formats an assignment's name
     *
     * @param assignment an Assignment object
     * @param width      the width of the column
     * @return The formatted assignment name
     */
    private static String formatAssignmentName(Assignment assignment, int width) {
        String assignmentName = assignment.getAssName();
        // Future processing if decided
        if (assignmentName.length() > 50) {
            assignmentName = assignmentName.substring(0, 50) + "...";
        }

        return assignmentName;
    }

    /**
     * Converts a list of courses to a list of strings
     *
     * @param courses a list of Course objects
     * @return ArrayList<String>
     */
    private static ArrayList<String> convertCourses(List<Course> courses) {
        ArrayList<String> stringCourses = new ArrayList<>();
        if (courses.isEmpty()) {
            stringCourses.add("No courses found");
        } else {
            for (Course c : courses) {
                stringCourses.add(courseToString(c));
            }
        }
        return stringCourses;
    }

    /**
     * Converts a course to a string
     *
     * @param course a Course object
     * @return String
     */
    private static String courseToString(Course course) {
        return course.getCourseName();
    }

    public static List<String> courseMessages(List<Course> c) {
        return stringsToMessages(convertCourses(c));
    }

    public static List<String> assignmentMessages(List<Assignment> a, List<Course> c) {
        ArrayList<String> messages = new ArrayList<>();

        if (a.isEmpty()) {
            messages.add("No assignments found");
        } else {
            messages = convertAssignments(a, c);
        }
        return stringsToMessages(messages);
    }

    /**
     * Compacts a list of strings into a list of Discord optimized messages.
     * Creates a list of max 2000 char strings stored in an array
     *
     * @param row a list of Course names
     * @return A compacted list of Course names
     */
    private static ArrayList<String> stringsToMessages(List<String> row) {
        ArrayList<String> messages = new ArrayList<>();
        StringBuilder message = new StringBuilder();

        // Algorithm thought process
        // 1. Append strings until char limit is reached
        // 2. If char limit is reached, add message to messages
        // 3. Repeat until all strings are added
        for (String s : row) {
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

//    protected void print() {
//        if (!this.courses.isEmpty() && !this.assignments.isEmpty()) {
//            for (String course : courses) {
//                System.out.println(course);
//            }
//
//            for (String assignment : assignments) {
//                System.out.println(assignment);
//            }
//        } else {
//            System.out.println("Course: " + this.courses.isEmpty());
//            System.out.println("Assignment: " + this.assignments.isEmpty());
//        }
//    }
}
