package code.aterstones.spells.spell;

import code.aterstones.spells.ThreeSpells;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Tom on 30.01.2019.
 */
public class Spread extends Spell implements Listener {

    public Spread(Player player) {
        super(player.getLocation().clone(), player, 10, 5, 10, 4);
        Bukkit.getPluginManager().registerEvents(this, ThreeSpells.getInstance());
    }

    @Override
    public void onTick(int currentTicks) {

    }

    @Override
    public void onCycle(int cycle) {
        int currentRadius = (int) Math.floor((getRange() / getCycleCount()) * (cycle + 1));
        Location l = getLocation();

        for(int x = -currentRadius; x < currentRadius; x++) {
            for(int z = -currentRadius; z < currentRadius; z++) {
                double cx = 0.5 + x;
                double cz = 0.5 + z;

                if(cx * cx + cz * cz <= currentRadius * currentRadius) {
                    int bx = l.getBlockX() + x;
                    int bz = l.getBlockZ() + z;
                    l.getWorld().getHighestBlockAt(bx, bz).setType(Material.FIRE);
                }
            }
        }
    }

    @Override
    public void onStart() {
        lockCaster();
    }

    @Override
    public void onEnd() {
        collectPlayersInRangeExceptCaster().forEach(p -> {
            p.damage(getModifier(), getPlayer());
            p.setFireTicks(0);
        });

        int currentRadius = (int) Math.ceil(getRange()) + 1;
        Location l = getLocation();

        for(int x = -currentRadius; x < currentRadius; x++) {
            for(int z = -currentRadius; z < currentRadius; z++) {

                int bx = l.getBlockX() + x;
                int bz = l.getBlockZ() + z;
                Block b = l.getWorld().getHighestBlockAt(bx, bz);

                if(b.getType() == Material.FIRE) {
                    b.setType(Material.AIR);
                }
            }
        }

        unlockCaster();
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onFireDamage(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
            if (event.getEntity() instanceof Player) {
                Player p = (Player) event.getEntity();

                if (isInRange(p)) {
                    event.setCancelled(true);
                    p.setFireTicks(0);
                }
            }
        }
    }
}
