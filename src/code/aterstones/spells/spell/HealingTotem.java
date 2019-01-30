package code.aterstones.spells.spell;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

/**
 * Created by Tom on 30.01.2019.
 */
public class HealingTotem extends Spell {

    private ArmorStand stand;

    public HealingTotem(Player player) {
        super(player.getLocation().clone(), player, 20, 20, 20, 1);
    }

    @Override
    public void onStart() {
        stand = getLocation().getWorld().spawn(getLocation(), ArmorStand.class);
        stand.setGravity(false);
        stand.setCollidable(false);
        stand.setInvulnerable(true);
        stand.setCustomName(ChatColor.GREEN + "Heiltotem");
        stand.setCustomNameVisible(true);
    }

    @Override
    public void onCycle(int cycle) {
        collectPlayersInRange().forEach(p -> {
            double health = p.getHealth() + getModifier();
            // TODO Nondepricated alternative?
            if(health > p.getMaxHealth()) {
                health = p.getMaxHealth();
            }

            p.setHealth(health);
            getLocation().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation(), 10, 0.5, 0.5, 0.5);
        });
    }

    @Override
    public void onTick(int currentTicks) {
        for(int i = 0; i < 10; i++) {
            double rad = Math.random() * 2 * Math.PI;
            double x = getRange() * Math.sin(rad);
            double z = getRange() * Math.cos(rad);
            Location l = getLocation();
            int blockY = l.getWorld().getHighestBlockYAt((int) (x + l.getX()), (int) (z + l.getZ()));
            l.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, x + l.getX(), blockY, z + l.getZ(), 1);
        }
    }

    @Override
    public void onEnd() {
        stand.remove();
    }
}
