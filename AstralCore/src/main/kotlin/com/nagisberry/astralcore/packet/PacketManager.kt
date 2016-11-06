package com.nagisberry.astralcore.packet

import net.minecraft.server.v1_10_R1.Packet
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer
import org.bukkit.entity.Player

object PacketManager {

    private val HANDLER_NAME = "astral_craft"

    fun sendPacket(player: Player, packet: Packet<*>) {
        getPlayerConnection(player).sendPacket(packet)
    }

    fun inject(player: Player) {
        getChannelPipeline(player).let {
            if (it[HANDLER_NAME] == null) {
                it.addBefore(
                        "packet_handler",
                        HANDLER_NAME,
                        PlayerChannelHandler(player)
                )
            }
        }
    }

    private fun getChannelPipeline(player: Player) =
            getPlayerConnection(player).networkManager.channel.pipeline()

    private fun getPlayerConnection(player: Player) =
            (player as  CraftPlayer).handle.playerConnection
}