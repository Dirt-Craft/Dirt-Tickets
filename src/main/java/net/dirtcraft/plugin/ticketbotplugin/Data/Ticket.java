package net.dirtcraft.plugin.ticketbotplugin.Data;

public class Ticket {

    private final int id;
    private final boolean open;
    private final String message;
    private final String username;
    private final String server;
    private final String channel;
    private final String level;

    public Ticket(int id, boolean open, String message, String username, String server, String channel, String level) {
        this.id = id;
        this.open = open;
        this.message = message;
        this.username = username;
        this.server = server;
        this.channel = channel;
        this.level = level;
    }

    public int getId() { return id; }
    public boolean isOpen() { return open; }
    public String getMessage() { return message; }
    public String getUsername() { return username; }

    public String getServer() { return server; }

    public String getChannel() { return channel; }
    public String getLevel() { return level; }

}
