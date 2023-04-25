package org.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;

public class MyListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        // getContentRaw() is an atomic getter
        // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)

        switch (content) {
            case "!ping" -> {
                MessageChannel channel = event.getChannel();
                channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)

            }
            case "!classes" -> {
                MessageChannel channel = event.getChannel();

                if (App.db.getCourses_AL().isEmpty()) {
                    channel.sendMessage("Getting classes").queue();
                    try {
                        System.out.println("Connecting for classes");
                        // App.allCourse_JSON = CanvasGet.getCourses(); //changed so this command can still work
                        App.db.courseLOAD(CanvasGet.getCourses());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                for (int i = 0; i < App.db.getCourses_AL().size(); i++) {
                    // JSONObject course = App.allCourse_JSON.getJSONObject(i);

                    // if (course.getInt("id") > 100000) {
                    //     channel.sendMessage(course.getString("name")).queue();
                    // }
                    Course course = App.db.getCourses_AL().get(i);
                    if (course.getCourseID() > 100000) {
                        channel.sendMessage(course.getCourseName()).queue();
                    }
                
                }
            }
            // Doesn't grab all hw
            case "!hw" -> {
                MessageChannel channel = event.getChannel();

                if (App.db.getAllAss_AL().isEmpty()) {
                    channel.sendMessage("Getting Assignments").queue();
                    try {
                        System.out.println("Connecting for hw");
                        // App.assignments = CanvasGet.getHW();
                        App.db.assLOAD(CanvasGet.getHW());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                for (int i = 0; i < App.db.getAllAss_AL().size(); i++) {
                    // JSONObject assignment = App.assignments.getJSONObject(i);
                    // channel.sendMessage(assignment.getString("name")).queue();
                    Assignment assignment = App.db.getAllAss_AL().get(i);
                    channel.sendMessage(assignment.getAssName()).queue();
                }
            }
        }
    }
}
