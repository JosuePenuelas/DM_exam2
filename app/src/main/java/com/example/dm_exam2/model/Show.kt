package com.example.dm_exam2.model

import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.dm_exam2.data.database.FavoriteShow

@Parcelize
data class Show(
    val averageRuntime: Int,
    val dvdCountry: DvdCountry?,
    val ended: String?,
    val externals: Externals?,
    val genres: List<String?>?,
    val id: Int?,
    val image: Image?,
    val language: String?,
    @Json(name = "_links")
    val links: Links?,
    val name: String?,
    val network: Network?,
    val officialSite: String?,
    val premiered: String?,
    val rating: Rating?,
    val runtime: Int?,
    val schedule: Schedule?,
    val status: String?,
    val summary: String?,
    val type: String?,
    val updated: Int?,
    val url: String?,
    val webChannel: WebChannel?,
    val weight: Int?
) : Parcelable {
    @Parcelize
    data class DvdCountry(
        val code: String?,
        val name: String?,
        val timezone: String?
    ) : Parcelable

    @Parcelize
    data class Externals(
        val imdb: String?,
        val thetvdb: Int?,
        val tvrage: Int?
    ) : Parcelable

    @Parcelize
    data class Image(
        val medium: String?,
        val original: String?
    ) : Parcelable

    @Parcelize
    data class Links(
        val previousepisode: Previousepisode?,
        val self: Self?
    ) : Parcelable {
        @Parcelize
        data class Previousepisode(
            val href: String?,
            val name: String?
        ) : Parcelable

        @Parcelize
        data class Self(
            val href: String?
        ) : Parcelable
    }

    @Parcelize
    data class Network(
        val country: Country?,
        val id: Int?,
        val name: String?,
        val officialSite: String?
    ) : Parcelable {
        @Parcelize
        data class Country(
            val code: String?,
            val name: String?,
            val timezone: String?
        ) : Parcelable
    }

    @Parcelize
    data class Rating(
        val average: Double?
    ) : Parcelable

    @Parcelize
    data class Schedule(
        val days: List<String?>?,
        val time: String?
    ) : Parcelable

    @Parcelize
    data class WebChannel(
        val country: Country?,
        val id: Int?,
        val name: String?,
        val officialSite: String?
    ) : Parcelable {
        @Parcelize
        data class Country(
            val code: String?,
            val name: String?,
            val timezone: String?
        ) : Parcelable
    }

    fun toFavoriteShow(): FavoriteShow {
        return FavoriteShow(
            id = this.id ?: 0,
            name = this.name ?: "",
            image = this.image?.original ?: "",
            rate = this.rating?.average?.toString() ?: "",
            genres = this.genres?.filterNotNull()?.joinToString(", ") ?: ""
        )
    }
}

data class SearchShowResponse(
    val score: Double?,
    val show: Show
)