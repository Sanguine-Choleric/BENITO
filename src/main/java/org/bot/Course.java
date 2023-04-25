package org.bot;

import org.json.JSONObject;

public class Course {
    private final int courseID;
    private final String courseName;

    // Constructor that takes a JSONObject
    public Course(JSONObject course) {
        this.courseID = course.getInt("id");
        this.courseName = course.getString("name");
    }

    // Getter for id
    public int getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }
}
