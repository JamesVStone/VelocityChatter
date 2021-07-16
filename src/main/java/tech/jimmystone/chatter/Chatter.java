package tech.jimmystone.chatter;

import com.google.common.collect.HashMultimap;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.slf4j.Logger;
import tech.jimmystone.chatter.discord.DiscordHandler;
import tech.jimmystone.chatter.discord.DiscordLoader;
import tech.jimmystone.chatter.minecraft.ChatListener;

import javax.security.auth.login.LoginException;

/*
Permission structure for chatter:
chatter.chat -> send messages in current server, send PMs and receive party invites
chatter.global -> use global chat
chatter.staff -> send and receive messages to staff channel

Command Structure:
/g <message> -> sends message to global chat with [global] prefix
/msg <user> <message> -> sends message to user (future add nick support)
/r <message> -> reply to last messaged user
/sc <message> sends message to staff chat
/staff <message> sends one way message to staff (/r or staff will then be mapped to that user)
 */
@Plugin(
        id = "chatter",
        name = "Chatter",
        version = "1.0-SNAPSHOT",
        authors = {"JamesVStone"},
        dependencies = {
            @Dependency(id = "luckperms"),
        }
)
public final class Chatter {

    @Inject
    private Logger logger;
    @Inject
    private ProxyServer server;
    // private HashMultimap<Player, String> replyMap;

    @Inject
    public void ChatterPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        logger.info("Loading chatter");
    }
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // create luckperms singleton for event reactors
        LuckPerms lpApi = LuckPermsProvider.get();
        logger.info("Spawning discord bot");
        try {
            DiscordLoader.startBot("");
            new DiscordHandler(server);
        } catch (LoginException e) {
            e.printStackTrace();
            logger.error("Failed to log into discord");
            return;
        }
        logger.info("Registering listeners");
        // register commands and listeners with the proxy
        server.getEventManager().register(this, new ChatListener(server, logger, lpApi));
        logger.info("Chatter enabled");
    }
}
