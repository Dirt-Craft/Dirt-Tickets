package net.dirtcraft.plugin.dirttickets.Commands;

import net.dirtcraft.plugin.dirttickets.Data.Ticket;
import net.dirtcraft.plugin.dirttickets.Database.Storage;
import net.dirtcraft.plugin.dirttickets.DirtTickets;
import net.dirtcraft.plugin.dirttickets.Util.Utility;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class List implements CommandExecutor {

    private final DirtTickets main;
    private final Storage storage;

    public List(DirtTickets main, Storage storage) {
        this.main = main;
        this.storage = storage;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        String username;
        Optional<User> optionalUser = args.<User>getOne("user");
        boolean showAll = args.hasAny("a");
        if (!(source instanceof Player) && !optionalUser.isPresent())
            throw new CommandException(Utility.format("&cYou must specify a user!"));
        else username = optionalUser.map(User::getName).orElseGet(source::getName);

        PaginationList.Builder pagination = PaginationList.builder();
        pagination.title(Utility.format("&cDirtCraft &7Support System"));
        pagination.padding(Utility.format("&4&m-"));

        source.sendMessage(Utility.format("&7&oLoading tickets for &6&o" + username + "&7&o..."));
        Task.builder()
                .async()
                .execute(() -> {
                    ArrayList<Ticket> tickets;

                    if (!showAll) {
                        tickets = storage.getOpenTickets(username);
                        setFooter(pagination, tickets.size());
                    } else {
                        tickets = storage.getTickets(username);
                        tickets.sort(Comparator.comparing(Ticket::isOpen).reversed());
                        setFooter(pagination, (int) tickets.stream().filter(Ticket::isOpen).count());
                    }

                    if (tickets.size() <= 0) {
                        Text text;
                        if (showAll) text = Text.builder()
                                .append(
                                        Utility.format(
                                                "&7&oTo create a new ticket, use &6&o&n/ticket create <reason>&r"))
                                .onHover(TextActions.showText(Utility.format("&5&oClick here to create a ticket!&r")))
                                .onClick(TextActions.suggestCommand("/ticket create <reason>"))
                                .build();
                        else text = Text.builder()
                                .append(
                                        Utility.format(
                                                "&7&oThere are no open tickets at this time! If you would like view all tickets, use &6&o&n/ticket list -a&r"))
                                .onHover(TextActions.showText(Utility.format("&5&oClick here to execute &6&o&n/ticket list -a&r")))
                                .onClick(TextActions.runCommand("/ticket list -a"))
                                .build();
                        pagination.contents(text);
                    }
                    else pagination.contents(listTickets(tickets));
                    pagination.build().sendTo(source);
                })
                .submit(main);

        return CommandResult.success();
    }

    private ArrayList<Text> listTickets(ArrayList<Ticket> tickets) {
        ArrayList<Text> contents = new ArrayList<>();

        for (Ticket ticket : tickets)
            contents.add(Utility.getTicketInfo(ticket));

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
