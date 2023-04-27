package org.bot;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Stores all info retrieved from the Canvas API.
 * JSONObjects are stored as custom objects in ArrayLists.
 * ArrayLists are populated by various methods in and outside of this class.
 */
public class Database {
    // private JSONArray allCourse_JSON = new JSONArray();
    // private JSONArray allAss_JSON = new JSONArray();
    private ArrayList<Course> courses_AL = new ArrayList<>();
    private ArrayList<Assignment> allAss_AL = new ArrayList<>();
    private ArrayList<Assignment> upcomingAss_AL = new ArrayList<>();

    public void clear() {
        courses_AL.clear();
        allAss_AL.clear();
        upcomingAss_AL.clear();
    }

    public ArrayList<Assignment> getUpcomingAss_AL() {
        return upcomingAss_AL;
    }

    // Setter for populating upcomingAss_AL with upcoming assignments
    public void setUpcomingAss_AL(ArrayList<Assignment> upcomingAss_AL) {
        this.upcomingAss_AL = upcomingAss_AL;
    }

    // Getter for courses_AL; returns an ArrayList of Course objects
    public ArrayList<Course> getCourses_AL() {
        return courses_AL;
    }

    // Getter for allAss_AL; returns an ArrayList of Assignment objects
    public ArrayList<Assignment> getAllAss_AL() {
        return allAss_AL;
    }

    /**
     * Populates coursesAL. Converts an input JSONArray into an ArrayList of Course
     * objects
     *
     * @param courses the JSONArray containing Course objects
     * @throws Exception if there is an error while loading the Course objects
     */
    public void courseLOAD(JSONArray courses) throws Exception {
        for (int i = 0; i < courses.length(); i++) {
            if (hasNonNullValues(courses.getJSONObject(i), "name", "id")) {
                courses_AL.add(new Course(courses.getJSONObject(i)));
            } else {
                System.out.println("Course " + i + " is missing a field");
            }
        }
    }

    /**
     * Populates allAss_AL Converts an input JSONArray into an ArrayList of
     * Assignment objects
     *
     * @param courses the JSONArray containing Course objects
     * @throws Exception if there is an error while loading the Course objects
     */
    public void assLOAD(JSONArray assignments) throws Exception {
        for (int i = 0; i < assignments.length(); i++) {
            if (hasNonNullValues(assignments.getJSONObject(i), "name", "due_at", "course_id")) {
                allAss_AL.add(new Assignment(assignments.getJSONObject(i)));
            } else {
                System.out.println("Assignment " + i + " is missing a field");
            }
        }
    }

    /**
     * Returns true if the given JSONObject has non-null values for all the given
     * keys,
     * and false otherwise.
     *
     * @param obj  the JSONObject to check
     * @param keys the keys to check for non-null values
     * @return true if the JSONObject has non-null values for all the given keys,
     *         and false otherwise.
     */
    private static boolean hasNonNullValues(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (!obj.has(key) || obj.isNull(key)) {
                return false;
            }
        }
        return true;
    }
}
