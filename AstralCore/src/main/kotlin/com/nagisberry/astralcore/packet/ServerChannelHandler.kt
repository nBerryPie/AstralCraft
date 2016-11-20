package com.nagisberry.astralcore.packet

import com.nagisberry.astralcore.event.StatusPacketReadEvent
import com.nagisberry.astralcore.event.StatusPacketWriteEvent
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.bukkit.Bukkit

class ServerChannelHandler: ChannelDuplexHandler() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        StatusPacketReadEvent(ctx, msg).let {
            Bukkit.getServer().pluginManager.callEvent(it)
            if (!it.cancel) {
                super.channelRead(ctx, msg)
            }
        }
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        StatusPacketWriteEvent(ctx, msg, promise).let {
            Bukkit.getServer().pluginManager.callEvent(it)
            if(!it.cancel) {
                super.write(ctx, msg, promise)
            }
        }
    }
}