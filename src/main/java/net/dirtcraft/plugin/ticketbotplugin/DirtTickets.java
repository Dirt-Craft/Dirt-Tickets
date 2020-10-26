package net.dirtcraft.plugin.ticketbotplugin;

import net.dirtcraft.plugin.ticketbotplugin.Commands.CommandRegistry;
import net.dirtcraft.plugin.ticketbotplugin.Database.Storage;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "dirt-tickets",
        name = "Dirt Tickets",
        description = "Integration with DirtCraft's ticket bot",
        url = "https://dirtcraft.net/",
        authors = "juliann"
)
public class DirtTickets {

    private Storage storage;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        this.storage = new Storage();

        new CommandRegistry(storage, this);
    }
}
