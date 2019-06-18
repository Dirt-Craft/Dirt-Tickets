package net.dirtcraft.plugin.ticketbotplugin;

import com.google.inject.Inject;
import net.dirtcraft.plugin.ticketbotplugin.Commands.CommandRegistry;
import net.dirtcraft.plugin.ticketbotplugin.Database.Storage;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = "ticketbot-plugin",
        name = "TicketBot Plugin",
        description = "Integration with DirtCraft's ticket bot",
        url = "https://dirtcraft.net/",
        authors = {
                "juliann"
        }
)
public class TicketBotPlugin {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    private TicketBotPlugin instance;
    private Storage storage;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        instance = this;
        storage = new Storage(instance);

        new CommandRegistry(storage, instance);
    }
}
