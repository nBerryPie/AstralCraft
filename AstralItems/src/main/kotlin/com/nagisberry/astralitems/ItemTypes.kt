package com.nagisberry.astralitems

enum class ItemTypes(val displayName: String) {
    MATERIAL("Material"),
    WEAPON("Weapon"),
    TOOL("Tool"),
    FOOD("Food"),
    BLOCK("Block"),
    ARMOR("Armor");

    companion object {
        operator fun get(name: String): ItemTypes? = try {
            ItemTypes.valueOf(name)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}