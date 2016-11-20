package com.nagisberry.astralcore.packet

import com.mojang.authlib.GameProfile
import com.nagisberry.astralcore.event.PacketReadEvent
import com.nagisberry.astralcore.event.PacketWriteEvent
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.server.v1_10_R1.PacketLoginInStart
import net.minecraft.server.v1_10_R1.PacketLoginOutSuccess
import org.bukkit.Bukkit
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class ServerChannelHandler: ChannelDuplexHandler() {

    private val PROPERTY_PACKET_LOGIN_OUT_SUCCESS_A = PacketLoginOutSuccess::class.declaredMemberProperties.first {
        it.name == "a"
    }.apply { isAccessible = true }

    var profile: GameProfile? = null
        private set

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is PacketLoginInStart) {
            profile = msg.a()
        }
        PacketReadEvent(profile, ctx, msg).let {
            Bukkit.getServer().pluginManager.callEvent(it)
            if (!it.cancel) {
                super.channelRead(ctx, msg)
            }
        }
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        if (msg is PacketLoginOutSuccess) {
            profile = PROPERTY_PACKET_LOGIN_OUT_SUCCESS_A.get(msg) as GameProfile
        }
        PacketWriteEvent(profile, ctx, msg, promise).let {
            Bukkit.getServer().pluginManager.callEvent(it)
            if(!it.cancel) {
                super.write(ctx, msg, promise)
            }
        }
    }
}