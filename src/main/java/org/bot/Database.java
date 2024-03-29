package org.bot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Stores all info retrieved from the Canvas API.
 * JSONObjects are stored as custom objects in ArrayLists.
 * ArrayLists are populated by various methods in and outside of this class.
 */
public class Database {
    static LocalDateTime today = LocalDateTime.now();
    public ArrayList<Course> courses = new ArrayList<>();
    private List<Assignment> allAssignments = new ArrayList<>();
    private List<Assignment> upcomingAssignments = new ArrayList<>();
    private List<Assignment> overdueAssignments = new ArrayList<>();
    private List<Assignment> submittedAssignments = new ArrayList<>();
    private List<Assignment> undatedAssignments = new ArrayList<>();

    public List<Assignment> getSubmitted() {
        return submittedAssignments;
    }

    public void setSubmittedAssignments() {
        this.submittedAssignments = submittedAssignments(allAssignments);
    }

    public List<Assignment> getUndated() {
        return undatedAssignments;
    }

    public void setUndatedAssignments() {
        this.undatedAssignments = undatedAssignments(allAssignments);
    }


    public List<Assignment> getOverdue() {
        return overdueAssignments;
    }

    public void setOverdueAssignments() {
        this.overdueAssignments = overdueAssignments(allAssignments);
    }

    public void clear() {
        courses.clear();
        allAssignments.clear();
        upcomingAssignments.clear();
        overdueAssignments.clear();
        submittedAssignments.clear();
        undatedAssignments.clear();
    }

    public List<Assignment> getUpcoming() {
        return upcomingAssignments;
    }

    // Setter for populating upcomingAss_AL with upcoming assignments
    public void setUpcomingAssignments() {
        this.upcomingAssignments = upcomingAssignments(allAssignments);
    }

    // Getter for courses_AL; returns an ArrayList of Course objects
    public ArrayList<Course> getCourses() {
        return courses;
    }

    // Getter for allAss_AL; returns an ArrayList of Assignment objects
    public List<Assignment> getAssignments() {
        return allAssignments;
    }

    /**
     * Populates coursesAL. Converts an input JSONArray into an ArrayList of Course
     * objects
     *
     * @param courses the JSONArray containing Course objects
     */
    public void courseLoad(JSONArray courses) {
        for (int i = 0; i < courses.length(); i++) {
            if (hasNonNullValues(courses.getJSONObject(i), "name", "id")) {
                this.courses.add(new Course(courses.getJSONObject(i)));
            } else {
                System.out.println("Course " + i + " is missing a field");
                System.out.println(courses.getJSONObject(i));//This is for debugging Purposes
            }
        }
    }

    /**
     * upcomingDue method sorts through allASS_AL and only populates based on a
     * certain set of requirements.
     * REQUIREMENTS for Assignment Object to enter upcomingAss_AL
     * 1.must not be current or past time compared to local time
     *
     * @param allAssignments, the arraylist of all assignments
     * @return returns an arraylist to populate the upcoming assignment category
     */
    public ArrayList<Assignment> upcomingAssignments(List<Assignment> allAssignments) {
        ArrayList<Assignment> upcoming = new ArrayList<>();
        today = LocalDateTime.now();

        // Filters out overdue assignments
        for (Assignment a : allAssignments) {
            if (!Objects.equals(a.getAssDate(), "null")) {
                if (a.getDueDate().isAfter(today) || a.getDueDate().isEqual(today)) {
                    upcoming.add(a);
                }
            }
        }

        // Sorts assignments by due date, closest to today first
        upcoming.sort(Comparator.comparing(Assignment::getDueDate));
        return upcoming;
    }

    /**
     * Filters out overdue assignments from the provided list of assignments and
     * returns a sorted list of overdue assignments. Sorted by due date, closest to
     * today first
     *
     * @param allAssignments the list of all assignments
     * @return a sorted list of overdue assignments
     */
    public ArrayList<Assignment> overdueAssignments(List<Assignment> allAssignments) {
        ArrayList<Assignment> overdue = new ArrayList<>();
        today = LocalDateTime.now();

        // Filters out upcoming assignments
        for (Assignment a : allAssignments) {
            if (!Objects.equals(a.getAssDate(), "null")) {
                if (a.getDueDate().isBefore(today) && !a.getHasBeenSubmitted()) {
                    overdue.add(a);
                }
            }
        }

        // Sorts assignments by due date, closest to today first
        overdue.sort((a1, a2) -> a2.getDueDate().compareTo(a1.getDueDate()));

        return overdue;
    }

    public ArrayList<Assignment> undatedAssignments(List<Assignment> allAssignments) {
        ArrayList<Assignment> undated = new ArrayList<>();
        for (Assignment a : allAssignments) {
            if (a.getDueDate() == null) {
                undated.add(a);
            }
        }

        return undated;
    }

    public ArrayList<Assignment> submittedAssignments(List<Assignment> allAssignments) {
        ArrayList<Assignment> pastSubmitted = new ArrayList<>();
        today = LocalDateTime.now();

        // Filters out past/submitted assignments
        for (Assignment a : allAssignments) {
            if (!Objects.equals(a.getAssDate(), "null")) {
                if ((a.getDueDate().isBefore(today) || a.getDueDate().isEqual(today)) && a.getHasBeenSubmitted()) {
                    pastSubmitted.add(a);
                }
            }
        }

        // Sorts assignments by due date, closest to today first
        pastSubmitted.sort((a1, a2) -> a2.getDueDate().compareTo(a1.getDueDate()));

        return pastSubmitted;
    }

    /**
     * Populates allAss_AL Converts an input JSONArray into an ArrayList of
     * Assignment objects
     *
     * @param assignments the JSONArray containing Course objects
     */
    public void assignmentLoad(JSONArray assignments) {
        for (int i = 0; i < assignments.length(); i++) {
            //ONLY VALUES THAT ALLOWED TO BE NULL IS due_at
            //Adding due_at can have a null value which means its undated
            if (hasNonNullValues(assignments.getJSONObject(i), "id", "name", "course_id",
                    "has_submitted_submissions")) {
                allAssignments.add(new Assignment(assignments.getJSONObject(i)));
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
     * and false otherwise.
     */
    private boolean hasNonNullValues(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (!obj.has(key) || obj.isNull(key)) {
                return false;
            }
        }
        return true;
    }
}
