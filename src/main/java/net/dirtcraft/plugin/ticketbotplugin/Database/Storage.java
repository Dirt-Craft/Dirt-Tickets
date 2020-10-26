package net.dirtcraft.plugin.ticketbotplugin.Database;

import net.dirtcraft.plugin.dirtdatabaselib.DirtDatabaseLib;
import net.dirtcraft.plugin.ticketbotplugin.Data.Ticket;
import net.dirtcraft.plugin.ticketbotplugin.Util.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Storage {

    public ArrayList<Ticket> getTickets(String username) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE lower(username) = ?")) {
            ps.setString(1, username.toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    tickets.add(new Ticket(
                            rs.getInt("id"),
                            rs.getBoolean("open"),
                            rs.getString("message"),
                            rs.getString("username"),
                            rs.getString("server"),
                            rs.getString("channel"),
                            rs.getString("level")));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return tickets;
    }

    public int getOpenTickets(String username) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE lower(username) = ? AND open = ?")) {
            ps.setString(1, username.toLowerCase());
            ps.setBoolean(2, true);

            try (ResultSet rs = ps.executeQuery()) {
                int i = 0;
                while (rs.next()) i++;
                return i;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    public void createTicket(String message, String username) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO tickets (message, username, server) VALUES (?, ?, ?)")) {

            ps.setString(1, message);
            ps.setString(2, username);
            ps.setString(3, Utility.getServerCode());
            ps.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean hasOpenTicket(String username) {

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE lower(username) = ?")) {
            ps.setString(1, username.toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private Connection getConnection() {
        return DirtDatabaseLib.getConnection("tickets", null);
    }
}
