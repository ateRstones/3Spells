package code.aterstones.spells.spell;

import code.aterstones.spells.ThreeSpells;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Tom on 30.01.2019.
 */
public abstract class Spell {

    protected Location location;
    private Player player;

    private int cycleCount;
    private int cycleLength;
    private double range;
    private double modifier;

    private int currentTicks = 0;
    private BukkitRunnable runnable;

    public Spell(Location location, Player player, int cycleCount, int cycleLength, double range, double modifier) {
        this.location = location;
        this.player = player;
        this.cycleCount = cycleCount;
        this.cycleLength = cycleLength;
        this.range = range;
        this.modifier = modifier;

        runTicker();
    }

    private void runTicker() {
        final int ticks = cycleCount * cycleLength;
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(ticks > currentTicks) {
                    if(currentTicks % cycleLength == 0) {
                        try {
                            onCycle(currentTicks);
                        } catch (Exception e) {
                            cancel();
                            throw new RuntimeException("Error in spell execution: " + e.getMessage(), e);
                        }
                    }
                }
                else {
                    cancel();
                }
                currentTicks++;
            }
        };
        runnable.runTaskTimer(ThreeSpells.getInstance(), 0, 1);
    }

    public abstract void onCycle(int currentTicks);

    public void cancel() {
        runnable.cancel();
    }

    public List<Player> collectPlayersInRangeExceptCaster() {
        List<Player> rangePlayers = collectPlayersInRange();
        rangePlayers.remove(player);
        return rangePlayers;
    }

    public List<Player> collectPlayersInRange() {
        return Bukkit.getOnlinePlayers().stream().filter(p -> isInRange(p)).collect(Collectors.toList());
    }

    public boolean isInRange(Player p) {
        Location loc = p.getLocation();
        return loc.getWorld() == location.getWorld() &&
                loc.toVector().distanceSquared(location.toVector()) <= range * range;
    }

    public Location getLocation() {
        return location;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public double getRange() {
        return range;
    }

    public double getModifier() {
        return modifier;
    }

    public int getCycleLength() {
        return cycleLength;
    }
}
