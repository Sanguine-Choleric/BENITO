package org.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONArray;
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

                if (App.courses.isEmpty()) {
                    channel.sendMessage("Getting classes").queue();
                    try {
                        App.courses = CanvasGet.getCourses();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                channel.sendMessage(messageBuilder(App.courses, "name")).queue();
            }
            case "!hw" -> {
                MessageChannel channel = event.getChannel();

                if (App.assignments.isEmpty()) {
                    channel.sendMessage("Getting Assignments").queue();
                    try {
                        App.assignments = CanvasGet.getHW();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                channel.sendMessage(messageBuilder(App.assignments, "name")).queue();
            }

            // Standard
            case "!allhw" -> {
                MessageChannel channel = event.getChannel();

                if (App.courses.isEmpty()) {
                    channel.sendMessage("Getting Classes").queue();

                    try {
                        App.courses = CanvasGet.getCourses();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    channel.sendMessage("Getting Assignments").queue();

                    App.allAssignments = CanvasGet.getAllAssignments();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // TODO: Need a better way of sending large messages
                // Messaging all of App.allAssignments WILL hit rate limit
                channel.sendMessage(messageBuilder(App.allAssignments, "name")).queue();
            }

            case "!upcoming" -> {
                MessageChannel channel = event.getChannel();

                if (App.courses.isEmpty()) {
                    channel.sendMessage("Getting Classes").queue();
                    try {
                        App.courses = CanvasGet.getCourses();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                if (App.allAssignments.isEmpty()) {
                    channel.sendMessage("Getting Assignments").queue();
                    try {
                        App.allAssignments = CanvasGet.getAllAssignments();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    channel.sendMessage("Getting Upcoming Assignments").queue();
                    String dueToday = DueDateHandler.upcomingDueMessageBuilder();
                    channel.sendMessage(dueToday).queue();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }

    /**
     * Builds a String message from a JSONArray that contains values associated with a given key.
     * Avoids Discord rate limiting by concatenating multiple strings with newlines.
     *
     * @param source The JSONArray containing JSONObjects to extract values from.
     * @param key    The key of the value to extract from each JSONObject.
     */
    protected static String messageBuilder(JSONArray source, String key) {
        String message = "";

        // Iterate through each JSONObject in the JSONArray
        for (int i = 0; i < source.length(); i++) {
            JSONObject object = source.getJSONObject(i);

            // Check if the JSONObject has a value associated with the given key
            // Solves issue of non-existent "name" in some classes/assignments
            if (object.has(key)) {
                message = message.concat(object.getString(key) + "\n");
            }
        }
//        channel.sendMessage(message).queue();
        return message;
    }
}
