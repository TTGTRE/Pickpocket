package logan.config;

import logan.pickpocket.main.PickpocketPlugin;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class PickpocketConfiguration {

    private static final String minigameRollRateKey = "minigame-roll-rate";
    private static final String cooldownTimeKey = "cooldown-time";
    private static final String pickpocketToggleKey = "allow-pickpocket-toggling";
    private static final String statusOnInteractKey = "show-status-on-interact";
    private static final String statusOnLoginKey = "show-status-on-login";
    private static final String disabledItemsKey = "disabled-items";

    private static CommentedConfiguration config;

    public static void init() {
        config = new CommentedConfiguration(new File(PickpocketPlugin.getInstance().getDataFolder(), "config.yml"));

        config.createKeyIfNoneExists(minigameRollRateKey, 20);
        config.createKeyIfNoneExists(cooldownTimeKey, 10);
        config.createKeyIfNoneExists(pickpocketToggleKey, true);
        config.createKeyIfNoneExists(statusOnInteractKey, true);
        config.createKeyIfNoneExists(statusOnLoginKey, true);
        config.createKeyIfNoneExists(disabledItemsKey, Collections.singletonList("cake"));

        config.addCommentToKey(minigameRollRateKey, "The time in ticks a user has before the", "mini-game inventory slots are randomized again.");
        config.addCommentToKey(cooldownTimeKey, "The time the player must wait in seconds", "between pick-pocketing attempts.", "An attempt is when a player successfully", "pick-pockets another player.");
        config.addCommentToKey(pickpocketToggleKey, "Allow players to disable pick-pocketing", "for themselves. This will also disallow others", "from pick-pocketing them.");
        config.addCommentToKey(statusOnInteractKey, "Whether or not to show a players the", "pick-pocket status message when they attempt", "to pick-pocket another player whilst they, or the", "victim has pick-pocketing disabled.");
        config.addCommentToKey(statusOnLoginKey, "Whether or not to show a players pick-pocket status when logging in.");
        config.addCommentToKey(disabledItemsKey, "Items that can't be stolen and therefore, won't show", "up in the rummage GUI. A list of Minecraft IDs can be found", "at www.deadmap.com/idlist");

        config.save();
    }

    public static int getMinigameRollRate() {
        return config.getConfiguration().getInt(minigameRollRateKey);
    }

    public static List<String> getDisabledItems() {
        return config.getConfiguration().getStringList(disabledItemsKey);
    }

    public static boolean isShowStatusOnInteractEnabled() {
        return config.getConfiguration().getBoolean(statusOnInteractKey);
    }

    public static boolean isShowStatusOnLoginEnabled() {
        return config.getConfiguration().getBoolean(statusOnLoginKey);
    }

    public static int getCooldownTime() {
        return config.getConfiguration().getInt(cooldownTimeKey);
    }

    public static void reloadConfiguration() {
        config.reload();
    }
}
