package org.bot;

public class API_keys {
//    public static String CanvasKey = "11299~vXmuQUkiy8JRXtvLS2Z3zv6rKUib7lptGAEgUFAWFWmdgyViq0GtvrpYVVyuVSpW";
//    public static String DiscordKey = "MTA5MTIyODg5MjQyMzAxMjM4Mg.GZCljR.qCPdoIwm08ut5Yj60-N3KX3SJjRB1cTjKeHx88";
    public static String CanvasKey = System.getenv("CANVAS_KEY");
    public static String DiscordKey = System.getenv("DISCORD_KEY");
}
