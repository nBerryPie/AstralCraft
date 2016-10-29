package com.nagisberry.astralitems.item

import com.nagisberry.astralitems.ItemTypes
import com.nagisberry.astralitems.element.Elements
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
        private val elements: Map<Elements, IElement>
) {

    fun getDefaultMetadata() = mutableMapOf("MAIN" to mapOf("rarity" to rarity as Any)).apply {
        elements.mapKeys { it.key.name }
                .mapValues { it.value.getDefaultMetadata() }
                .filter { it.value.isNotEmpty() }
                .let { putAll(it) }
    }
}