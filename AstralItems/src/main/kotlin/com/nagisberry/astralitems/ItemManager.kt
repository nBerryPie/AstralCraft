package com.nagisberry.astralitems

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.bukkit.Bukkit
import org.bukkit.Material
import java.io.FileReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object ItemManager {

    private val ID_VANILLA = "vanilla_item"

    private val gson = Gson()
    private val logger = Bukkit.getLogger()

    fun loadFiles(dir: Path, isVanilla: Boolean = false) {
        dir.forEach { path ->
            if (Files.isDirectory(path)) {
                loadFiles(path, isVanilla || checkVanillaFile(path))
            } else if (path.toString().endsWith(".json", true)) {
                try {
                    FileReader(path.toFile()).use { reader ->
                        gson.fromJson(reader, JsonArray::class.java).forEach { json ->
                            if (json is JsonObject) {
                                loadItemData(json, isVanilla || checkVanillaFile(path))
                            } else {
                                logger.warning("this is not JsonObject: $json")
                            }
                        }
                    }
                } catch (e: IOException) {

                }
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
        //ToDo: Elementの読み込み
        //ToDo: ItemDataInstanceの作成
        //ToDo: Mapに突っ込む
    }

    fun checkVanillaFile(path: Path) = path.fileName.toString().startsWith("vanilla", true)
}