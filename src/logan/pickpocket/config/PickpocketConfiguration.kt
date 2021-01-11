package logan.pickpocket.config

import logan.api.config.CommentedConfiguration
import logan.pickpocket.main.PickpocketPlugin
import java.io.File

class PickpocketConfiguration {

    init {
        config.createKeyIfNoneExists(
            MONEY_TAKE_ENABLED_KEY, true, """
                Enable the ability to steal money from other players.
            """.trimIndent()
        )
        config.createKeyIfNoneExists(
            MONEY_TAKE_PERCENTAGE_KEY, 0.10, """
            The percentage of the victim's balance that will be 
            available to the thief upon pick-pocketing.
        """.trimIndent()
        )
        config.createKeyIfNoneExists(
            FOREIGN_TOWN_THEFT_KEY, false, """
            Allow players to steal from within foreign town boundaries.
        """.trimIndent()
        )
        config.createKeyIfNoneExists(
            SAME_TOWN_THEFT_KEY, false, """
            Allow players to steal from own town members.
        """.trimIndent()
        )
        config.createKeyIfNoneExists(
            PICKPOCKET_TIMER_KEY, 4, """
            The amount of time in seconds a player has to pick-pocket 
            another player before getting noticed
        """.trimIndent()
        )
        config.createKeyIfNoneExists(
            COOLDOWN_DURATION_KEY, 30
        )
        config.createKeyIfNoneExists(
            DISABLED_ITEMS_KEY, emptyArray<String>(),
            """
                Items that can't be pick-pocketed.
            """.trimIndent()
        )
    }

    companion object {
        private val config = CommentedConfiguration(File(PickpocketPlugin.getInstance().dataFolder, "config.yml"))
        private val MONEY_TAKE_ENABLED_KEY = "money-take"
        private val MONEY_TAKE_PERCENTAGE_KEY = "money-take-percentage"
        private val FOREIGN_TOWN_THEFT_KEY = "towny.foreign-town-theft"
        private val SAME_TOWN_THEFT_KEY = "towny.same-town-theft"
        private val PICKPOCKET_TIMER_KEY = "rummage-timer-delay"
        private val COOLDOWN_DURATION_KEY = "cooldown-duration"
        private val DISABLED_ITEMS_KEY = "disabled-items"
        fun isMoneyTakeEnabled() = config.configuration.getBoolean(MONEY_TAKE_ENABLED_KEY)
        fun getMoneyTakePercentage() = config.configuration.getDouble(MONEY_TAKE_PERCENTAGE_KEY)
        fun isForeignTownTheftEnabled() = config.configuration.getBoolean(FOREIGN_TOWN_THEFT_KEY)
        fun isSameTownTheftEnabled() = config.configuration.getBoolean(SAME_TOWN_THEFT_KEY)
        fun getPickpocketTimerValue() = config.configuration.getInt(PICKPOCKET_TIMER_KEY)
        fun getCooldownDuration() = config.configuration.getInt(COOLDOWN_DURATION_KEY)
        fun getDisabledItems() = config.configuration.getStringList(DISABLED_ITEMS_KEY)
        fun reload() = config.reload()
    }
}