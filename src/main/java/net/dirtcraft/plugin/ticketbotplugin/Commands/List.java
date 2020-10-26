package net.dirtcraft.plugin.ticketbotplugin.Commands;

import net.dirtcraft.plugin.ticketbotplugin.Data.Ticket;
import net.dirtcraft.plugin.ticketbotplugin.Database.Storage;
import net.dirtcraft.plugin.ticketbotplugin.DirtTickets;
import net.dirtcraft.plugin.ticketbotplugin.Util.Utility;
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

import java.util.ArrayList;
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
        if (!(source instanceof Player) && !optionalUser.isPresent())
            throw new CommandException(Utility.format("&cYou must specify a user!"));
        else username = optionalUser.map(User::getName).orElseGet(source::getName);

        PaginationList.Builder pagination = PaginationList.builder();
        pagination.title(Utility.format("&cDirtCraft &7Support System"));
        pagination.padding(Utility.format("&4&m-"));

        Task.builder()
                .async()
                .execute(() -> {
                    ArrayList<Ticket> tickets = storage.getTickets(username);
                    pagination.contents(listTickets(tickets));
                    setFooter(pagination, (int) tickets.stream().filter(Ticket::isOpen).count());

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
