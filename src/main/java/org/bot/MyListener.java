package org.bot;

import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MyListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot())
            return;

        Message message = event.getMessage();
        String content = message.getContentRaw();

        switch (content) {
            case "!ping" -> {
                MessageChannel channel = event.getChannel();
                channel.sendMessage("Pong!").queue();
            }
            
            case "!courses" -> {
                MessageChannel channel = event.getChannel();

                if (App.db.getCourses_AL().isEmpty()) {
                    channel.sendMessage("Getting classes").queue();
                    try {
                        System.out.println("Connecting for classes");
                        App.db.courseLOAD(CanvasGet.getCourses());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                channel.sendMessage(messageBuilder(App.db.getCourses_AL(), "name")).queue();
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
                channel.sendMessage(messageBuilder(App.db.getAllAss_AL(), "name")).queue();
            }

            // Standard
            case "!allhw" -> {
                MessageChannel channel = event.getChannel();

                if (App.db.getCourses_AL().isEmpty()) {
                    channel.sendMessage("Getting Classes").queue();

                    try {
                        App.db.courseLOAD(CanvasGet.getCourses());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                channel.sendMessage("Getting All Assignments").queue();
                try {
                    App.db.assLOAD(CanvasGet.getAllAssignments());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                channel.sendMessage(messageBuilder(App.db.getAllAss_AL(), "name")).queue();
            }
        }
    }

    protected static String messageBuilder(ArrayList<?> source, String key) {
        String message = "";

        for (int i = 0; i < source.size(); i++) {
            Object object = source.get(i);

            if (object instanceof Course) {
                Course course = (Course) object;
                message = message.concat(course.getCourseName() + "\n");
            } else if (object instanceof Assignment) {
                Assignment assignment = (Assignment) object;
                message = message.concat(assignment.getAssName() + "\n");
            }
        }

        return message;
    }
}
