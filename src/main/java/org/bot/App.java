package org.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.json.JSONArray;

/**
 * Hello world!
 */
public class App {
    static JSONArray courses = new JSONArray();
    static JSONArray assignments = new JSONArray();

    public static void main(String[] args) throws Exception {
        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).build();
        builder.addEventListener(new MyListener());
    }
}

