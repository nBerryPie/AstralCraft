package com.nagisberry.astralcore.event

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

class PacketWriteEvent(player: Player, context: ChannelHandlerContext, message: Any, var promise: ChannelPromise): PacketEvent(player, context, message) {

    companion object {
        private val _handlers: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = _handlers
    }

    override fun getHandlers() = _handlers
}