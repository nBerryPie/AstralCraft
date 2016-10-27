package com.nagisberry.astralcore.event

import io.netty.channel.ChannelHandlerContext
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.player.PlayerEvent

abstract class PacketEvent(player: Player, val context: ChannelHandlerContext, val message: Any): PlayerEvent(player), Cancellable {

    private var cancel = false

    override fun isCancelled() = cancel

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }
}