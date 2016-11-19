package com.nagisberry.astralcore.packet

import com.nagisberry.astralcore.event.PacketReadEvent
import com.nagisberry.astralcore.event.PacketWriteEvent
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerChannelHandler(private val player: Player): ChannelDuplexHandler() {

    override fun channelRead(content: ChannelHandlerContext, message: Any) {
        PacketReadEvent(player, content, message).let {
            Bukkit.getServer().pluginManager.callEvent(it)
            if (!it.cancel) {
                super.channelRead(content, message)
            }
        }
    }

    override fun write(content: ChannelHandlerContext, message: Any, promise: ChannelPromise) {
        PacketWriteEvent(player, content, message, promise).let {
            Bukkit.getServer().pluginManager.callEvent(it)
            if (!it.cancel) {
                super.write(content, message, promise)
            }
        }
    }
}