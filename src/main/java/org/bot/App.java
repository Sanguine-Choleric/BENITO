package org.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.json.JSONArray;

public class App {

    static JSONArray assignments = new JSONArray();

    public static void main(String[] args) throws Exception {
        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).build();
        builder.addEventListener(new MyListener());
        System.out.println("Getting Data");
        DueDateHandler.courses = CanvasGet.getCourses();
        DueDateHandler.allAssignments = CanvasGet.getAllAssignments();
        System.out.println("Bot is ready");
    }
}

