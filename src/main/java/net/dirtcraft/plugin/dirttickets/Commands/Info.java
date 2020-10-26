package net.dirtcraft.plugin.dirttickets.Commands;

import net.dirtcraft.plugin.dirttickets.Data.Ticket;
import net.dirtcraft.plugin.dirttickets.Database.Storage;
import net.dirtcraft.plugin.dirttickets.DirtTickets;
import net.dirtcraft.plugin.dirttickets.Util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Info implements CommandExecutor {
    
    private final DirtTickets main;
    private final Storage storage;
    
    public Info(DirtTickets main, Storage storage) {
        this.main = main;
        this.storage = storage;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        int id = args.<Integer>getOne("id").get();
        source.sendMessage(Utility.format("&7&oLoading information for ticket &8&o#&6&o" + id + "&7&o..."));
        
        Task.builder()
                .async()
                .execute(() -> {
                    Optional<Ticket> optionalTicket = storage.getTicketById(id);
                    if (!optionalTicket.isPresent()) {
                        source.sendMessage(Utility.format("&7The ticket &8#&e" + id + "&7 is &cnot valid"));
                        return;
                    }
                    Ticket ticket = optionalTicket.get();
                    PaginationList.Builder paginationBuilder = PaginationList.builder();
                    paginationBuilder.title(Utility.format("&7Ticket &8#&6" + id));
                    paginationBuilder.padding(Utility.format("&4&m-"));
                    List<Text> contents = new ArrayList<Text>() {{
                        add(Utility.format("&7Server&8: &6" + Optional.ofNullable(ticket.getServer()).orElse("N/A")));
                        add(Utility.format("&7Ticket ID&8: &6" + ticket.getId()));
                        add(Utility.format("&7Ticket Level&8: &6" + StringUtils.capitalize(ticket.getLevel())));
                        add(Utility.format("&7Username&8: &6" + Optional.ofNullable(ticket.getUsername()).orElse("N/A")));
                        add(Utility.format("&7Solved&8: " + (!ticket.isOpen() ? "&aYes" : "&cNo")));
                        add(Utility.format("&r\n&7Reason&8: &6" + ticket.getMessage()));
                    }};
                    paginationBuilder.contents(contents);
                    paginationBuilder.footer(Utility.format("&5&o&nClick to open ticket in Discord"));
                    paginationBuilder.sendTo(source);
                })
                .submit(main);
        return CommandResult.success();
    }
}
