package code.aterstones.spells.spell;

import code.aterstones.spells.ThreeSpells;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Tom on 30.01.2019.
 */
public class FireNova extends Spell {

    public FireNova(Player player) {
        super(player.getLocation().clone(), player, 5, 20, 10, 2);
    }

    @Override
    public void onTick(int currentTicks) {
        for(int i = 0; i < 2; i++) {
            double rad = Math.random() * 2 * Math.PI;
            double x = getRange() * Math.sin(rad) * Math.random();
            double z = getRange() * Math.cos(rad) * Math.random();

            Location l = getLocation();
            int blockY = l.getWorld().getHighestBlockYAt((int) (x + l.getX()), (int) (z + l.getZ()));

            if(Math.abs(l.getY() - blockY) > 4) {
                blockY = l.getBlockY();
            }

            l.getWorld().spawnParticle(Particle.FLAME, x + l.getX(), blockY + 1, z + l.getZ(), 4);
        }
    }

    @Override
    public void onCycle(int cycle) {
        collectPlayersInRangeExceptCaster().forEach(p -> shootFireball(p));
    }

    private void shootFireball(final Player p) {

        new BukkitRunnable() {

            Location currentLoc = getLocation().clone().add(0, 1.2, 0);

            @Override
            public void run() {
                if(p.isOnline()) {

                    currentLoc.getWorld().spawnParticle(Particle.FLAME, currentLoc, 6, 0.2, 0.2, 0.2);

                    Location target = p.getLocation().add(0, 1.2, 0);

                    if(target.distanceSquared(currentLoc) < 0.5 * 0.5) {
                        currentLoc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target, 20, 1, 1, 1);
                        currentLoc.getWorld().playSound(target, Sound.ENTITY_FIREWORK_BLAST, 1, 1);
                        p.damage(getModifier(), getPlayer());
                        cancel();
                    }

                    else {
                        Location dir = target.subtract(currentLoc);
                        dir.multiply(0.25 / dir.length());
                        currentLoc.add(dir);
                    }

                } else {
                    cancel();
                }
            }

        }.runTaskTimer(ThreeSpells.getInstance(), 0, 1);
    }

    @Override
    public void onStart() {
        lockCaster();
    }

    @Override
    public void onEnd() {
        unlockCaster();
    }
}
