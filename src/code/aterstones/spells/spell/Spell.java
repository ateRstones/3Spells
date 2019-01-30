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

        runStart();
        runTicker();
    }

    private void runStart() {
        try {
            onStart();
        } catch (Exception e) {
            throw new RuntimeException("Error in spell onStart execution: " + e.getMessage(), e);
        }
    }

    private void runTicker() {
        final int ticks = cycleCount * cycleLength;
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                currentTicks++;
                if(ticks >= currentTicks) {
                    try {
                        onTick(currentTicks);
                    } catch (Exception e) {
                        cancelSpell();
                        throw new RuntimeException("Error in spell onTick execution: " + e.getMessage(), e);
                    }

                    if(currentTicks % cycleLength == 0) {
                        try {
                            onCycle(currentTicks / cycleLength);
                        } catch (Exception e) {
                            cancelSpell();
                            throw new RuntimeException("Error in spell onCycle execution: " + e.getMessage(), e);
                        }
                    }
                }
                else {
                    cancelSpell();
                }
            }
        };
        runnable.runTaskTimer(ThreeSpells.getInstance(), 0, 1);
    }

    public abstract void onTick(int currentTicks);
    public abstract void onCycle(int cycle);
    public abstract void onStart();
    public abstract void onEnd();

    public void cancelSpell() {
        runnable.cancel();

        try {
            onEnd();
        } catch (Exception e) {
            throw new RuntimeException("Error in spell onEnd execution: " + e.getMessage(), e);
        }
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
