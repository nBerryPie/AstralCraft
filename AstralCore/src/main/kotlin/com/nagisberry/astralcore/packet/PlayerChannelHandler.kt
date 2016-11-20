package com.nagisberry.astralcore.packet

import com.nagisberry.astralcore.event.PacketReadEvent
import com.nagisberry.astralcore.event.PacketWriteEvent
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerChannelHandler(private val player: Player): ChannelDuplexHandler() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        PacketReadEvent(player, ctx, msg).let {
            Bukkit.getServer().pluginManager.callEvent(it)
            if (!it.cancel) {
                super.channelRead(ctx, msg)
            }
        }
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        PacketWriteEvent(player, ctx, msg, promise).let {
            Bukkit.getServer().pluginManager.callEvent(it)
            if (!it.cancel) {
                super.write(ctx, msg, promise)
            }
        }
    }
}