package com.nagisberry.astralcore.event

import com.mojang.authlib.GameProfile
import io.netty.channel.ChannelHandlerContext
import org.bukkit.event.HandlerList

class StatusPacketReadEvent(profile: GameProfile?, context: ChannelHandlerContext, message: Any): StatusPacketEvent(profile, context, message) {

    companion object {
        private val _handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = _handlers
    }

    override fun getHandlers() = _handlers
}