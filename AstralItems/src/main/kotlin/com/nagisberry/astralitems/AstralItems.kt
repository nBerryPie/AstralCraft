package com.nagisberry.astralitems

import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Files

class AstralItems: JavaPlugin() {

    override fun onEnable() {
        if (!dataFolder.exists()) {
            Files.createDirectory(dataFolder.toPath())
        }
        ItemManager.loadFiles(dataFolder.toPath())
    }
}