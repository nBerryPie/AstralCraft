package com.nagisberry.astralcore.event

import com.mojang.authlib.GameProfile
import io.netty.channel.ChannelHandlerContext
import org.bukkit.event.Event

abstract class PacketEvent(val profile: GameProfile?, val context: ChannelHandlerContext, val message: Any): Event() {

    var cancel = false
}