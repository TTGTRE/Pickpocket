package logan.pickpocket.main;

import logan.pickpocket.commands.*;
import logan.pickpocket.events.InventoryClick;
import logan.pickpocket.events.InventoryClose;
import logan.pickpocket.events.PlayerInteract;
import logan.pickpocket.events.PlayerJoin;
import logan.pickpocket.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import logan.pickpocket.profile.PickpocketItemInventory;

/**
 * Created by Tre on 12/14/2015.
 */
public class Pickpocket extends JavaPlugin {

    private static Pickpocket instance;
    
    public static final String NAME = "Pickpocket";
    public static final String PLUGIN_FOLDER_DIRECTORY = "plugins/" + NAME + "/";
    public static final String PLAYER_DIRECTORY = PLUGIN_FOLDER_DIRECTORY + "players/";

    private Server server = getServer();
    private Logger logger = getLogger();

    private static Vector<Profile> profiles;
    private Map<Player, Integer> cooldowns;
    private int cooldownDelay = 8;

    private PickpocketCommand profilesCommand;
    private PickpocketCommand itemsCommand;
    private PickpocketCommand stealsCommand;
    private PickpocketCommand adminCommand;
    private PickpocketCommand bypassCommand;
    private PickpocketCommand exemptCommand;

    public static final Permission PICKPOCKET_EXEMPT = new Permission("pickpocket.exempt", "Exempt a user from being stolen from.");
    public static final Permission PICKPOCKET_BYPASS = new Permission("pickpocket.bypass", "Allows user to bypass cooldown.");
    public static final Permission PICKPOCKET_ADMIN = new Permission("pickpocket.admin", "Logs pickpocket information to admins.");
    public static final Permission PICKPOCKET_DEVELOPER = new Permission("pickpocket.developer", "Allows use of developer commands.");
    
    private static Map<UUID, PickpocketItemInventory> registeredInventories = new HashMap<>();

    private PickpocketConfiguration configuration;

    private BukkitScheduler scheduler;

    public void onEnable() {
        instance = this;
        
        File folder = new File(PLUGIN_FOLDER_DIRECTORY);
        File playerFolder = new File(PLAYER_DIRECTORY);
        folder.mkdirs();
        playerFolder.mkdirs();

        profiles = new Vector<>();
        cooldowns = new ConcurrentHashMap<>();

        profilesCommand = new ProfilesCommand();
        itemsCommand = new ItemsCommand(this);
        stealsCommand = new StealsCommand(this);
        adminCommand = new AdminCommand(this);
        bypassCommand = new BypassCommand(this);
        exemptCommand = new ExemptCommand(this);

        new InventoryClick(this);
        new InventoryClose(this);
        new PlayerInteract(this);
        new PlayerJoin(this);

        configuration = new PickpocketConfiguration(PLUGIN_FOLDER_DIRECTORY, "config.yml");
        configuration.setup();

        scheduler = server.getScheduler();

        scheduler.runTaskTimerAsynchronously(this, new Runnable() {
            public void run() {
                for (Player player : cooldowns.keySet()) {
                    cooldowns.put(player, cooldowns.get(player) - 1);
                    if (cooldowns.get(player) <= 0) cooldowns.remove(player);
                }
            }
        }, 20, 20);

        logger.info(getName() + " enabled.");
    }

    public void onDisable() {
        logger.info(getName() + " disabled.");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("pickpocket")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.DARK_GRAY + NAME + " " + getDescription().getVersion());
                sender.sendMessage(ChatColor.GRAY + "Type '/pickpocket profiles' to see a list of loaded profiles.");
                sender.sendMessage(ChatColor.GRAY + "Type '/pickpocket items' to see a list of your pickpocket items.");
                sender.sendMessage(ChatColor.GRAY + "Type '/pickpocket steals' to check how many times you've stolen.");
                sender.sendMessage(ChatColor.GRAY + "Type '/pickpocket admin' to receive admin notifications.");
                sender.sendMessage(ChatColor.GRAY + "Type '/pickpocket exempt [name]' to exempt yourself from being stolen from.");
                sender.sendMessage(ChatColor.GRAY + "Type '/pickpocket bypass [name]' to toggle cooldown bypass.");
                sender.sendMessage(ChatColor.DARK_GRAY + "Developer Area");
                sender.sendMessage(ChatColor.GRAY + "/pickpocket giverandom <amount>");
                sender.sendMessage(ChatColor.GRAY + "/pickpocket printkeys");
            }
            else if (args[0].equalsIgnoreCase("profiles")) {
                profilesCommand.execute(player, profiles);
            }
            else if (args[0].equalsIgnoreCase("items")) {
                itemsCommand.execute(player, profiles);
            }
            else if (args[0].equalsIgnoreCase("steals")) {
                stealsCommand.execute(player, profiles);
            }
            else if (args[0].equalsIgnoreCase("giverandom") && player.hasPermission(PICKPOCKET_DEVELOPER)) {
                PickpocketItem[] items = PickpocketItem.values();
                for (int i = 0; i < Integer.valueOf(args[1]); i++) {
                    Profile profile = Profiles.get(player, profiles, this);
                    profile.givePickpocketItem(items[new Random().nextInt(items.length)]);
                }
            }
            else if (args[0].equalsIgnoreCase("admin") && player.hasPermission(PICKPOCKET_ADMIN)) {
                adminCommand.execute(player, profiles, args);
            }
            else if (args[0].equalsIgnoreCase("exempt") && player.hasPermission(PICKPOCKET_EXEMPT)) {
                if (args.length > 1)
                    exemptCommand.execute(player, profiles, args[1]);
                else exemptCommand.execute(player, profiles);
            }
            else if (args[0].equalsIgnoreCase("bypass") && player.hasPermission(PICKPOCKET_BYPASS)) {
                if (args.length > 1)
                    bypassCommand.execute(player, profiles, args[1]);
                else bypassCommand.execute(player, profiles);
            }
            else if (args[0].equalsIgnoreCase("printkeys") && player.hasPermission(PICKPOCKET_DEVELOPER)) {
                configuration.printKeys(player);
            }
        }

        return true;
    }

    public static void registerInventory(UUID uuid, PickpocketItemInventory inventory) {
        if (!registeredInventories.containsKey(uuid)) {
            getInstance().getServer().getPluginManager().registerEvents(inventory, instance);
            registeredInventories.put(uuid, inventory);
        }
    }
    
    public static void addProfile(Profile profile) {
        profiles.add(profile);
    }

    public static Vector<Profile> getProfiles() {
        return profiles;
    }

    public void addCooldown(Player player) {
        cooldowns.put(player, cooldownDelay);
    }

    public Map<Player, Integer> getCooldowns() {
        return cooldowns;
    }
    
    public static Pickpocket getInstance() {
        return instance;
    }
}
