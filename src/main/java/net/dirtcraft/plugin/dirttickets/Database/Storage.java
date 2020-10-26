package net.dirtcraft.plugin.dirttickets.Database;

import net.dirtcraft.plugin.dirtdatabaselib.DirtDatabaseLib;
import net.dirtcraft.plugin.dirttickets.Data.Ticket;
import net.dirtcraft.plugin.dirttickets.Util.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class Storage {

    public ArrayList<Ticket> getOpenTickets(String username) {
        ArrayList<Ticket> tickets = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE lower(username) = ? AND open = ?")) {
            ps.setString(1, username.toLowerCase());
            ps.setBoolean(2, true);

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

    public Optional<Ticket> getTicketById(int id) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM tickets WHERE id = ?")) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(
                        new Ticket(
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
            return Optional.empty();
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
