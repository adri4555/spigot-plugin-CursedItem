package adri4555.curseditem.commands;

import adri4555.curseditem.Main;
import adri4555.curseditem.items.CursedItem;
import adri4555.curseditem.manager.ItemMananager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class command implements CommandExecutor {

    Main plugin;

    public command(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length >= 1) {
                if(player.isOp()) {
                    if(args[0].equalsIgnoreCase("getitem")){
                        if(plugin.namePlayerDamned == null){
                            ItemMananager.addItem(plugin, player);
                            return true;
                        }
                        if(args.length >= 2 ){
                            if(args[1].equalsIgnoreCase("force")){
                                ItemMananager.addItem(plugin, player);
                                return true;
                            }
                        }
                        player.sendMessage("Ya hay un item en juego");
                        return true;
                    }else if(args[0].equalsIgnoreCase("reload")){
                        this.plugin.config.reloadConfig();
                    }
                }else{
                    player.sendMessage("No tienes permisos para ejecutar ese comando");
                }
            }else {
                player.sendMessage(plugin.chat("El usuario que esta maldito es: &e" + plugin.namePlayerDamned));
            }
        }

        return false;
    }
}
