package org.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;

public class App {
    static JSONArray courses = new JSONArray();
    static JSONArray assignments = new JSONArray();
    static JSONArray allAssignments = new JSONArray();

    public static void main(String[] args) throws Exception {
        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).build();
        builder.addEventListener(new MyListener());

        // Populating storage for faster response times
        System.out.println("Getting Data");
        courses = CanvasGet.getCourses();
        allAssignments = CanvasGet.getAllAssignments();
        System.out.println("Bot is ready");
        writeJSON(courses);
    }

    public static void writeJSON(JSONArray assignments) {
        try {
            FileWriter file = new FileWriter("output.json");
            file.write(assignments.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

