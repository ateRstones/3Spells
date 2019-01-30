package code.aterstones.spells.command;

import code.aterstones.spells.ThreeSpells;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Tom on 30.01.2019.
 */
public class SpellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player p = (Player) commandSender;

            if (strings.length >= 1) {
                try {
                    int id = Integer.parseInt(strings[0]);

                    if(!ThreeSpells.getInstance().executeSpell(p, id)) {
                        p.sendMessage(ChatColor.RED + "Dieser Spell existiert nicht.");
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.RED + "Gib bitte eine Nummer an.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "/spells <number> - Gebe bitte eine Nummber von 1 bis 3 an");
            }
        }
        else {
            commandSender.sendMessage("Dieser Command ist nur für Spieler verfügbar.");
        }
        return true;
    }
}
