package org.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class App {

    public static void main(String[] args) throws Exception {
        Database database = new Database();
        CanvasGet canvasGet = new CanvasGet();

        // Pre-populating database
        System.out.println("Populating Courses");
        database.courseLOAD(canvasGet.getCourses());
        System.out.println("Populating Assignments");
        database.assignmentLoad(canvasGet.getAllAssignments(database));
        System.out.println("Bot is ready");

        JDA builder = JDABuilder.createDefault(API_keys.DiscordKey).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        builder.addEventListener(new MyListener(database));
    }
}
