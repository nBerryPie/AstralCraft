package com.nagisberry.astralitems

import com.google.gson.Gson
import com.nagisberry.astralcore.command.CommandManager
import com.nagisberry.astralitems.element.Elements
import com.nagisberry.astralitems.item.ItemData
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.Reader
import java.nio.file.Files

class AstralItems: JavaPlugin() {

    override fun onEnable() {
        CommandManager.registerCommand("items", this)
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
}

val ItemStack.itemData: ItemData?
    get() = ItemManager[this]

fun ItemStack.getItemMetadata(category: String) = itemMeta.lore
        .map { it.substringBefore(" ") to it.substringAfter(" ") }
        .toMap()[category]?.let { ItemManager.gson.fromJson<Map<String, Any>>(it) }?.let {
    if (category == "MAIN") {
        itemData?.let { mapOf("rarity" to it.rarity) }
    } else {
        Elements[category]?.let { itemData?.getElement(it) }?.getDefaultMetadata()
    } to it
}?.let { (it.first ?: emptyMap()) + it.second }

fun ItemStack.setItemMetadata(category: String, metadata: Map<String, Any>) {
    val s = "${category.toUpperCase()} ${ItemManager.gson.toJson(metadata)}"
    itemMeta = itemMeta.apply {
        lore.mapIndexed { index, str ->
            index to str
        }.filter {
            it.second.startsWith("$category ", true)
        }.map { it.first }.firstOrNull()?.let {
            lore = lore.apply { set(it, s) }
        } ?: run {
            lore = lore.apply { add(s) }
        }
    }
}

fun ItemStack.removeItemMetadata(category: String) {
    itemMeta = itemMeta.apply {
        lore = lore.filter { it.startsWith("$category ", true) }
    }
}

fun ItemStack.toSimpleItemStack() = SimpleItemStack(type, durability)

inline fun <reified T: Any> Gson.fromJson(json: Reader): T = fromJson(json, T::class.java)

inline fun <reified T: Any> Gson.fromJson(json: String): T = fromJson(json, T::class.java)