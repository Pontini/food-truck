package com.pontini.food.core.mapper

interface Mapper<FROM, TO> {
    fun map(from: FROM): TO
}

fun <FROM, TO> Mapper<FROM, TO>.mapList(list: List<FROM>): List<TO> {
    return list.map { map(it) }
}