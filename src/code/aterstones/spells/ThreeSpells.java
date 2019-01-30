package code.aterstones.spells;

import code.aterstones.spells.command.SpellCommand;
import code.aterstones.spells.spell.HealingTotem;
import code.aterstones.spells.spell.Spell;
import code.aterstones.spells.spell.Spread;
import code.aterstones.spells.spell.listener.SpellListener;
import code.aterstones.spells.util.CommandRegisterer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Tom on 30.01.2019.
 */
public class ThreeSpells extends JavaPlugin {

    private static ThreeSpells instance;

    private SpellListener spellListener;
    private HashMap<Integer, Function<Player, Spell>> spellMap = new HashMap<>();
    private int id = 1;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        //Registering Commands
        CommandRegisterer.registerCommand(this, "spell", "Executes the spells 1 to 3", new SpellCommand());

        //Registering Listeners
        spellListener = new SpellListener();
        Bukkit.getPluginManager().registerEvents(spellListener, this);

        //Registering Spells
        addSpell(p -> new Spread(p));
        addSpell(p -> new HealingTotem(p));
    }

    @Override
    public void onDisable() {

    }

    public void addSpell(Function<Player, Spell> spellCreator) {
        spellMap.put(id++, spellCreator);
    }

    public boolean executeSpell(Player player, int id) {
        Function<Player, Spell> spellCreator = spellMap.get(id);

        if(spellCreator != null) {
            spellCreator.apply(player);
            return true;
        } else {
            return false;
        }
    }

    public SpellListener getSpellListener() {
        return spellListener;
    }

    public static ThreeSpells getInstance() {
        return instance;
    }
}
