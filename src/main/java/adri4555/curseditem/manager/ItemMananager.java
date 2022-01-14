package adri4555.curseditem.manager;

import adri4555.curseditem.Main;
import adri4555.curseditem.items.CursedItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ItemMananager {
    public static void addItem(Main plugin, Player player){
        if(plugin.namePlayerDamned != null){
            if(!plugin.namePlayerDamned.equals(player.getName())){
                Player oldPlayer = Bukkit.getPlayerExact(plugin.namePlayerDamned);
                if(oldPlayer != null){
                    int search = playerContainsUnresolvableItem(plugin, oldPlayer);
                    if(search != -1) {
                        oldPlayer.getInventory().setItem(search, null);
                    }
                }
            }
        }

        int searchItem = playerContainsUnresolvableItem(plugin, player);
        if( searchItem != -1){
            //player.getInventory().removeItem(player.getInventory().getItem(searchItem));
            player.getInventory().setItem(searchItem, getItemDefault(plugin));
            return;
        }

        if(player.getInventory().firstEmpty() == -1){
            int selectedItem = new Random().nextInt(player.getInventory().getSize()-1);
            ItemStack itemToDropped = player.getInventory().getItem(selectedItem);
            player.getInventory().setItem(selectedItem, null);
            Item itemDropped = player.getWorld().dropItemNaturally(player.getLocation(), itemToDropped);
            itemDropped.setPickupDelay(40);
        }

        plugin.setUsernameCursed(player.getName());
        player.getInventory().addItem(getItemDefault(plugin));
    }

    public static ItemStack getItemDefault(Main plugin){
        CursedItem newUrei = new CursedItem(plugin);
        newUrei.initialize();
        return newUrei.getItem();
    }

    public static int playerContainsUnresolvableItem(Main plugin, Player player){
        ItemStack[] inventory = player.getInventory().getContents();
        for(int i = 0; i < inventory.length; i++){
            if(inventory[i] != null && inventory[i].getType() != Material.AIR){
                CursedItem urei = new CursedItem(plugin, inventory[i]);
                if(urei.isUnresolvableItem()){
                    return i;
                }
            }
        }
        return -1;
    }

}
