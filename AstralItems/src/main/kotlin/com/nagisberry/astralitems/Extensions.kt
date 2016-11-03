package com.nagisberry.astralitems

import com.google.gson.Gson
import com.nagisberry.astralitems.element.Elements
import com.nagisberry.astralitems.item.ItemData
import org.bukkit.inventory.ItemStack
import java.io.Reader

val ItemStack.itemData: ItemData?
    get() = ItemManager[this]

fun ItemStack.getItemMetadata(category: String) = itemMeta.lore
        .map { it.substringBefore(" ") to it.substringAfter(" ") }
        .toMap()[category]?.let { ItemManager.gson.fromJson<Map<String, Any>>(it) }?.let {
    if (category == "MAIN") {
        itemData?.let { mapOf("rarity" to it.rarity) }
    } else {
        Elements[category]?.let { itemData?.getElement(it) }?.getDefaultMetadata()
    } to it
}?.let { (it.first ?: emptyMap()) + it.second }

fun ItemStack.setItemMetadata(category: String, metadata: Map<String, Any>) {
    val s = "${category.toUpperCase()} ${ItemManager.gson.toJson(metadata)}"
    itemMeta = itemMeta.apply {
        lore.mapIndexed { index, str ->
            index to str
        }.filter {
            it.second.startsWith("$category ", true)
        }.map { it.first }.firstOrNull()?.let {
            lore = lore.apply { set(it, s) }
        } ?: run {
            lore = lore.apply { add(s) }
        }
    }
}

fun ItemStack.removeItemMetadata(category: String) {
    itemMeta = itemMeta.apply {
        lore = lore.filter { it.startsWith("$category ", true) }
    }
}

fun ItemStack.toSimpleItemStack() = SimpleItemStack(type, durability)

inline fun <reified T: Any> Gson.fromJson(json: Reader): T = fromJson(json, T::class.java)

inline fun <reified T: Any> Gson.fromJson(json: String): T = fromJson(json, T::class.java)