package com.nagisberry.astralitems

import com.nagisberry.astralcore.command.CommandManager
import com.nagisberry.astralcore.event.PacketWriteEvent
import com.nagisberry.astralcore.packet.PacketManager
import net.minecraft.server.v1_10_R1.ItemStack as NMSItemStack
import net.minecraft.server.v1_10_R1.PacketPlayOutSetSlot
import net.minecraft.server.v1_10_R1.PacketPlayOutWindowItems
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Files

class AstralItems: JavaPlugin(), Listener {

    override fun onEnable() {
        CommandManager.registerCommand("items", this)
        Bukkit.getPluginManager().registerEvents(this, this)
        if (!dataFolder.exists()) {
            Files.createDirectory(dataFolder.toPath())
        }
        ItemManager.loadFiles(dataFolder.toPath())
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (command.name == "items") {
            if (sender.isOp) {
                if (args.size == 0) {
                    sender.sendMessage("/$label [reload|get]")
                } else if (args[0].equals("reload", true)) {
                    ItemManager.reloadFiles(dataFolder.toPath())
                    sender.sendMessage("[AstralItems] リロードが完了しました")
                } else if (args[0].equals("get", true)) {
                    if (sender !is Player) {
                        sender.sendMessage("[AstralItems] Playerのみ実行可能なコマンドです")
                    } else if (args.size == 1) {
                        sender.sendMessage("/$label ${args[0]} <ItemID> (<amount>)")
                    } else {
                        ItemManager[args[1]]?.createItemStack(
                                if (args.size >= 2) args[2].toInt() else 1,
                                if (args.size >= 3) ItemManager.gson
                                        .fromJson<Map<String, Map<String, Any>>>(
                                                args.copyOfRange(3, args.size - 1).joinToString(" ")
                                        )
                                else emptyMap()
                        )?.let {
                            sender.inventory.addItem(it)
                        } ?: sender.sendMessage("[AstralItems] 存在しないItemIDです")
                    }
                }
                return true
            }
        }
        return false
    }

    @EventHandler
    fun onPacketWrite(evt: PacketWriteEvent) {
        if (evt.player.gameMode != GameMode.CREATIVE) {
            val message = evt.message
            if (message is PacketPlayOutWindowItems) {
                PacketPlayOutWindowItems::class.java.getDeclaredField("b")
                        .apply { isAccessible = true }
                        .let {
                            it[message] = it[message].let { it as Array<*> }
                                    .map { it as NMSItemStack? }
                                    .map(CraftItemStack::asBukkitCopy)
                                    .map { it.toDisplayItem() }
                                    .map(CraftItemStack::asNMSCopy)
                                    .toTypedArray()
                        }
            } else if (message is PacketPlayOutSetSlot) {
                PacketPlayOutSetSlot::class.java.getDeclaredField("c")
                        .apply { isAccessible = true }
                        .let {
                            it[message] = it[message]?.let { it as NMSItemStack? }
                                    ?.let(CraftItemStack::asBukkitCopy)
                                    ?.let { it.toDisplayItem() }
                                    ?.let(CraftItemStack::asNMSCopy)
                        }
            }
        }
    }

    @EventHandler
    fun onGameModeChange(evt: PlayerGameModeChangeEvent) {
        val player = evt.player
        if (evt.newGameMode == GameMode.CREATIVE) {
            player.inventory.contents.forEachIndexed { slot, stack ->
                PacketPlayOutSetSlot(
                        0,
                        getNMSSlotNumber(slot),
                        CraftItemStack.asNMSCopy(stack)
                ).let { PacketManager.sendPacket(player, it) }
            }
        } else if (player.gameMode == GameMode.CREATIVE) {
            player.inventory.contents.forEachIndexed { slot, stack ->
                PacketPlayOutSetSlot(
                        0,
                        getNMSSlotNumber(slot),
                        stack?.toDisplayItem()?.let(CraftItemStack::asNMSCopy)
                ).let { PacketManager.sendPacket(player, it) }
            }
        }
    }

    private fun getNMSSlotNumber(bukkitSlotNumber: Int): Int {
        return when (bukkitSlotNumber) {
            in 0..8 -> bukkitSlotNumber + 36
            in 9..35 -> bukkitSlotNumber
            in 80..83 -> bukkitSlotNumber - 79
            in 100..103 -> (bukkitSlotNumber - 108) * -1
            106 -> 45
            else -> -1
        }
    }
}