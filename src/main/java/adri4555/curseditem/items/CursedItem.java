package adri4555.curseditem.items;

import adri4555.curseditem.Main;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CursedItem {
    private static String PATH_ITEM_META = "isUnresolvableItem";
    ItemStack item = null;
    Main plugin =  null;

    public CursedItem(Main plugin, ItemStack item) {
        this.plugin = plugin;
        this.item = item;
    }

    public CursedItem(Main plugin) {
        this(plugin, new ItemStack(getMaterial(plugin), 1 ));
    }

    public boolean isUnresolvableItem(){
        NBTItem nbti = new NBTItem(this.item);
        String isItem =  nbti.getString(PATH_ITEM_META);
        return isItem.equals("true");
    }

    private void setItemInitialMeta(){
        ItemMeta meta = this.item.getItemMeta();

        //Enchantaments
        meta.addEnchant(Enchantment.ARROW_FIRE, 1, true );
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        //Titles
        String titleConfig = plugin.config.getConfig().getString("Item.title");
        if(titleConfig != null){
            meta.setDisplayName(plugin.chat(titleConfig));
        }

        //Description
        List<String> descriptionConfig = plugin.config.getConfig().getStringList("Item.description");
        if(descriptionConfig != null){
            List<String> newLore = new ArrayList<>();
            for(String line : descriptionConfig) {
                newLore.add(plugin.chat(line));
            }
            meta.setLore(newLore);
        }

        this.item.setItemMeta(meta);
    }

    public void initialize(){
        NBTItem nbti = new NBTItem(this.item);
        nbti.setString(PATH_ITEM_META, "true");
        this.item = nbti.getItem();
        setItemInitialMeta();
    }

    public ItemStack getItem() {
        return this.item;
    }

    private static Material getMaterial(Main plugin){
        String materialConfig = plugin.config.getConfig().getString("Item.item-id");
        materialConfig = materialConfig != null ? materialConfig : "MOOSHROOM_SPAWN_EGG";
        return Material.matchMaterial(materialConfig);
    }
}
