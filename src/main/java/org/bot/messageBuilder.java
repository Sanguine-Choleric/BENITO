package org.bot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// TODO: Undated assignment specific printer

public class messageBuilder {


    /**
     * Converts an arraylist of assignments into pretty strings. Works with Discord's 2000-character limit.
     *
     * @param assignments ArrayList of assignments to convert to a string
     * @return ArrayList of strings, each of which is a message to be sent to Discord
     */
    public static ArrayList<String> convert(ArrayList<Assignment> assignments) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int charCount = 0;

        // I am the way into the city of woe
        for (Assignment assignment : assignments) {

            // I am the way into eternal pain
            String assignmentString = assignmentToString(assignment);

            // I am the way to go among the lost
            if (charCount + assignmentString.length() + 1 > 2000) {

                // Justice caused my high architect to move
                strings.add(sb.toString());
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
            strings.add(sb.toString());
        }

        // Abandon all hope, ye who enter here
        return strings;
    }

    private static String assignmentToString(Assignment assignment) {
        String dueAt = formatDueDate(assignment.getDateFormat());
        String courseName = formatCourseName(assignment.getCourseID());
        String name = formatAssignmentName(assignment.getAssName());
        return String.format("`%s | %s |` %s", dueAt, courseName, name);
    }

    private static String formatDueDate(LocalDateTime dueDate) {
        if (dueDate == null) {
            return "00/00 00:00";
        }
        return dueDate.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
    }

    private static String formatCourseName(int courseId) {
        String courseName;
        for (int i = 0; i < App.db.getCourses_AL().size(); i++) {
            if (App.db.getCourses_AL().get(i).getCourseID() == courseId) {
                courseName = App.db.getCourses_AL().get(i).getCourseName();
                courseName = courseName.split("\\s+")[0];
                return courseName.length() > 7 ? courseName.substring(0, 7) : String.format("%-7s", courseName);
            }
        }
        return "       ";
    }

    private static String formatAssignmentName(String AssignmentName) {
        int MAX_LENGTH = 75;
        String stripped = AssignmentName.replaceAll("[\\\\*_`\\[\\]]", "\\\\$0");
        if (stripped.length() > MAX_LENGTH) {
            return stripped.substring(0, MAX_LENGTH) + "...";
        }
        return stripped;
    }
}