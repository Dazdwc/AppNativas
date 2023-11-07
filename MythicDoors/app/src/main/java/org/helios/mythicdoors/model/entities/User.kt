package org.helios.mythicdoors.model.entities

import java.time.LocalDate

data class User(
    private val id: Long?,
    private val name: String,
    private val email: String,
    private val password: String,
    private var score: Int,
    private var level: Int,
    private var experience: Int,
    private var coins: Int,
    private var goldCoins: Int,
    private var isActive: Boolean,
    private val createdAt: LocalDate
) {
    fun getId(): Long? { return id }
    fun getName(): String { return name }
    fun getEmail(): String { return email }
    fun getPassword(): String { return password }
    fun getScore(): Int { return score }
    fun getLevel(): Int { return level }
    fun getExperience(): Int { return experience }
    fun getCoins(): Int { return coins }
    fun getGoldCoins(): Int { return goldCoins }
    fun getIsActive(): Boolean { return isActive }
    fun getCreatedAt(): LocalDate { return createdAt }

    fun setScore(score: Int) { this.score = score }
    fun setLevel(level: Int) { this.level = level }
    fun setExperience(experience: Int) { this.experience = experience }
    fun setCoins(coins: Int) { this.coins = coins }
    fun setGoldCoins(goldCoins: Int) { this.goldCoins = goldCoins }
    fun setIsActive(isActive: Boolean) { this.isActive = isActive }

    /* Implementación del patrón Fabric */
    companion object {
        fun createEmptyUser(): User {
            return User(
                null,
                "",
                "",
                "",
                0,
                0,
                0,
                0,
                0,
                true,
                createdAt = LocalDate.now())
        }

        fun create(
            name: String,
            email: String,
            password: String,
        ): User {
            return User(
                null,
                name,
                email,
                password,
                0,
                0,
                0,
                0,
                0,
                true,
                createdAt = LocalDate.now())
        }
    }

    fun isEmpty(): Boolean { return this.id == null }
    fun isValid(): Boolean {
        return this.name.isNotEmpty() && this.email.isNotEmpty() && this.password.isNotEmpty() && this.score >= 0 && this.level >= 0 && this.experience >= 0 && this.coins >= 0 && this.goldCoins >= 0
    }
}