// package org.bot;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Collections;

// public class DueDateHandler {
//     static LocalDate today = LocalDate.now();

//     public static ArrayList<Assignment> upcomingDue(ArrayList<Assignment> assignments) {
//         ArrayList<Assignment> upcoming = new ArrayList<>();

//         // Filters out overdue assignments
//         for (Assignment a : assignments) {
//             if (a.getDateFormat().isAfter(today) || a.getDateFormat().isEqual(today)) {
//                 upcoming.add(a);
//             }
//         }

//         // Sorts assignments by due date
//         Collections.sort(upcoming, (a1, a2) -> a1.getDateFormat().compareTo(a2.getDateFormat()));

//         return upcoming;
//     }
// }
