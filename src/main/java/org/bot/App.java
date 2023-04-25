package org.bot;

import org.json.JSONArray;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class App {
    // static JSONArray allCourse_JSON = new JSONArray();
    // static JSONArray assignments = new JSONArray();

    static Database db = new Database();

    public static void main(String[] args) throws Exception {
        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).build();
        builder.addEventListener(new MyListener());
    }
}

