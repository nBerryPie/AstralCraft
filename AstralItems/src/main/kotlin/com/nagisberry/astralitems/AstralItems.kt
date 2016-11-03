package com.nagisberry.astralitems

import com.nagisberry.astralcore.command.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
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