package logan.pickpocket.user

import logan.api.gui.Menu
import logan.api.gui.MenuItem
import logan.api.gui.MenuItemClickEvent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class PickpocketInventory(private val predator: PickpocketUser, private val victim: PickpocketUser) {
    private val INVENTORY_TITLE = "Pick-pocketing..."
    private val RUMMAGE_BUTTON_TEXT = "Rummage..."
    private val gui = Menu(INVENTORY_TITLE, 6)

    fun show() {
        val victimPlayer = victim.bukkitPlayer!!
        for (i in 0 until 5) {
            for (j in 0 until 9) {

            }
        }
        // Find 10 items in the victims inventory and put them into the pick-pocketing
        // inventory. These items can be null (air).
        for (i in 0 until 10) {
            val randomSlot = (Math.random() * victimPlayer.inventory.size).toInt()
            var item = victimPlayer.inventory.getItem(randomSlot).also { it?.amount = 10 } ?: continue
            val menuItem = MenuItem(item).also {
                it.addListener(this::menuItemClickListener)
            }
            gui.addItem(randomSlot, menuItem)
        }
        gui.show(predator.bukkitPlayer)
    }

    fun menuItemClickListener(event: MenuItemClickEvent) {
        val item = event.menuItem.itemStack
        if (event.inventoryClickEvent.currentItem?.type == Material.GREEN_STAINED_GLASS_PANE
            || event.inventoryClickEvent.currentItem?.type == Material.RED_STAINED_GLASS_PANE
            || event.inventoryClickEvent.currentItem?.type == Material.YELLOW_STAINED_GLASS_PANE
        ) {
            println("Returning")
            return
        }
        gui.addItem(
            event.inventoryClickEvent.slot,
            MenuItem(ItemStack(item.type, item.amount - 1)).also { menuItem ->
                menuItem.addListener(this@PickpocketInventory::menuItemClickListener)
            })
        gui.update()
        if (item.amount <= 1) {
            predator.bukkitPlayer!!.inventory.addItem(item.also { stoleItem -> stoleItem.amount = 1 })
            gui.removeItem(event.inventoryClickEvent.slot)
            gui.update()
            println("Giving item to thief")
        }
        item.apply {
            type = Material.GREEN_STAINED_GLASS_PANE
            amount = 1
        }
        println("End of menu item listener")
    }
}