package net.dirtcraft.plugin.ticketbotplugin.Database;

import net.dirtcraft.plugin.dirtdatabaselib.DirtDatabaseLib;
import net.dirtcraft.plugin.ticketbotplugin.TicketBotPlugin;
import net.dirtcraft.plugin.ticketbotplugin.Data.Ticket;
import net.dirtcraft.plugin.ticketbotplugin.Util.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Storage {

    private final TicketBotPlugin main;

    public Storage(TicketBotPlugin main) {
        this.main = main;
    }

    public ArrayList<Ticket> listTickets(String username) {
        ArrayList<Ticket> ticket = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE lower(username) = '" + username.toLowerCase() + "'");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ticket.add(new Ticket(
                                rs.getInt("id"),
                                rs.getBoolean("open"),
                                rs.getString("message"),
                                rs.getString("username"),
                                rs.getString("server"),
                                rs.getString("channel"),
                                rs.getString("level")));

            }

            rs.close();
            ps.close();
            connection.close();

            return ticket;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return ticket;
        }
    }

    public int getOpenTickets(String username) {
        int openTickets = 0;

        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE lower(username) = '" + username.toLowerCase() + "'");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getBoolean("open")) openTickets++;
            }

            rs.close();
            ps.close();
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return openTickets;
    }

    public void createTicket(String message, String username) {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO tickets (message, username, server) VALUES ('" + message + "', '" + username + "', '" + Utility.getServerCode() + "')");

            ps.execute();

            ps.close();
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean hasOpenTicket(String username) {
        boolean result;

        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE lower(username) = '" + username + "'");
            ResultSet rs = ps.executeQuery();

            result = rs.next();

            rs.close();
            ps.close();
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
            result = false;
        }

        return result;
    }

    private Connection getConnection() {
        return DirtDatabaseLib.getConnection(null, "development", null);
    }
}
