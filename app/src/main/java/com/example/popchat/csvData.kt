package com.example.popchat

import java.math.BigInteger

data class csvData(
    val data__id: BigInteger,
    val data__readable: Boolean,
    val data__title: String,
    val data__title_short: String,
    val data__title_version: String?,
    val data__link: String,
    val data__duration: Int,
    val data__rank: Int,
    val data__explicit_lyrics: Boolean,
    val data__explicit_content_lyrics: Int,
    val data__explicit_content_cover: Int,
    val data__preview: String,
    val data__md5_image: String,
    val data_artist_id: Int,
    val data_artist_name: String,
    val data_artist_link: String,
    val data_artist_picture: String,
    val data_artist_picture_small: String,
    val data_artist_picture_medium: String,
    val data_artist_picture_big: String,
    val data_artist_picture_xl: String,
    val data_artist_tracklist: String,
    val data_artist_type: String,
    val data_album_id: Int,
    val data_album_title: String,
    val data_album_cover: String,
    val data_album_cover_small: String,
    val data_album_cover_medium: String,
    val data_album_cover_big: String,
    val data_album_cover_xl: String,
    val data_album_md5_image: String,
    val data_album_tracklist: String,
    val data_album_type: String,
    val data__type: String,
    val total: String,
    val next: String?

)
