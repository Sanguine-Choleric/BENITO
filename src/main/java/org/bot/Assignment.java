package org.bot;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class Assignment {
    private final int courseID;
    private final int assID;
    private final String assName;
    private final String assDate;
    private final Boolean has_submitted_submissions;
    private final LocalDateTime assDateFormat;

    // Constructor that takes a JSONObject
    public Assignment(JSONObject assignment) {
        this.courseID = assignment.getInt("course_id");
        this.assID = assignment.getInt("id");
        this.assName = assignment.getString("name");
        this.has_submitted_submissions = assignment.getBoolean("has_submitted_submissions");
        Object value = assignment.get("due_at");
        if (value == JSONObject.NULL){
            assDate = "null";
            assDateFormat = null;
        }
        else
        {
            this.assDate = assignment.getString("due_at");
            this.assDateFormat = ZonedDateTime.parse(assDate, DateTimeFormatter.ISO_DATE_TIME)
                    .withZoneSameInstant(java.time.ZoneId.systemDefault()).toLocalDateTime();
        }
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

    public LocalDateTime getDateFormat() {
        return assDateFormat;
    }

    public boolean getHasBeenSubmited(){return has_submitted_submissions;}

    public String getData(){
        return getAssName()+getAssDate()+getHasBeenSubmited()+getAssID()+getDateFormat()+getCourseID();
    }
}
