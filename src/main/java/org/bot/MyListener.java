package org.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

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

            case "!help" -> {
                MessageChannel channel = event.getChannel();

                ArrayList<String[]> commandList = new ArrayList<>();
                commandList.add(new String[]{"!ping", "Pong!"});
                commandList.add(new String[]{"!help", "This message"});
                commandList.add(new String[]{"!courses", "List all courses"});
                commandList.add(new String[]{"!hw", "List all assignments"});
                commandList.add(new String[]{"!upcoming", "List all upcoming assignments"});
                commandList.add(new String[]{"!overdue", "List all overdue assignments"});
                commandList.add(new String[]{"!past", "List all past submitted assignments"});
                commandList.add(new String[]{"!undated", "List all undated assignments"});

                int maxCommandLength = 0;
                for (String[] command : commandList) {
                    maxCommandLength = Math.max(maxCommandLength, command[0].length());
                }

                StringBuilder formattedCommands = new StringBuilder();
                for (String[] command : commandList) {
                    String formattedCommand = String.format("%-" + maxCommandLength + "s - %s%n", command[0], command[1]);
                    formattedCommands.append(formattedCommand);
                }

                String outputString = String.format("```\n%s\n```", formattedCommands.toString());
                channel.sendMessage(outputString).queue();
            }

            // Temp; UI guys redo this
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

//                // channel.sendMessage(messageBuilder(App.db.getCourses_AL(), "name")).queue();
//                for (int i = 0; i < App.db.getCourses_AL().size(); i++) {
//                    channel.sendMessage(App.db.getCourses_AL().get(i).getCourseName()).queue();
//                }

                ArrayList<String> allCourses = messageBuilder.convert(App.db.getCourses_AL());
                for (String s : allCourses) {
                    channel.sendMessage(s).queue();
                }

            }

            // Temp; UI guys redo this
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

                ArrayList<String> allHw = messageBuilder.convert(App.db.getAllAss_AL());
                for (String s : allHw) {
                    channel.sendMessage(s).queue();
                }
            }

            // Temp; UI guys redo this
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
                    App.db.setUpcomingAss_AL(Database.upcomingDue(App.db.getAllAss_AL()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                ArrayList<String> upcoming = messageBuilder.convert(App.db.getUpcomingAss_AL());
                for (String s : upcoming) {
                    channel.sendMessage(s).queue();
                }

            }

            // Temp; UI guys redo this
            case "!overdue" -> {
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

                channel.sendMessage("Getting Overdue Assignments").queue();
                try {
                    App.db.setOverdueAss_AL(Database.overDue(App.db.getAllAss_AL()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                ArrayList<String> overdue = messageBuilder.convert(App.db.getOverdueAss_AL());
                for (String s : overdue) {
                    channel.sendMessage(s).queue();
                }
            }
            case "!undated" -> {
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

                channel.sendMessage("Getting Undated Assignments").queue();
                try {
                    App.db.setUndatedAss_AL(Database.undatedAssignments(App.db.getAllAss_AL()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                ArrayList<String> undated = messageBuilder.convert(App.db.getUndatedAss_AL());
                for (String s : undated) {
                    channel.sendMessage(s).queue();
                }
            }

            // Temp; UI guys redo this
            case "!submitted" -> {
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

                channel.sendMessage("Getting Submitted Assignments").queue();
                try {
                    App.db.setPastSubmittedAss_AL(Database.pastSubmitted(App.db.getAllAss_AL()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                ArrayList<String> past = messageBuilder.convert(App.db.getPastSubmittedAss_AL());
                for (String s : past) {
                    channel.sendMessage(s).queue();
                }
            }
        }
    }
}
