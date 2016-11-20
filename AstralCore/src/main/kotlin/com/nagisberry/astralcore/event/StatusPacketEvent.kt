package com.nagisberry.astralcore.event

import io.netty.channel.ChannelHandlerContext
import org.bukkit.event.Event

abstract class StatusPacketEvent(val context: ChannelHandlerContext, val message: Any): Event() {

    var cancel = false
}