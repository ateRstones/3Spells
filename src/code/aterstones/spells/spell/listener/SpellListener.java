package code.aterstones.spells.spell.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Tom on 30.01.2019.
 */
public class SpellListener implements Listener {

    private HashSet<UUID> lockedPlayers = new HashSet<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(lockedPlayers.contains(event.getPlayer().getUniqueId())) {
            if(event.getFrom().distanceSquared(event.getTo()) > 0) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        lockedPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDisconnect(PlayerKickEvent event) {
        lockedPlayers.remove(event.getPlayer().getUniqueId());
    }

    public void addLockedPlayer(Player p) {
        lockedPlayers.add(p.getUniqueId());
    }

    public void removeLockedPlayer(Player p) {
        lockedPlayers.remove(p.getUniqueId());
    }

}
