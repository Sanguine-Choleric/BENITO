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
                        App.db.courseLOAD(CanvasGet.getCourses());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                channel.sendMessage(messageBuilder(App.db.getCourses_AL(), "name")).queue();
            }
            // Now grabs all assignments
            // TODO: BUG - 2000 char limit per message. This command regularily goes over limit
            case "!hw" -> {
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

            case "!upcoming" -> {
                MessageChannel channel = event.getChannel();
                if (App.db.getCourses_AL().isEmpty()) {
                    channel.sendMessage("Getting Classes").queue();

                    try {
                        App.db.courseLOAD(CanvasGet.getCourses());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (App.db.getAllAss_AL().isEmpty()) {
                    channel.sendMessage("Getting All Assignments").queue();

                    try {
                        App.db.assLOAD(CanvasGet.getAllAssignments());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                channel.sendMessage("Getting Upcoming Assignments").queue();
                try {
                    App.db.setUpcomingAss_AL(DueDateHandler.upcomingDue(App.db.getAllAss_AL()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                channel.sendMessage(messageBuilder(App.db.getUpcomingAss_AL(), "name")).queue();
            }
        }
    }

    // TEMP messagbuilder for demo only
    // TODO: UI/UX guys rewrite this; needs to solve 2000char limit on messages
    protected static String messageBuilder(ArrayList<?> source, String key) {
        String message = "";

        // Java hackery to take Assignment and Course Objects
        for (int i = 0; i < source.size(); i++) {
            Object object = source.get(i);

            if (key == "name") {
                if (object instanceof Course) {
                    Course course = (Course) object;
                    message = message.concat(course.getCourseName() + "\n");
                } else if (object instanceof Assignment) {
                    Assignment assignment = (Assignment) object;
                    message = message.concat(assignment.getAssName() + "\n");
                }
            } else if (key == "id") {
                if (object instanceof Course) {
                    Course course = (Course) object;
                    message = message.concat(course.getCourseID() + "\n");
                } else if (object instanceof Assignment) {
                    Assignment assignment = (Assignment) object;
                    message = message.concat(assignment.getAssID() + "\n");
                }
            }
            
        }
        return message;
    }
}
