package com.nagisberry.astralitems

import org.bukkit.plugin.java.JavaPlugin

class AstralItems: JavaPlugin() {

    override fun onEnable() {
        ItemManager.loadFiles(dataFolder.toPath())
    }
}