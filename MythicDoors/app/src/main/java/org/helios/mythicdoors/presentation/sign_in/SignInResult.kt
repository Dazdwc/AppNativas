package org.helios.mythicdoors.presentation.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?,

) {
    companion object {
        fun success(data: UserData): SignInResult {
            return SignInResult(data, null)
        }

        fun error(errorMessage: String): SignInResult {
            return SignInResult(null, errorMessage)
        }
    }
}

data class UserData(
    val id: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?,
) {
    companion object {
        fun create(
            id: String,
            name: String? = null,
            email: String? = null,
            photoUrl: String? = null
        ): UserData {
            return UserData(id, name, email, photoUrl)
        }
    }
}
