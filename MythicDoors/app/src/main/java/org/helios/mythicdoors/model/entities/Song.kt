package org.helios.mythicdoors.model.entities

import android.net.Uri

data class Song(
    val id: Int,
    val name: String,
    val artist: String,
    val path: Uri
) {
    companion object {
        fun create(
            id: Int,
            name: String,
            artist: String = "",
            path: Uri = Uri.EMPTY
            ): Song {
            return Song(
                id = id,
                name = name,
                artist = artist,
                path = path
            )
        }
    }
}