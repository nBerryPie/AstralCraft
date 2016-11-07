package com.nagisberry.astralitems.item

import com.nagisberry.astralcore.AstralCore
import com.nagisberry.astralitems.AstralItems
import com.nagisberry.astralitems.ItemTypes
import com.nagisberry.astralitems.Rarity
import com.nagisberry.astralitems.element.Elements
import com.nagisberry.astralitems.element.IElement
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class ItemData(
        val id: String,
        val material: Material,
        val damage: Short,
        val name: String,
        val description: List<String>,
        val rarity: Int,
        val types: List<ItemTypes>,
        private val elements: Map<Elements, IElement>
) {

    fun getDisplayName(rarityNum: Int?) ="${Rarity[rarityNum ?: rarity].color}$name"

    fun hasElement(element: Elements) = elements.containsKey(element)

    fun getElement(element: Elements) = elements[element]

    fun createItemStack(amount: Int, metadata: Map<String, Map<String, Any>>) = ItemStack(Material.STICK, amount).apply {
        itemMeta = itemMeta.apply {
            displayName = id
            lore = metadata.map { "${it.key} ${AstralCore.gson.toJson(it.value)}" }
        }
    }

    fun getDefaultMetadata() = mutableMapOf("MAIN" to mapOf("rarity" to rarity as Any)).apply {
        elements.mapKeys { it.key.name }
                .mapValues { it.value.getDefaultMetadata() }
                .filter { it.value.isNotEmpty() }
                .let { putAll(it) }
    }

    fun getDisplayItem(amount: Int, metadata: Map<String, Map<String, Any>>) = ItemStack(material, amount, damage).apply {
        itemMeta = itemMeta.apply {
            displayName = getDisplayName((metadata["MAIN"]?.get("rarity") as? Number?)?.toInt())
            lore = listOf(
                    *(description.map { "${ChatColor.GRAY}$it" }.toTypedArray()),
                    "",
                    "Type: ${if (types.isEmpty()) "Undefined" else types.joinToString(", ")}"
            )
            spigot().isUnbreakable = true
            addItemFlags(*AstralItems.hideFlags)
        }
    }
}