package com.nagisberry.astralitems.element

data class ElementDamageable(val baseDurability: Int): IElement {

    override fun getDefaultMetadata() = mapOf("durability" to baseDurability)
}