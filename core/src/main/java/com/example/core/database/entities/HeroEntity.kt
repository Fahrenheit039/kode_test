
package com.example.core.database.entities

import com.squareup.moshi.Json

data class HeroEntity(
    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val powerstats: PowerstatsAfterDb,
    val biographies: BiographiesAfterDb,
    val appearances: AppearancesAfterDb,
    val work: WorkAfterDb,
    val connections: ConnectionsAfterDb,
    val images: ImagesList
)

data class PowerstatsAfterDb(
    val intelligence: String?,
    val strength: String?,
    val speed: String?,
    val durability: String?,
    val power: String?,
    val combat: String?
)

data class BiographiesAfterDb(
    @Json(name = "full-name")
    val fullName: String?,
    @Json(name = "alter-egos")
    val alterEgos: String?,
    val aliases: List<String>?,
    @Json(name = "place-of-birth")
    val placeOfBirth: String?,
    @Json(name = "first-appearance")
    val firstAppearance: String?,
    val publisher: String?
)

data class AppearancesAfterDb(
    val gender: String?,
    val race: String?,
    val height: List<String>?,
    val weight: List<String>?,
    @Json(name = "eye-color")
    val eyeColor: String?,
    @Json(name = "hair-color")
    val hairColor: String?
)

data class WorkAfterDb(
    val occupation: String?,
    val base: String?
)

data class ConnectionsAfterDb(
    @Json(name = "group-affiliation")
    val groupAffiliation: String?,
    val relatives: String?
)


data class ImagesList(
    var urls: List<String>?
)
//{
//    constructor(
//        database: HeroDatabase,
//        heroId: Long
//    ): this(
//        urls = database.imagesQueries
//            .selectImageByHeroId(heroId).executeAsList().map
//            { it.url.toString() }
//    )
//}


