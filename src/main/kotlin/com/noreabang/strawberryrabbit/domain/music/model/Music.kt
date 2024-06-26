package com.noreabang.strawberryrabbit.domain.music.model

import com.noreabang.strawberryrabbit.domain.music.dto.MusicRequest
import com.noreabang.strawberryrabbit.domain.music.dto.MusicResponse
import jakarta.persistence.*

@Entity
class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, length = 100)
    var singer: String? = null

    @Column(nullable = false, length = 100)
    var title: String? = null

    @Column(length = 1000)
    var cover: String? = null

    companion object {
        fun createMusic(musicRequest: MusicRequest): Music {
            val music: Music = Music()
            music.singer = musicRequest.singer
            music.title = musicRequest.title
            music.cover = musicRequest.cover
            return music
        }
    }

    fun toResponse(): MusicResponse {
        return MusicResponse(
            id = id!!,
            singer = singer,
            title = title,
            cover = cover
        )
    }
}