package org.bot;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import org.json.JSONObject;

public class Assignment {
    private final int courseID;
    private final int assID;
    private final String assName;
    private final String assDate;

    private final LocalDate assDateFormat;

    // Constructor that takes a JSONObject
    public Assignment(JSONObject assignment) {
        this.courseID = assignment.getInt("course_id");
        this.assID = assignment.getInt("id");
        this.assName = assignment.getString("name");
        this.assDate = assignment.getString("due_at");
        this.assDateFormat = ZonedDateTime.parse(assDate, DateTimeFormatter.ISO_DATE_TIME)
                .withZoneSameInstant(java.time.ZoneId.systemDefault()).toLocalDate();
    }

    // methods
    public int getCourseID() {
        return courseID;
    }

    public int getAssID() {
        return assID;
    }

    public String getAssName() {
        return assName;
    }

    public String getAssDate() {
        return assDate;
    }

    public LocalDate getDateFormat() {
        return assDateFormat;
    }
}