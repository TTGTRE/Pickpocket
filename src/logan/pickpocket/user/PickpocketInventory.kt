package logan.pickpocket.user

import logan.api.gui.Menu
import logan.api.gui.MenuItem
import org.bukkit.Material

class PickpocketInventory(private val victim: PickpocketUser) {
    private val INVENTORY_TITLE = "Pick-pocketing..."
    private val RUMMAGE_BUTTON_TEXT = "Rummage..."
    private val gui = Menu(INVENTORY_TITLE, 6)

    fun show(predator: PickpocketUser) {
        val victimPlayer = victim.bukkitPlayer!!
        // Find 10 items in the victims inventory and put them into the pick-pocketing
        // inventory. These items can be null (air).
        for (i in 0 until 10) {
            val randomSlot = (Math.random() * victimPlayer.inventory.size).toInt()
            val item = victimPlayer.inventory.getItem(randomSlot).also { it?.amount = 10 } ?: continue
            val menuItem = MenuItem(item).also {
                it.addListener { clickEvent ->
                    if (clickEvent.inventoryClickEvent.currentItem?.type == Material.GREEN_STAINED_GLASS_PANE
                        || clickEvent.inventoryClickEvent.currentItem?.type == Material.RED_STAINED_GLASS_PANE
                    ) {
                        return@addListener
                    }
                    item.amount -= 1
                    if (item.amount <= 0) {
                        predator.bukkitPlayer!!.inventory.addItem(item.also { stoleItem -> stoleItem.amount = 1 })
                    }
                    item.apply {
                        type = Material.GREEN_STAINED_GLASS_PANE
                        amount = 1
                    }
                }
            }
            gui.addItem(randomSlot, menuItem)
        }
        gui.show(predator.bukkitPlayer)
    }
}