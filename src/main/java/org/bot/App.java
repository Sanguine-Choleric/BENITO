package org.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.json.JSONArray;

public class App {
    static JSONArray courses = new JSONArray();
    static JSONArray assignments = new JSONArray();

    static JSONArray allAssignments = new JSONArray();

    public static void main(String[] args) {
        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).build();
        builder.addEventListener(new MyListener());
    }
}

