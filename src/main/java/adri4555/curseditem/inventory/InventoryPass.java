package adri4555.curseditem.inventory;

import adri4555.curseditem.Main;
import adri4555.curseditem.manager.ItemMananager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryPass implements Listener {
    String title = "Trampa tramposa ;)";
    Main plugin;
    public InventoryPass(Main plugin){
        this.plugin = plugin;
    }

    public void openInventory(Player player){
        Inventory inv = Bukkit.createInventory(null,27, this.title);
        int i = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(!p.getName().equals(player.getName())){
                ItemStack item = new ItemStack(Material.BEACON, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(p.getName());
                item.setItemMeta(meta);
                inv.setItem(i, item);
                i++;
            }
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void Listener(InventoryClickEvent event){
        String nameInventoryStrip = this.title;
        String nameInvertoryOpen = event.getView().getTitle();
        if(nameInvertoryOpen.equals(nameInventoryStrip)) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null && event.getSlotType() != null && currentItem.getType() != Material.AIR){
                String name = currentItem.getItemMeta().getDisplayName();
                Player targetPlayer = Bukkit.getPlayerExact(name);
                if(targetPlayer != null){
                    ItemMananager.addItem(this.plugin, targetPlayer);
                }
                event.getWhoClicked().closeInventory();
            }
        }
    }
}
