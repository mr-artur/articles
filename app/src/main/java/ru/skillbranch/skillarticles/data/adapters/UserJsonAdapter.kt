package ru.skillbranch.skillarticles.data.adapters

import ru.skillbranch.skillarticles.data.local.User

class UserJsonAdapter : JsonAdapter<User> {

    companion object {
        private const val DELIMITER = ";"
    }

    override fun fromJson(json: String): User = User(
        firstName = json.substringBefore(DELIMITER),
        lastName = json.substringAfter(DELIMITER)
    )

    override fun toJson(obj: User): String = arrayOf(obj.firstName, obj.lastName)
        .joinToString(separator = DELIMITER)
}