// package org.bot;

// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.ArgumentMatchers;
// import org.mockito.junit.jupiter.MockitoExtension;

// import net.dv8tion.jda.api.JDA;
// import net.dv8tion.jda.api.JDABuilder;
// import net.dv8tion.jda.api.entities.Message;
// import net.dv8tion.jda.api.entities.User;
// import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
// import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

// @ExtendWith(MockitoExtension.class)
// public class MyListenerTest {
//     JDA jda;

//     @BeforeEach
//     public void setup() {
//         // Setting up real bot
//         jda = JDABuilder.createDefault(API_keys.DiscordKey).build();
//         jda.getEventManager().register(new MyListener());
//     }

//     @Test
//     public void testOnMessageReceivedPing() {
//         // Mock the MessageReceivedEvent

//         // Faking a !ping message
//         // When the mocked event runs runs getContentRaw(), return "!ping"
//         Message message = mock(Message.class);
//         when(message.getContentRaw()).thenReturn("!ping");

//         // Faking a message received event.
//         // Enough of the MessageReceivedEvent interface is mocked for the bot to work
//         MessageReceivedEvent event = mock(MessageReceivedEvent.class);
//         when(event.getMessage()).thenReturn(message);
//         when(event.getAuthor()).thenReturn(mock(User.class));
//         when(event.getChannel()).thenReturn(mock(MessageChannelUnion.class));

//         // Call the onMessageReceived method with the mock event - fake event
//         jda.getEventManager().handle(event);

//         // Verify that the bot responded with "Pong!"
//         verify(event.getChannel()).sendMessage("Pong!");
//     }

//     @Test
//     public void testOnMessageReceivedCourses() {
//         // Mock the MessageReceivedEvent

//         // Faking a !ping message
//         // When the mocked event runs runs getContentRaw(), return "!ping"
//         Message message = mock(Message.class);
//         when(message.getContentRaw()).thenReturn("!courses");

//         // Faking a message received event.
//         // Enough of the MessageReceivedEvent interface is mocked for the bot to work
//         MessageReceivedEvent event = mock(MessageReceivedEvent.class);
//         when(event.getMessage()).thenReturn(message);
//         when(event.getAuthor()).thenReturn(mock(User.class));
//         when(event.getChannel()).thenReturn(mock(MessageChannelUnion.class));

//         // Call the onMessageReceived method with the mock event - fake event
//         jda.getEventManager().handle(event);

//         // Verify that the bot responded with "Pong!"
//         verify(event.getChannel()).sendMessage(ArgumentMatchers.anyString());
//     }
// }