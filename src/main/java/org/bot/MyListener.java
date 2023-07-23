package org.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

// TODO: Better integration with MessageBuilder

public class MyListener extends ListenerAdapter {
    Database database;
    CanvasGet canvasGet;

    public MyListener(Database database) {
        this.database = database;
        this.canvasGet = new CanvasGet();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();

        switch (content) {
            case "!ping" -> channel.sendMessage("Pong!").queue();

            default -> {
                ArrayList<String[]> commandList = new ArrayList<>();
                commandList.add(new String[]{"!ping", "Pong!"});
                commandList.add(new String[]{"!help", "This message"});
                commandList.add(new String[]{"!update", "Update the database"});
                commandList.add(new String[]{"!courses", "List all courses"});
                commandList.add(new String[]{"!hw", "List all assignments"});
                commandList.add(new String[]{"!upcoming", "List all upcoming assignments"});
                commandList.add(new String[]{"!overdue", "List all overdue assignments"});
                commandList.add(new String[]{"!submitted", "List all past submitted assignments"});
                commandList.add(new String[]{"!undated", "List all undated assignments"});

                int maxCommandLength = 0;
                for (String[] command : commandList) {
                    maxCommandLength = Math.max(maxCommandLength, command[0].length());
                }

                StringBuilder formattedCommands = new StringBuilder();
                for (String[] command : commandList) {
                    String formattedCommand = String.format("%-" + maxCommandLength + "s | %s%n", command[0], command[1]);
                    formattedCommands.append(formattedCommand);
                }

                String outputString = String.format("```\n%s\n```", formattedCommands);
                channel.sendMessage(outputString).queue();
            }

            case "!update" -> {
                try {
                    channel.sendMessage("Updating Courses").queue();
                    database.courseLoad(canvasGet.getCourses());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    channel.sendMessage("Updating Assignments").queue();
                    database.assignmentLoad(canvasGet.getAllAssignments(database.getCourses()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                channel.sendMessage("Database Updated").queue();
            }

            // Temp; UI guys redo this
            case "!courses" -> {
                channel.sendMessage("Getting Courses").queue();
                loadCourses();

                for (String s : MessageBuilder.courseMessages(database.getCourses())) {
                    channel.sendMessage(s).queue();
                }

            }

            // Temp; UI guys redo this
            case "!hw" -> {
                loadCourses();

                channel.sendMessage("Getting All Assignments").queue();
                loadAssignments();

                for (String s : MessageBuilder.assignmentMessages(database.getAssignments(), database.getCourses())) {
                    channel.sendMessage(s).queue();
                }
            }

            // Temp; UI guys redo this
            case "!upcoming" -> {
                loadCourses();
                loadAssignments();

                channel.sendMessage("Getting Upcoming Assignments").queue();
                try {
                    database.setUpcomingAssignments();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                for (String s : MessageBuilder.assignmentMessages(database.getUpcoming(), database.getCourses())) {
                    channel.sendMessage(s).queue();
                }
            }

            // Temp; UI guys redo this
            case "!overdue" -> {
                loadCourses();
                loadAssignments();

                channel.sendMessage("Getting Overdue Assignments").queue();
                try {
                    database.setOverdueAssignments();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                for (String s : MessageBuilder.assignmentMessages(database.getOverdue(), database.getCourses())) {
                    channel.sendMessage(s).queue();
                }
            }
            case "!undated" -> {
                loadCourses();
                loadAssignments();

                channel.sendMessage("Getting Undated Assignments").queue();
                try {
                    database.setUndatedAssignments();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                for (String s : MessageBuilder.assignmentMessages(database.getUndated(), database.getCourses())) {
                    channel.sendMessage(s).queue();
                }
            }

            // Temp; UI guys redo this
            case "!submitted" -> {
                loadCourses();
                loadAssignments();

                channel.sendMessage("Getting Submitted Assignments").queue();
                try {
                    database.setSubmittedAssignments();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                for (String s : MessageBuilder.assignmentMessages(database.getSubmitted(), database.getCourses())) {
                    channel.sendMessage(s).queue();
                }
            }
        }
    }

    private void loadAssignments() {
        if (database.getAssignments().isEmpty()) {
            try {
                database.assignmentLoad(canvasGet.getAllAssignments(database.getCourses()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadCourses() {
        if (database.getCourses().isEmpty()) {
            try {
                database.courseLoad(canvasGet.getCourses());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
