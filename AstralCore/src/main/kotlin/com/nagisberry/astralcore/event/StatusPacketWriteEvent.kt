package com.nagisberry.astralcore.event

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.bukkit.event.HandlerList

class StatusPacketWriteEvent(context: ChannelHandlerContext, message: Any, var promise: ChannelPromise): StatusPacketEvent(context, message) {

    companion object {
        private val _handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = _handlers
    }

    override fun getHandlers() = _handlers
}