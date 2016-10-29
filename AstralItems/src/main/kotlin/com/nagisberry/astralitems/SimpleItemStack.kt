package com.nagisberry.astralitems

import org.bukkit.Material

data class SimpleItemStack(val material: Material, val damage: Short = 0, val amount: Int = 1) {}