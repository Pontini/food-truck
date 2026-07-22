package com.pontini.food.delivery.domain.repositories

import com.pontini.food.delivery.domain.models.MenuItem

interface MenuRepository {
    suspend fun getMenu(): List<MenuItem>
}
