package net.dirtcraft.plugin.ticketbotplugin.Commands;

import net.dirtcraft.plugin.ticketbotplugin.Database.Storage;
import net.dirtcraft.plugin.ticketbotplugin.Util.Utility;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class Create implements CommandExecutor {

    private final Storage storage;

    public Create(Storage storage) {
        this.storage = storage;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        String reason = args.<String>getOne("reason").get();

        if (!(source instanceof Player)) {
            storage.createTicket(reason, "Console");
        } else {
            Player player = (Player) source;
            storage.createTicket(reason, player.getName());
        }

        source.sendMessage(Utility.format("&aYour ticket has successfully been created!"));

        return CommandResult.success();
    }

}
