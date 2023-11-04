data class Enemy(
    private val id: Long?,
    private val name: String,
    private val level: Int,
    private val coinReward: Int,
    private var image: String?
    // TODO: Add fields
) {
    fun getId(): Long? { return id }
    fun getName(): String { return name }
    fun getLevel(): Int { return level }
    fun getCoinReward(): Int { return coinReward }
    fun getImage(): String? { return image }

    fun setImage(image: String) { this.image = image }

    /*

Implementación del patrón Fabric*/
    companion object {
        fun create(
            name: String,
            level: Int,
<<<<<<< HEAD
            coinReward: Int,): Enemy {
            return Enemy(null, name, level, coinReward, null)}}
=======
            coinReward: Int,
        ): Enemy {
            return Enemy(null, name, level, coinReward, null)
        }
    }
>>>>>>> edf26e884813e5bcba28d745924d3f2e2dd1a727

    fun isEmpty(): Boolean {
        return id == null
    }
}
