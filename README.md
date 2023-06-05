# BENITO

A bot that lets you keep track of Canvas from Discord. Development ongoing. Written in Java using JDA.
* Show currently enrolled courses
* Show assignments for those courses
  * Filter by upcoming, overdue, submitted, and undated assignments
* DM the bot or add it to a server

## Requirements
* Java JDK 17
* Maven to build

Build with `mvn package` to create a jar executable.

## Usage
You need API keys for Discord and Canvas. They need to be set as env vars to use. Set as `DISCORD_KEY` and `CANVAS_KEY`

```Java
package org.bot;

public class API_keys {
    static final String DiscordKey = System.getenv("DISCORD_KEY");
    static final String CanvasKey = System.getenv("CANVAS_KEY");
}
```
