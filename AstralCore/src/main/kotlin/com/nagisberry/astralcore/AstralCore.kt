package com.nagisberry.astralcore

import com.nagisberry.astralcore.packet.PacketManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class AstralCore: JavaPlugin(), Listener {

    override fun onEnable() {
        server.onlinePlayers.forEach { PacketManager.inject(it) }
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    override fun onDisable() {
        server.onlinePlayers.forEach { PacketManager.remove(it) }
    }

    @EventHandler
    fun onPlayerJoin(evt: PlayerJoinEvent) {
        PacketManager.inject(evt.player)
    }

    @EventHandler
    fun onPlayerQuit(evt: PlayerQuitEvent) {
        PacketManager.remove(evt.player)
    }
}