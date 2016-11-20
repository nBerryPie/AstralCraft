package com.nagisberry.astralcore.packet

import io.netty.channel.*
import net.minecraft.server.v1_10_R1.Packet
import net.minecraft.server.v1_10_R1.ServerConnection
import org.bukkit.Server
import org.bukkit.craftbukkit.v1_10_R1.CraftServer
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

object PacketManager {

    private val HANDLER_NAME = "astral_craft"
    private val PROPERTY_SERVER_CONNECTION_G = ServerConnection::class.declaredMemberProperties.first {
        it.name == "g"
    }.apply { isAccessible = true }

    fun sendPacket(player: Player, packet: Packet<*>) {
        getPlayerConnection(player).sendPacket(packet)
    }

    fun inject(player: Player) {
        getChannelPipeline(player).let {
            if (it[HANDLER_NAME] == null) {
                it.addBefore(
                        "packet_handler",
                        HANDLER_NAME,
                        PlayerChannelHandler(player)
                )
            }
        }
    }

    fun inject(server: Server) {
        (server as CraftServer).server.serverConnection.let(PROPERTY_SERVER_CONNECTION_G).let {
            it as List<*>
        }.map {
            it as ChannelFuture
        }.forEach {
            it.channel().pipeline().let {
                if (it[HANDLER_NAME] == null) {
                    it.addFirst(HANDLER_NAME, createServerChannelHandler())
                }
            }
        }
    }

    private fun getChannelPipeline(player: Player) =
            getPlayerConnection(player).networkManager.channel.pipeline()

    private fun getPlayerConnection(player: Player) =
            (player as  CraftPlayer).handle.playerConnection

    private fun createServerChannelHandler(): ChannelHandler {
        class A: ChannelInitializer<Channel>() {
            override fun initChannel(channel: Channel) {
                channel.pipeline().let {
                    if (it[HANDLER_NAME] == null) {
                        it.addBefore(
                                "packet_handler",
                                HANDLER_NAME,
                                ServerChannelHandler()
                        )
                    }
                }
            }
        }

        class B: ChannelInitializer<Channel>() {
            override fun initChannel(channel: Channel) {
                channel.pipeline().addLast(A())
            }

        }

        class C: ChannelInboundHandlerAdapter() {
            override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                (msg as Channel).pipeline().addFirst(B())
                super.channelRead(ctx, msg)
            }
        }

        return C()
    }
}