package org.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class App {
    static Database db = new Database();

    public static void main(String[] args) throws Exception {
        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).build();
        builder.addEventListener(new MyListener());

        // Pre-populating database
        System.out.println("Populating Courses");
        db.courseLOAD(CanvasGet.getCourses());
        System.out.println(CanvasGet.getCourses());
        System.out.println("Populating Assignments");
        db.assLOAD(CanvasGet.getAllAssignments());
        System.out.println("Bot is ready");
    }
}
