package com.nagisberry.astralcore.event

import io.netty.channel.ChannelHandlerContext
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

class PacketReadEvent(player: Player, context: ChannelHandlerContext, message: Any): PacketEvent(player, context, message) {

    companion object {
        private val _handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = _handlers
    }

    override fun getHandlers() = _handlers
}