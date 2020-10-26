package net.dirtcraft.plugin.dirttickets.Commands;

import net.dirtcraft.plugin.dirttickets.Database.Storage;
import net.dirtcraft.plugin.dirttickets.DirtTickets;
import net.dirtcraft.plugin.dirttickets.Util.Utility;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

public class Create implements CommandExecutor {

    private final DirtTickets main;
    private final Storage storage;

    public Create(DirtTickets main, Storage storage) {
        this.main = main;
        this.storage = storage;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        String reason = args.<String>getOne("reason").get();
        if (!(source instanceof Player))
            throw new CommandException(Utility.format("&cOnly a player can create a ticket!"));
        Player player = (Player) source;

        source.sendMessage(Utility.format("&7&oCreating ticket for &6&o" + player.getName() + "&7&o..."));
        Task.builder()
                .async()
                .execute(() -> {
                    storage.createTicket(reason, player.getName());
                    source.sendMessage(Utility.format("&aYour ticket has successfully been created!"));
                })
                .submit(main);

        return CommandResult.success();
    }

}