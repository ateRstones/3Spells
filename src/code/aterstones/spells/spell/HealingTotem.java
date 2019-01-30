package code.aterstones.spells.spell;

import org.bukkit.entity.Player;

/**
 * Created by Tom on 30.01.2019.
 */
public class HealingTotem extends Spell {

    public HealingTotem(Player player) {
        super(player.getLocation().clone(), player, 20, 20, 20, 1);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCycle(int currentTicks) {

    }

    @Override
    public void onEnd() {

    }
}
