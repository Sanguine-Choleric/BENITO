package org.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.JSONArray;

/**
 * Hello world!
 */
public class App {
    static JSONArray courses = new JSONArray();
    static JSONArray assignments = new JSONArray();

    public static void main(String[] args) throws Exception {
        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        builder.addEventListener(new MyListener());
    }
}

