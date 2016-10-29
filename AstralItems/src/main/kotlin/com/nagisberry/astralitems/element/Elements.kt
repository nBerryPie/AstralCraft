package com.nagisberry.astralitems.element

import com.google.gson.JsonObject

enum class Elements(private val f: (JsonObject) -> IElement) {

    DAMAGEABLE({ ElementDamageable(it["base_durability"]?.asInt ?: 1) });

    operator fun invoke(json: JsonObject)  = f(json)
}