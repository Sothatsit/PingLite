package net.sothatsit.pinglite;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PingLite extends JavaPlugin implements Listener {
    
    private static PingLite instance;
    private String line1;
    private String line2;
    private String maxPlayers;
    private long lastEdit = -1;
    
    public void onEnable() {
        instance = this;
        
        Bukkit.getPluginManager().registerEvents(this, this);
        
        reloadConfiguration();
        
        getCommand("pinglite").setExecutor(new PingLiteCommand());
    }
    
    public void onDisable() {
        instance = null;
    }
    
    public void reloadConfiguration() {
        File configFile = new File(getDataFolder(), "config.yml");
        
        if (lastEdit > 0 && configFile.exists() && configFile.lastModified() == lastEdit) {
            return;
        }
        
        lastEdit = configFile.lastModified();
        
        this.saveDefaultConfig();
        this.reloadConfig();
        
        log("Reloading PingLite Configuration");
        
        FileConfiguration config = getConfig();
        boolean save = false;
        
        if (!config.isSet("line-1") || !config.isString("line-1")) {
            getLogger().warning("Invalid \"line-1\" in config, resetting to default value");
            config.set("line-1", "&a&m---&7&m[-&5&l Ping&d&lLite &7&m-]&a&m---");
            save = true;
        }
        
        if (!config.isSet("line-2") || !config.isString("line-2")) {
            getLogger().warning("Invalid \"line-2\" in config, resetting to default value");
            config.set("line-2", "&aThis &dis &ea &bcool &cMotd");
            save = true;
        }
        
        if (!config.isSet("max-players") || (!config.isString("max-players") && !config.isInt("max-players"))) {
            getLogger().warning("Invalid \"max-players\" in config, resetting to default value");
            config.set("max-players", "standard");
            save = true;
        }
        
        if (save) {
            saveConfig();
        }
        
        line1 = ChatColor.translateAlternateColorCodes('&', config.getString("line-1"));
        line2 = ChatColor.translateAlternateColorCodes('&', config.getString("line-2"));
        
        log("Loaded Motd: ");
        log(line1);
        log(line2);
        
        if (config.isInt("max-players")) {
            maxPlayers = Integer.toString(config.getInt("max-players"));
        } else {
            maxPlayers = config.getString("max-players");
        }
        
        log("Loaded max-players: " + maxPlayers);
        
        log("PingLite Configuration Reloaded");
    }
    
    public String getLine1() {
        return line1;
    }
    
    public String getLine2() {
        return line2;
    }
    
    public void setLine1(String line1) {
        this.line1 = line1;
        setConfigValue("line-1", line1, "\"line-1\" set to \"%value%\"");
    }
    
    public void setLine2(String line2) {
        this.line2 = line2;
        setConfigValue("line-2", line2, "\"line-2\" set to \"%value%\"");
    }
    
    public String getMotd() {
        return line1 + "\n" + line2;
    }
    
    public String getMaxPlayersMode() {
        return maxPlayers;
    }
    
    public void setMaxPlayersMode(String maxPlayers) {
        this.maxPlayers = maxPlayers;
        setConfigValue("max-players", maxPlayers, "\"max-players\" set to \"%value%\"");
    }
    
    public boolean isCustomMaxPlayers() {
        return getMaxPlayers() >= 0;
    }
    
    public int getMaxPlayers() {
        if (maxPlayers.equalsIgnoreCase("plus-one"))
            return Bukkit.getOnlinePlayers().size() + 1;
        
        try {
            return Integer.valueOf(maxPlayers);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerListPing(ServerListPingEvent e) {
        reloadConfiguration();
        
        e.setMotd(getMotd());
        
        if (isCustomMaxPlayers()) {
            e.setMaxPlayers(getMaxPlayers());
        }
    }
    
    private void setConfigValue(String key, Object value, String msg) {
        getConfig().set(key, value);
        saveConfig();
        lastEdit = new File(getDataFolder(), "config.yml").lastModified();
        log(msg.replace("%value%", msg + ChatColor.RESET));
    }
    
    private void log(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
    
    public static PingLite getInstance() {
        return instance;
    }
    
}
