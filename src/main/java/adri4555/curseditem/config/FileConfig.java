package adri4555.curseditem.config;

import adri4555.curseditem.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class FileConfig {

	private FileConfiguration config = null;
	private File configFile = null;
	
	public Main plugin;
	private String fileName;
	
	
	public FileConfig(Main plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.registerConfig();
	}
	
	
	public FileConfiguration getConfig() {
		if(config == null) {
			reloadConfig();
		}
		
		return config;
	}
	
	public void reloadConfig() {
		if(config == null) {
			configFile = new File(plugin.getDataFolder(), this.fileName);
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(plugin.getResource(this.fileName), "UTF8");
			if(defConfigStream != null) {
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				config.setDefaults(defConfig);
			}
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	 
	public void saveConfig() {
		try {
			config.save(configFile);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void registerConfig() {
		configFile = new File(plugin.getDataFolder(), this.fileName);
		if(!configFile.exists()) {
			this.getConfig().options().copyDefaults(true);
			this.saveConfig();
		}
	}
}
