package com.nagisberry.astralitems

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.nagisberry.astralcore.AstralCore
import com.nagisberry.astralcore.AstralCore.Companion.fromJson
import com.nagisberry.astralitems.element.Elements
import com.nagisberry.astralitems.item.ItemData
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object ItemManager {

    private val ID_VANILLA = "vanilla_item"

    private val logger = Bukkit.getLogger()

    private val vanillaItems = HashMap<SimpleItemStack, ItemData>()
    private val items = HashMap<String, ItemData>()

    operator fun get(id: String) = items[id]

    operator fun get(stack: SimpleItemStack) = vanillaItems[stack]

    operator fun get(stack: ItemStack) = stack.itemMeta?.displayName?.let {
        get(it)
    } ?: get(stack.toSimpleItemStack())

    fun reloadFiles(dir: Path) {
        vanillaItems.clear()
        items.clear()
        loadFiles(dir)
    }

    fun loadFiles(dir: Path) {
        Files.walk(dir).filter {
            it.toString().endsWith(".json", true)
        }.forEach { path ->
            println(path)
            try {
                path.toFile().reader().use { reader ->
                    AstralCore.gson.fromJson<JsonArray>(reader).forEach { json ->
                        if (json is JsonObject) {
                            loadItemData(json, checkVanillaFile(path))
                        } else {
                            logger.warning("this is not JsonObject: $json")
                        }
                    }
                }
            } catch (e: IOException) {
                logger.warning("Load failure: " + path)
                e.printStackTrace()
            }
        }
    }

    fun loadItemData(json: JsonObject, isVanilla: Boolean) {
        val id = if (isVanilla) ID_VANILLA else json["id"]?.asString ?: run {
            logger.warning("id is not found: $json")
            return
        }
        val material = json["material"]?.asString?.let { Material.getMaterial(it) } ?: run {
            logger.warning("material is not found: ${ if (isVanilla) json.toString() else id }")
            return
        }
        val damage = json["damage"]?.asShort ?: 0
        val name = json["name"]?.asString ?: run {
            logger.warning("name is not found: ${ if (isVanilla) "${material.name}:$damage" else id }")
            return
        }
        val description = json["description"]?.asJsonArray
                ?.map(JsonElement::getAsString)
                ?.filter { it != null } ?: emptyList()
        val rarity = json["rarity"]?.asInt ?: 0
        val types = json["types"]?.asJsonArray
                ?.map(JsonElement::getAsString)
                ?.map { ItemTypes[it] }
                ?.filterNotNull() ?: emptyList()
        val elements = Elements.values().map {
            json[it.name.toLowerCase()]?.asJsonObject?.let { json -> it to json }
        }.filterNotNull().map { it.first to it.first(it.second) }.toMap()
        val data = ItemData(id, material, damage, name, description, rarity, types, elements)
        if (isVanilla) {
            vanillaItems.put(SimpleItemStack(material, damage), data)
        } else {
            items.put(id, data)
        }
    }

    fun checkVanillaFile(path: Path) = path.any { it.fileName.toString().startsWith("vanilla", true) }
}