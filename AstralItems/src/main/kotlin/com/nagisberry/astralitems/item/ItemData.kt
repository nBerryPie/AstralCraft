package com.nagisberry.astralitems.item

import com.nagisberry.astralitems.ItemTypes
import com.nagisberry.astralitems.element.IElement
import org.bukkit.Material

data class ItemData(
        val id: String,
        val material: Material,
        val damage: Short,
        val name: String,
        val description: List<String>,
        val rarity: Int,
        val types: List<ItemTypes>,
        private val elements: List<IElement>
) {

    fun getDefaultMetadata() = mutableMapOf("MAIN" to mapOf("rarity" to rarity as Any)).apply {
        elements.map { it.type.name to it.getDefaultMetadata() }
                .toMap()
                .filter { it.value.isNotEmpty() }
                .let { putAll(it) }
    }
}