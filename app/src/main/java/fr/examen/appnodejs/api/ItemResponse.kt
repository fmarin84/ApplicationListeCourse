package fr.examen.appnodejs.api

import java.io.Serializable

data class ItemResponse(
    var items: List<Item>
): Serializable