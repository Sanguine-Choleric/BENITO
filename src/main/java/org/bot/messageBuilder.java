package org.bot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// TODO: Undated assignment specific printer

public class messageBuilder {

    public static ArrayList<String> convert(ArrayList<Assignment> assignments) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int charCount = 0;
        for (Assignment assignment : assignments) {
            String assignmentString = assignmentToString(assignment);
            if (charCount + assignmentString.length() + 1 > 2000) {
                // start a new string
                strings.add(sb.toString());
                sb = new StringBuilder();
                charCount = 0;
            }
            if (charCount > 0) {
                sb.append("\n");
                charCount += 1;
            }
            sb.append(assignmentString);
            charCount += assignmentString.length();
        }
        if (sb.length() > 0) {
            strings.add(sb.toString());
        }
        return strings;
    }

    private static String assignmentToString(Assignment assignment) {
        String dueAt = formatDueDate(assignment.getDateFormat());
        String courseName = formatCourseName(assignment.getCourseID());
        String name = formatAssignmentName(assignment.getAssName());
        return String.format("`%s | %s |` %s", dueAt, courseName, name);
    }

    private static String formatDueDate(LocalDateTime dueDate) {
        return dueDate.format(DateTimeFormatter.ofPattern("MM/dd HH:mm"));
    }

    private static String formatCourseName(int courseId) {
        String courseName;
        for (int i = 0; i < App.db.getCourses_AL().size(); i++) {
            if (App.db.getCourses_AL().get(i).getCourseID() == courseId) {
                courseName = App.db.getCourses_AL().get(i).getCourseName();
                return courseName.split("\\s+")[0];
            }
        }
        return "";
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