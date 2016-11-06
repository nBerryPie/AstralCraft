package com.nagisberry.astralcore.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.SimplePluginManager
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

object CommandManager {

    private val commandMap = SimplePluginManager::class.declaredMemberProperties
            .first { it.name == "commandMap" }
            .apply { isAccessible = true }
            .call(Bukkit.getPluginManager())
            .let { it as CommandMap }

    private val constructorCommand = PluginCommand::class.constructors
            .first()
            .apply { isAccessible = true }

    fun registerCommand(name: String, plugin: Plugin) {
        constructorCommand.call(name, plugin).let {
            commandMap.register(plugin.name, it)
        }
    }
}