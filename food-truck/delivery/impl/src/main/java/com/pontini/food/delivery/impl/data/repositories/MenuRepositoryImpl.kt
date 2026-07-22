package com.pontini.food.delivery.impl.data.repositories

import com.pontini.food.delivery.domain.models.MenuItem
import com.pontini.food.delivery.domain.repositories.MenuRepository
import kotlinx.coroutines.delay

private val SIMULATED_MENU = listOf(
    MenuItem(
        id = "1",
        name = "X-Burger",
        description = "Hambúrguer, queijo, alface e tomate",
        price = 24.90,
        emoji = "🍔"
    ),
    MenuItem(
        id = "2",
        name = "Batata Frita",
        description = "Porção de batata frita crocante",
        price = 14.90,
        emoji = "🍟"
    ),
    MenuItem(
        id = "3",
        name = "Hot Dog",
        description = "Pão, salsicha, molho e batata palha",
        price = 18.90,
        emoji = "🌭"
    ),
    MenuItem(
        id = "4",
        name = "Refrigerante",
        description = "Lata 350ml",
        price = 6.90,
        emoji = "🥤"
    ),
    MenuItem(
        id = "5",
        name = "Pastel de Carne",
        description = "Pastel frito recheado com carne",
        price = 12.90,
        emoji = "🥟"
    ),
    MenuItem(
        id = "6",
        name = "Milkshake",
        description = "Milkshake de chocolate 400ml",
        price = 16.90,
        emoji = "🥛"
    )
)

class MenuRepositoryImpl : MenuRepository {

    override suspend fun getMenu(): List<MenuItem> {
        delay(400) // simula latência de carregamento do cardápio

        return SIMULATED_MENU
    }
}
