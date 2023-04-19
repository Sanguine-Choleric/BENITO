package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the due dates of assignments.
 */
public class DueDateHandler {
    static ZonedDateTime now = ZonedDateTime.now();

    /**
     * Returns a map needed by upcomingDueMessageBuilder. Stores assignment id, name, and due_date as {id: [name, due_at]}.
     * Name is pulled with course_id matching its corresponding id in courses.
     * Due_at is converted to local time.
     *
     * @param source JSONArray of assignments
     * @return Map of upcoming due dates
     */
    protected static Map<String, String[]> upcomingDue(JSONArray source) {
        // Note: This method is probably unmaintainable, improve if possible

        Map<String, String[]> dueDates = new HashMap<>();

        // Iterate through each JSONObject in the JSONArray
        for (int i = 0; i < source.length(); i++) {
            JSONObject object = source.getJSONObject(i);

            // UGLY check to make sure required fields exist and are not null
            if (object.has("id") && object.has("name") && object.has("due_at") && object.has("course_id")
                    && !object.isNull("id") && !object.isNull("name") && !object.isNull("due_at") && !object.isNull("course_id")) {
                // UGLY check to make sure due_at is current or after current date
                if (ZonedDateTime.parse(object.getString("due_at"), DateTimeFormatter.ISO_DATE_TIME).isAfter(now)
                        || ZonedDateTime.parse(object.getString("due_at"), DateTimeFormatter.ISO_DATE_TIME).isEqual(now)) {

                    // Painful building of the dueDates map
                    // Pulling id, name, and due_at fields from the JSONObject
                    // name and due_at are stored in a String array
                    String id = Integer.toString(object.getInt("id"));
                    String name = object.getString("name");

                    String due_at = ZonedDateTime.parse(object.getString("due_at")).toString();

                    // Grabs course name from shared course id field in allAssignments and courses
                    // CSC134 moment - "id" in courses and "course_id" in AllAssignments are the same
                    // This is terrible, but it works
                    String courseId = Integer.toString(object.getInt("course_id"));
                    String courseName = "";
                    for (int j = 0; j < App.courses.length(); j++) {
                        JSONObject course = App.courses.getJSONObject(j);

                        if (Integer.parseInt(courseId) == course.getInt("id")) {
                            courseName = course.getString("name");
                            break;
                        }
                    }
                    String[] dueDate = {due_at, courseName, name};
                    dueDates.put(id, dueDate);
                }
            }
        }
        return dueDates;
    }

    /**
     * Returns a String that is used to send the bulk Discord message for upcoming assignments.
     * The String is formatted as: MM/dd courseName assignmentName and is multiline.
     * Due dates are sorted by closest to current date first.
     *
     * @return String of upcoming due dates
     */
    public static String upcomingDueMessageBuilder() {
        Map<String, String[]> dueDates = DueDateHandler.upcomingDue(App.allAssignments);
        String message = "";

        // Create a list of map entries from the dueDates map
        List<Map.Entry<String, String[]>> sortedEntries = new ArrayList<>(dueDates.entrySet());

        // Sort the entries by due_at date, closest to current date first
        sortedEntries.sort((e1, e2) -> {
            ZonedDateTime date1 = ZonedDateTime.parse(e1.getValue()[0]);
            ZonedDateTime date2 = ZonedDateTime.parse(e2.getValue()[0]);
            return date1.compareTo(date2);
        });

        // Iterate through the sorted entries and appends them to the message String
        for (Map.Entry<String, String[]> entry : sortedEntries) {
            String name = entry.getValue()[2];
            String courseName = entry.getValue()[1];

            ZonedDateTime zonedDateTime = ZonedDateTime.parse(entry.getValue()[0]);
            ZonedDateTime convertedTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
            LocalDateTime localDateTime = convertedTime.toLocalDateTime();
            String dueAt = localDateTime.format(DateTimeFormatter.ofPattern("MM/dd"));

            // Truncate name and courseName if they are too long
            if (name.length() > 50) {
                name = name.substring(0, 50) + "...";
            }

            // Truncate courseName if it is too long
            if (courseName.length() > 6) {
                courseName = courseName.substring(0, 6);
            }

            message = message.concat("`" + dueAt + " " + courseName + "` " + name + "\n");
        }
//        System.out.println(message);
        return message;
    }
}
