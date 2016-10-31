package com.nagisberry.astralitems

import com.nagisberry.astralcore.command.CommandManager
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
}