package com.nagisberry.astralitems.element

data class ElementDamageable(val durability: Int): IElement {

    override val type = Elements.DAMAGEABLE

    override fun getDefaultMetadata() = mapOf("durability" to durability)
}