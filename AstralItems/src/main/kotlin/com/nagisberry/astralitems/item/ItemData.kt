package com.nagisberry.astralitems.item

import com.nagisberry.astralitems.ItemManager
import com.nagisberry.astralitems.ItemTypes
import com.nagisberry.astralitems.element.Elements
import com.nagisberry.astralitems.element.IElement
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

    fun hasElement(element: Elements) = elements.containsKey(element)

    fun getElement(element: Elements) = elements[element]

    fun createItemStack(amount: Int, metadata: Map<String, Map<String, Any>>) = ItemStack(Material.STICK, amount).apply {
        itemMeta = itemMeta.apply {
            displayName = id
            lore = getDefaultMetadata().apply { putAll(metadata) }
                    .map { "${it.key} ${ItemManager.gson.toJson(it.value)}" }
        }
    }

    fun getDefaultMetadata() = mutableMapOf("MAIN" to mapOf("rarity" to rarity as Any)).apply {
        elements.mapKeys { it.key.name }
                .mapValues { it.value.getDefaultMetadata() }
                .filter { it.value.isNotEmpty() }
                .let { putAll(it) }
    }
}