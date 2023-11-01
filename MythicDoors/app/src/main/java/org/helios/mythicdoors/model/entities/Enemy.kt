package org.helios.mythicdoors.model.entities

data class Enemy(
    private val id: Long?,
    private val name: String,
    private val level: Int,
    private val coinReward: Int,
    private val image: String
    // TODO: Add fields
) {
    fun getId(): Long? { return id }
    fun getName(): String { return name }
    fun getLevel(): Int { return level }
    fun getCoinReward(): Int { return coins }
    fun getImage(): String { return image }
}
