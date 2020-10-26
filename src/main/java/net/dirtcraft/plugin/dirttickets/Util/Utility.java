package net.dirtcraft.plugin.dirttickets.Util;

import net.dirtcraft.discord.spongediscordlib.SpongeDiscordLib;
import net.dirtcraft.plugin.dirttickets.Data.Ticket;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

public class Utility {

    public static Text format(String message) {
        return TextSerializers.FORMATTING_CODE.deserialize(message);
    }

    public static Text getTicketInfo(Ticket ticket) {
        Text.Builder ticketText = Text.builder();

        ticketText.append(Utility.format("&6Ticket &8#&7" + ticket.getId() + "&r " +
                (!ticket.isOpen() ? "&a✓" : "")));

        ArrayList<String> hover = new ArrayList<String>() {{
            add("&7Server&8: &6" + Optional.ofNullable(ticket.getServer()).orElse("N/A"));
            add("&7Ticket ID&8: &6" + ticket.getId());
            add("&7Ticket Level&8: &6" + StringUtils.capitalize(ticket.getLevel()));
            add("&7Username&8: &6" + Optional.ofNullable(ticket.getUsername()).orElse("N/A"));
            add("&7Solved&8: " + (!ticket.isOpen() ? "&aYes" : "&cNo"));
            add("&r\n&7Reason&8: &6" + ticket.getMessage());
        }};

        ticketText.onHover(TextActions.showText(Utility.format(String.join("\n", hover))));

        try {
            if (ticket.isOpen())
                ticketText.onClick(
                        TextActions.openUrl(
                                new URL(
                                        "https://discordapp.com/channels/269639757351354368/"
                                                + ticket.getChannel())));
        } catch (MalformedURLException exception) {
            ticketText.onHover(TextActions.showText(Utility.format("&cMalformed URL! Contact Administrator")));
        }
        return ticketText.build();
    }

    public static String getServerCode() {
         return SpongeDiscordLib
                 .getJDA()
                 .getTextChannelById(
                         SpongeDiscordLib.getGamechatChannelID())
                 .getName().split("-")[1].toLowerCase();
    }

}