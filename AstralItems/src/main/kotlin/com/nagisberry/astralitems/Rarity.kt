package com.nagisberry.astralitems

import net.md_5.bungee.api.ChatColor

enum class Rarity(val color: ChatColor) {
    JUNK(ChatColor.DARK_GRAY),
    COMMON(ChatColor.WHITE),
    UNCOMMON(ChatColor.GREEN),
    RARE(ChatColor.AQUA),
    EPIC(ChatColor.RED),
    LEGENDARY(ChatColor.GOLD),
    MYTHIC(ChatColor.DARK_PURPLE);

    companion object {
        operator fun get(num: Int) = when {
            num < 0 -> JUNK
            num < 3 -> COMMON
            num < 6 -> UNCOMMON
            num < 9 -> RARE
            num < 12 -> EPIC
            num < 15 -> LEGENDARY
            else -> MYTHIC
        }
    }
}