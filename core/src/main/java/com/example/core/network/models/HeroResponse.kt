package com.example.core.network.models

import com.example.core.database.entities.AppearancesAfterDb
import com.example.core.database.entities.BiographiesAfterDb
import com.example.core.database.entities.ConnectionsAfterDb
import com.example.core.database.entities.HeroEntity
import com.example.core.database.entities.ImagesList
import com.example.core.database.entities.PowerstatsAfterDb
import com.example.core.database.entities.WorkAfterDb
import com.squareup.moshi.Json


//response from https://superheroapi.com/
data class HeroResponse(
    val response: String,
    val id: Long,
    val name: String,
    val powerstats: PowerstatsResponse,
    val biography: BiographyResponse,
    val appearance: AppearanceResponse,
    val work: WorkResponse,
    val connections: ConnectionsResponse,
    val image: ImageResponse
) {

    fun toHeroEntity(): HeroEntity {
        return HeroEntity(
            id = this.id,
            name = this.name,
            isFavorite = false, // или любое другое значение по умолчанию
            powerstats = PowerstatsAfterDb(
                intelligence = this.powerstats.intelligence,
                strength = this.powerstats.strength,
                speed = this.powerstats.speed,
                durability = this.powerstats.durability,
                power = this.powerstats.power,
                combat = this.powerstats.combat
            ),
            biographies = BiographiesAfterDb(
                fullName = this.biography.fullName.toString(),
                alterEgos = this.biography.alterEgos.toString(),
                aliases = this.biography.aliases ?: emptyList(),
                placeOfBirth = this.biography.placeOfBirth.toString(),
                firstAppearance = this.biography.firstAppearance.toString(),
                publisher = this.biography.publisher.toString()
            ),
            appearances = AppearancesAfterDb(
                gender = this.appearance.gender,
                race = this.appearance.race,
                height = this.appearance.height ?: emptyList(),
                weight = this.appearance.weight ?: emptyList(),
                eyeColor = this.appearance.eyeColor,
                hairColor = this.appearance.hairColor
            ),
            work = WorkAfterDb(
                occupation = this.work.occupation,
                base = this.work.base
            ),
            connections = ConnectionsAfterDb(
                groupAffiliation = this.connections.groupAffiliation,
                relatives = this.connections.relatives
            ),
            images = ImagesList(
                urls = listOf(this.image.url ?: "")
            )
        )
    }

}

data class PowerstatsResponse(
    val intelligence: String,
    val strength: String,
    val speed: String,
    val durability: String,
    val power: String,
    val combat: String
)

data class BiographyResponse(
    @Json(name = "full-name")
    val fullName: String,
    @Json(name = "alter-egos")
    val alterEgos: String,
    val aliases: List<String>,
    @Json(name = "place-of-birth")
    val placeOfBirth: String,
    @Json(name = "first-appearance")
    val firstAppearance: String,
    val publisher: String,
    val alignment: String
)

data class AppearanceResponse(
    val gender: String,
    val race: String,
    val height: List<String>,
    val weight: List<String>,
    @Json(name = "eye-color")
    val eyeColor: String,
    @Json(name = "hair-color")
    val hairColor: String
)

data class WorkResponse(
    val occupation: String,
    val base: String
)

data class ConnectionsResponse(
    @Json(name = "group-affiliation")
    val groupAffiliation: String,
    val relatives: String
)

data class ImageResponse(
    val url: String
)


