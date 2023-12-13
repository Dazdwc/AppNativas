package org.helios.mythicdoors.model.entities

import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    private val name: String,
    private val email: String,
    private val password: String,
    private var score: Long,
    private var level: Long,
    private var experience: Long,
    private var coins: Long,
    private var goldCoins: Long,
    private var isActive: Boolean,
    private var createdAt: LocalDateTime
) {
    fun getName(): String { return name }
    fun getEmail(): String { return email }
    fun getPassword(): String { return password }
    fun getScore(): Long { return score }
    fun getLevel(): Long { return level }
    fun getExperience(): Long { return experience }
    fun getCoins(): Long { return coins }
    fun getGoldCoins(): Long { return goldCoins }
    fun getIsActive(): Boolean { return isActive }
    fun getCreatedAt(): LocalDateTime { return createdAt }

    fun setScore(score: Long) { this.score = score }
    fun setLevel(level: Long) { this.level = level }
    fun setExperience(experience: Long) { this.experience = experience }
    fun setCoins(coins: Long) { this.coins = coins }
    fun setGoldCoins(goldCoins: Long) { this.goldCoins = goldCoins }
    fun setIsActive(isActive: Boolean) { this.isActive = isActive }
    fun setCreatedAt(createdAt: LocalDateTime) { this.createdAt = createdAt }

    /* Implementación del patrón Fabric */
    companion object {
        fun createEmptyUser(): User {
            return User(
                "",
                "",
                "",
                0,
                0,
                0,
                0,
                0,
                true,
                createdAt = LocalDateTime.now())
        }

        fun create(
            name: String,
            email: String,
            password: String,
        ): User {
            return User(
                name,
                email,
                password,
                0,
                1,
                0,
                200,
                0,
                true,
                createdAt = LocalDateTime.now())
        }

        /* Testing function */
        fun createDummyUser(): User {
            return User(
                "Jane Doe",
                "janedoe@dummy.com",
                "1234",
                0,
                1,
                0,
                200,
                0,
                true,
                createdAt = LocalDateTime.now()
            )
        }
    }

    fun isFirestoreEmpty(): Boolean { return this.email.isNotBlank() && this.password.isNotBlank() }

    fun isValid(): Boolean {
        return this.name.isNotEmpty()
                && this.email.isNotEmpty()
                && this.password.isNotEmpty()
                && this.score >= 0
                && this.level >= 0
                && this.experience >= 0
                && this.coins >= 0
                && this.goldCoins >= 0
    }
}