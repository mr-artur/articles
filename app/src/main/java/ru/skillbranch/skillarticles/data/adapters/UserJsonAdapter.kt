package ru.skillbranch.skillarticles.data.adapters

import ru.skillbranch.skillarticles.data.local.User

class UserJsonAdapter : JsonAdapter<User> {

    companion object {
        private const val DELIMITER = ";"
    }

    override fun fromJson(json: String): User {
        val parts = json.split(DELIMITER)
        return User(
            firstName = parts[0],
            lastName = parts[1],
            rating = Integer.parseInt(parts[2]),
            respect = parts[3]
        )
    }

    override fun toJson(obj: User): String =
        arrayOf(obj.firstName, obj.lastName, obj.rating, obj.respect)
            .joinToString(separator = DELIMITER)
}