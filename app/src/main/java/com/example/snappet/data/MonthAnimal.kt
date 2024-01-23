package com.example.snappet.data

import java.time.LocalDate

class MonthAnimal{
    private val monthlyAnimals = listOf(
        "Horse", "Lizard", "Peacock", "Pig", "Rabbit", "Sheep",
        "Bird", "Cat", "Chicken", "Cow", "Dog", "Duck"
    )
    fun getAnimalForPresentMonth(): String {
        val currentMonth = LocalDate.now().month.value
        return monthlyAnimals[currentMonth - 1]
    }
}