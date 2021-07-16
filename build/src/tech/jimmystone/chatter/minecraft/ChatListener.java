package tech.jimmystone.chatter.minecraft;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import org.slf4j.Logger;

public class ChatListener {

    private static ProxyServer proxy;
    private static Logger log;
    private static LuckPerms lp;

    public ChatListener(ProxyServer ps, Logger logger, LuckPerms luckperms) {
        proxy = ps;
        log = logger;
        lp = luckperms;
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        event.setResult(PlayerChatEvent.ChatResult.denied());

        if (!player.hasPermission("chatter.chat")) {
            player.sendMessage(
                    Component.text("To enable chat follow the instructions on our discord")
                            .color(TextColor.color(158, 6, 6))
            );
            return;
        }

        // get prefix
        final String prefix = lp.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix();
        final String suffix = lp.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getSuffix();
        Component strippedMsg = LegacyComponentSerializer
                                .legacyAmpersand().deserialize((prefix != null) ? prefix + " " : "")
                                .append(Component.text(player.getUsername() + " "))
                                .append(LegacyComponentSerializer.legacyAmpersand().deserialize((suffix != null) ? suffix + " : " : ": "))
                                .append(Component.text(message));
        // send message to all players
        proxy.getAllPlayers()
                .forEach(target -> target.sendMessage(strippedMsg));

    }
}
