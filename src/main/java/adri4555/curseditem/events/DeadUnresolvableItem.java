package adri4555.curseditem.events;

import adri4555.curseditem.Main;
import adri4555.curseditem.inventory.InventoryPass;
import adri4555.curseditem.items.CursedItem;
import adri4555.curseditem.manager.ItemMananager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;


public class DeadUnresolvableItem  implements Listener {
    Main plugin;
    BukkitTask lastTask = null;
    public DeadUnresolvableItem(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void playerDropUlresolvableItem(PlayerDropItemEvent event) {
        Item itemDroped = event.getItemDrop();
        ItemStack item = itemDroped.getItemStack();
        CursedItem urei = new CursedItem(this.plugin, item);
        if(urei.isUnresolvableItem()){
            Player player = event.getPlayer();
            this.plugin.itemsInFlor.add(itemDroped);
            this.plugin.setUsernameCursed(player.getName());

            if(lastTask != null) {
                this.plugin.getServer().getScheduler().cancelTask(lastTask.getTaskId());
            };
            lastTask = Bukkit.getScheduler().runTaskLater(this.plugin, ()->{

                if(this.plugin.namePlayerDamned.equals(player.getName())){
                    ItemMananager.addItem(this.plugin, player);
                    this.plugin.removeAllItemsInFloor();
                }
            }, 100L);
        }
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int search = ItemMananager.playerContainsUnresolvableItem(this.plugin, player);
        if(!player.getName().equals(this.plugin.namePlayerDamned)){
            if(search != -1){
                player.getInventory().setItem(search, null);
            }
        }else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("El usuario maldito ha entrado");
            }
            ItemMananager.addItem(this.plugin, player);
        }
    }

    @EventHandler
    public void playerLeaveServer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(this.plugin.namePlayerDamned.equals(player.getName())){
            if(this.plugin.itemsInFlor.size() > 0 && this.plugin.namePlayerDamned.equals(player.getName())){
                this.plugin.setUsernameCursed(player.getName());
                ItemMananager.addItem(this.plugin, player);
                this.plugin.removeAllItemsInFloor();
            }
        }
    }

    @EventHandler
    public void playerHopperPickup(InventoryPickupItemEvent event) {
        Item item = event.getItem();
        CursedItem urei = new CursedItem(this.plugin, item.getItemStack());
        if(urei.isUnresolvableItem()){
            item.remove();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void pickEvent(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            CursedItem urei = new CursedItem(this.plugin, event.getItem().getItemStack());
            if(urei.isUnresolvableItem()){
                this.plugin.setUsernameCursed(player.getName());
                this.plugin.removeAllItemsInFloor();
            }
        }
    }

    @EventHandler
    public void onPlayerDead(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        Player player = (Player) event.getEntity();
        if(player.getName().equals(this.plugin.namePlayerDamned)){
            for(ItemStack item : drops){
                CursedItem urei = new CursedItem(this.plugin, item);
                if(urei.isUnresolvableItem()){
                    event.getDrops().remove(item);

                    this.plugin.setUsernameCursed(player.getName());
                }
            }
            this.plugin.removeAllItemsInFloor();
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if(player.getName().equals(this.plugin.namePlayerDamned) && player.getInventory().isEmpty()){
            CursedItem urei = new CursedItem(this.plugin);
            urei.initialize();
            player.getInventory().addItem(urei.getItem());
        }
    }

    @EventHandler
    public void onPlayerChangeDimension(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        this.plugin.removeAllItemsInFloor();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(item != null){
                CursedItem urei = new CursedItem(this.plugin, item);

                if (urei.isUnresolvableItem()) {
                    event.setCancelled(true);
                    Player player = event.getPlayer();
                    if(player.getName().equals("adri_205") && player.isSneaking()){
                        player.sendMessage("shifteando");
                        InventoryPass inv = new InventoryPass(plugin);
                        inv.openInventory(player);
                    }
                }
            }
        }

    }

    @EventHandler
    public void testik(PlayerInteractEntityEvent e) {
        if(e.getRightClicked()  instanceof ItemFrame) {
            Player player = e.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item != null) {
                CursedItem urei = new CursedItem(this.plugin, item);

                if (urei.isUnresolvableItem()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.PLAYER) && !event.getInventory().getType().equals(InventoryType.CRAFTING)) {
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null && event.getSlotType() != null && currentItem.getType() != Material.AIR){
                CursedItem urei = new CursedItem(this.plugin, currentItem);
                if (urei.isUnresolvableItem()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
