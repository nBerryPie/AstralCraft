package com.nagisberry.astralcore

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class AstralCore: JavaPlugin(), Listener {

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }
}