package com.nagisberry.astralcore.event

import io.netty.channel.ChannelHandlerContext
import org.bukkit.event.HandlerList

class StatusPacketReadEvent(context: ChannelHandlerContext, message: Any): StatusPacketEvent(context, message) {

    companion object {
        private val _handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = _handlers
    }

    override fun getHandlers() = _handlers
}