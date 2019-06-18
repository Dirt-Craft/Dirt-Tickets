package net.dirtcraft.plugin.ticketbotplugin.Commands;

import net.dirtcraft.plugin.ticketbotplugin.Data.Ticket;
import net.dirtcraft.plugin.ticketbotplugin.Database.Storage;
import net.dirtcraft.plugin.ticketbotplugin.Util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class List implements CommandExecutor {

    private final Storage storage;

    public List(Storage storage) {
        this.storage = storage;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!(source instanceof Player)) throw new CommandException(Utility.format("&cYou must specify a user!"));

        int openTickets;
        PaginationList.Builder pagination = PaginationList.builder();
        pagination.title(Utility.format("&cDirtCraft &7Support System"));
        pagination.padding(Utility.format("&4&m-"));

        if (!args.<User>getOne("user").isPresent()) {
            Player player = (Player) source;
            openTickets = storage.getOpenTickets(player.getName());

            pagination.contents(getTicketInfo(player.getName()));

            setFooter(pagination, openTickets);

        } else {
            User user = args.<User>getOne("user").get();
            openTickets = storage.getOpenTickets(user.getName());

            pagination.contents(getTicketInfo(user.getName()));

            setFooter(pagination, openTickets);
        }

        pagination.build().sendTo(source);

        return CommandResult.success();
    }

    private ArrayList<Text> getTicketInfo(String username) {

        ArrayList<Text> contents = new ArrayList<>();

        for (Ticket ticket : storage.listTickets(username.toLowerCase())) {
            Text.Builder ticketText = Text.builder();

            if (!ticket.isOpen()) {
                ticketText.append(Utility.format("&8#&6" + ticket.getId() + "&r &a✓"));
            } else {
                ticketText.append(Utility.format("&8#&6" + ticket.getId() + "&r &c✗"));
            }

            ArrayList<String> hover = new ArrayList<String>() {{
                add("&7Ticket ID&8: &6" + ticket.getId());
                add("&7Ticket Level&8: &6" + StringUtils.capitalize(ticket.getLevel()));
                if (ticket.getUsername(true) != null)
                    add("&7Username&8: &6" + ticket.getUsername(false));

                add(!ticket.isOpen() ? "&7Solved&8: &aYes" : "&7Solved&8: &cNo");
                add("&r\n&7Reason&8: &6" + ticket.getMessage());
            }};

            ticketText.onHover(TextActions.showText(Utility.format(String.join("\n", hover))));


            try {
                if (ticket.isOpen())
                    ticketText.onClick(TextActions.openUrl(new URL("https://discordapp.com/channels/269639757351354368/" + ticket.getChannel())));

            } catch (MalformedURLException exception) {
                ticketText.onHover(TextActions.showText(Utility.format("&cMalformed URL! Contact Administrator")));
            }

            contents.add(ticketText.build());

        }

        return contents;
    }

    private void setFooter(PaginationList.Builder pagination, int openTickets) {
        if (openTickets > 1) {
            pagination.footer(Utility.format("&7You currently have &b" + openTickets + "&7 open tickets"));
        } else if (openTickets == 1) {
            pagination.footer(Utility.format("&7You currently have &b" + openTickets + "&7 open ticket"));
        }
    }

}
