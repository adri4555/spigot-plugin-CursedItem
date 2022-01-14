package adri4555.curseditem;

import adri4555.curseditem.commands.command;
import adri4555.curseditem.config.FileConfig;
import adri4555.curseditem.events.DeadUnresolvableItem;
import adri4555.curseditem.inventory.InventoryPass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();
    public String latestversion;
    public String name = pdffile.getName();

    //Initial Variables
    public List<Item> itemsInFlor = new ArrayList<>();
    public String namePlayerDamned = null;
    public FileConfig config = null;




    @Override
    public void onEnable(){
        registerEvents();
        registerCommands();
        registerConfigs();
        updateChecker();
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new DeadUnresolvableItem(this), this);
        pm.registerEvents(new InventoryPass(this), this);
    }

    public void registerCommands() {

        this.getCommand("curi").setExecutor(new command(this));

    }

    public String chat(String title){
        return ChatColor.translateAlternateColorCodes('&', title);
    }

    public void registerConfigs(){
        this.config = new FileConfig(this,"config.yml");

        if(this.config.getConfig().contains("Last-cursed-username")){
            String actualConfigUsername = config.getConfig().getString("Last-cursed-username");
            if(actualConfigUsername != null) {
                this.namePlayerDamned = actualConfigUsername;
            }
        }
    }

    public void setUsernameCursed(String username) {
        FileConfiguration fileConfig =  this.config.getConfig();
        String actualConfigUsername = fileConfig.getString("Last-cursed-username");
        if(actualConfigUsername == null || actualConfigUsername != username){
            fileConfig.set("Last-cursed-username", username);
            this.config.saveConfig();
        }
        this.namePlayerDamned = username;
    }

    public void removeAllItemsInFloor(){
        try {
            for(int i = 0; i< this.itemsInFlor.size(); i++){
                this.itemsInFlor.get(i).remove();
                this.itemsInFlor.remove(i);
            }
        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }

    }

    public void updateChecker(){
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=99181").openConnection();
            int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (latestversion.length() <= 7) {

                if(!version.equals(latestversion)){
                    Bukkit.getConsoleSender().sendMessage("");
                    Bukkit.getConsoleSender().sendMessage("#######################################################################");
                    Bukkit.getConsoleSender().sendMessage("#     There is a new version available for the plugin [" + name + "]    #");
                    Bukkit.getConsoleSender().sendMessage("# Its current version is (" + version + ") and the version (" + latestversion + ") is available #");
                    Bukkit.getConsoleSender().sendMessage("#        If you want the new changes you just have to update ;)       #");
                    Bukkit.getConsoleSender().sendMessage("#                To download visit the following link:                #");
                    Bukkit.getConsoleSender().sendMessage("#        https://www.spigotmc.org/resources/cursed-item.99181/        #");
                    Bukkit.getConsoleSender().sendMessage("#######################################################################");
                    Bukkit.getConsoleSender().sendMessage("");
                }
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(this.name + ChatColor.RED + "Error while checking update.");
        }
    }

}
