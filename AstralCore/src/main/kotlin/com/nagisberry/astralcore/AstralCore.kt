package com.nagisberry.astralcore

import com.google.gson.Gson
import com.nagisberry.astralcore.packet.PacketManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class AstralCore: JavaPlugin(), Listener {

    companion object {
        val gson = Gson()
    }

    override fun onEnable() {
        server.onlinePlayers.forEach { PacketManager.inject(it) }
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onPlayerJoin(evt: PlayerJoinEvent) {
        PacketManager.inject(evt.player)
    }
}