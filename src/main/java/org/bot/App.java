package org.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class App {
    static Database db = new Database();

    public static void main(String[] args) throws Exception {
        // Pre-populating database
        System.out.println("Populating Courses");
        db.courseLOAD(CanvasGet.getCourses());
        System.out.println("Populating Assignments");
        db.assLOAD(CanvasGet.getAllAssignments());
        System.out.println("Bot is ready");

        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        builder.addEventListener(new MyListener());
    }
}
