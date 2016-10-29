package com.nagisberry.astralitems.element

interface IElement {

    val type: Elements

    fun getDefaultMetadata(): Map<String, Any>
}