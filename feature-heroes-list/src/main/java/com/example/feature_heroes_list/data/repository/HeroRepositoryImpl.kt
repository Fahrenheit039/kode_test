package com.example.feature_heroes_list.data.repository


import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.HeroDatabase
import com.example.core.database.Heroes.Heroes
import com.example.core.database.entities.AppearancesAfterDb
import com.example.core.database.entities.BiographiesAfterDb
import com.example.core.database.entities.ConnectionsAfterDb
import com.example.core.database.entities.HeroEntity
import com.example.core.database.entities.ImagesList
import com.example.core.database.entities.PowerstatsAfterDb
import com.example.core.database.entities.WorkAfterDb
import com.example.core.network.api.HeroApiService
import com.example.core.network.models.AppearanceResponse
import com.example.core.network.models.BiographyResponse
import com.example.core.network.models.ConnectionsResponse
import com.example.core.network.models.HeroResponse
import com.example.core.network.models.ImageResponse
import com.example.core.network.models.PowerstatsResponse
import com.example.core.network.models.WorkResponse
import com.example.feature_heroes_list.domain.repository.HeroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HeroRepositoryImpl(
    private var database: HeroDatabase,
    private var api: HeroApiService
) : HeroRepository {

    override suspend fun getHeroes(): List<Heroes> {
        TODO("Not yet implemented")
    }

    override suspend fun getHeroById(id: Long): Flow<HeroEntity?> {
        return database.heroQueries.selectById(id)
            .asFlow()
            .mapToOneOrNull(context = Dispatchers.IO)
            .map { dbHero -> dbHero?.let { mapDbHeroToEntity(it) } }
    }

    override suspend fun insertHero(hero: Heroes) {
        TODO("Not yet implemented")
    }

    override suspend fun mapDbHeroToEntity(dbHero: Heroes): HeroEntity {
        return HeroEntity(
            id = dbHero.id,
            name = dbHero.name,
            isFavorite = dbHero.isFavorite.toInt() == 1,
            powerstats = getPowerstats(database, dbHero.id),
            biographies = getBiography(database, dbHero.id).copy(
                publisher = getBiography(database, dbHero.id).publisher ?: "Unknown"
            ),
            appearances = getAppearance(database, dbHero.id),
            work = getWork(database, dbHero.id),
            connections = getConnections(database, dbHero.id),
            images = getImage(database, dbHero.id)
        )
    }


    override suspend fun getPowerstats(database: HeroDatabase, heroId: Long): PowerstatsAfterDb {
        val powerstats = database.powerstatsQueries.selectByHeroId(heroId).executeAsOne()
        return PowerstatsAfterDb(
            intelligence = powerstats.intelligence,
            strength = powerstats.strength,
            speed = powerstats.speed,
            durability = powerstats.durability,
            power = powerstats.power,
            combat = powerstats.combat
        )
    }
    override suspend fun getBiography(database: HeroDatabase, heroId: Long): BiographiesAfterDb {
        val biographies = database.biographiesQueries.selectByHeroId(heroId).executeAsOne()
        val aliases: List<String> = database.biographiesQueries
            .selectAliasesByHeroId(heroId).executeAsList().map { it.alias.toString() }
        return BiographiesAfterDb(
            fullName = biographies.fullName.toString(),
            alterEgos = biographies.alterEgos.toString(),
            aliases = aliases,
            placeOfBirth = biographies.placeOfBirth.toString(),
            firstAppearance = biographies.firstAppearance.toString(),
            publisher = biographies.publisher.toString()
        )
    }
    override suspend fun getAppearance(database: HeroDatabase, heroId: Long): AppearancesAfterDb {
        val appearance = database.appearancesQueries.selectByHeroId(heroId).executeAsOne()
        val height: List<String> = database.appearancesQueries
            .selectHeightByHeroId(heroId).executeAsList().map { it.height.toString() }
        val weight: List<String> = database.appearancesQueries
            .selectWeightByHeroId(heroId).executeAsList().map { it.weight.toString() }
        return AppearancesAfterDb(
            gender = appearance.gender,
            race = appearance.race,
            height = height,
            weight = weight,
            eyeColor = appearance.eyeColor,
            hairColor = appearance.hairColor
        )
    }
    override suspend fun getWork(database: HeroDatabase, heroId: Long): WorkAfterDb {
        val work = database.workQueries.selectByHeroId(heroId).executeAsOne()
        return WorkAfterDb(
            occupation = work.occupation,
            base = work.base
        )
    }
    override suspend fun getConnections(database: HeroDatabase, heroId: Long): ConnectionsAfterDb {
        val connections = database.connectionsQueries.selectByHeroId(heroId).executeAsOne()
        return ConnectionsAfterDb(
            groupAffiliation = connections.groupAffiliation,
            relatives = connections.relatives
        )
    }
    override suspend fun getImage(database: HeroDatabase, heroId: Long): ImagesList {
        val image = database.imagesQueries.selectImageByHeroId(heroId).executeAsList()
//        val test = image.map { it.url.toString() }
        return ImagesList(
//            urls = listOf(image.url) // Предполагаем, что у вас только одно изображение
            urls = image.map { it.url.toString() }
        )
    }

    override suspend fun insertHeroIfNotExists(database: HeroDatabase, hero: HeroResponse) {
        val existing = database.heroQueries.selectById(hero.id).executeAsList()
        if (existing.isEmpty()) {
            // Вставляем героя в таблицу Heroes
            hero.let { insertHero(database, it) }
            // Вставляем остальные данные
            hero.biography.let { insertBiography(database, it, hero.id) }
            hero.appearance.let { insertAppearance(database, it, hero.id) }
            hero.work.let { insertWork(database, it, hero.id) }
            hero.connections.let { insertConnections(database, it, hero.id) }
            hero.powerstats.let { insertPowerstats(database, it, hero.id) }
            // Вставляем картинки
            hero.image.let { insertImage(database, it, hero.id) }
        } else {
            Log.d("MyApp:InsertError","Hero with id= ${hero.id} already exists.")
        }
    }

    override suspend fun insertHero(database: HeroDatabase, hero: HeroResponse) {
        database.heroQueries.insert(
            id = hero.id,  // Используем heroId как id для Work
            name = hero.name,
            isFavorite = 0
        )
    }
    override suspend fun insertBiography(database: HeroDatabase, biography: BiographyResponse, heroId: Long) {
        database.transaction {
            database.biographiesQueries.insert(
                id = heroId,  // Используем heroId как id для Biographies
                fullName = biography.fullName,
                alterEgos = biography.alterEgos,
                placeOfBirth = biography.placeOfBirth,
                firstAppearance = biography.firstAppearance,
                publisher = biography.publisher
            )

            // Вставляем данные в таблицу BiographiesAliases
            biography.aliases?.let { aliases ->
                aliases.forEach { alias ->
                    database.biographiesQueries.insertAliases(
                        heroId = heroId,
                        alias = alias
                    )
                }
            } ?: run {
                database.biographiesQueries.insertAliases(
                    heroId = heroId,
                    alias = "-"
                )
            }
        }
    }
    override suspend fun insertAppearance(database: HeroDatabase, appearance: AppearanceResponse, heroId: Long) {
        // Начинаем транзакцию
        database.transaction {
            // Вставляем данные в таблицу Appearances
            database.appearancesQueries.insert(
                id = heroId,  // Используем heroId как id для Appearances
                gender = appearance.gender,
                race = appearance.race,
                eyeColor = appearance.eyeColor,
                hairColor = appearance.hairColor
            )

            // Вставляем данные в таблицу AppearanceHeight
            appearance.height?.let { height ->
                height.forEach { height ->
                    database.appearancesQueries.insertHeight(
                        heroId = heroId,
                        height = height
                    )
                }
            } ?: run {
                database.appearancesQueries.insertHeight(
                    heroId = heroId,
                    height = "-"
                )
            }

            // Вставляем данные в таблицу AppearanceWeight
            appearance.weight?.let { weight ->
                weight.forEach { weight ->
                    database.appearancesQueries.insertWeight(
                        heroId = heroId,
                        weight = weight
                    )
                }
            } ?: run {
                database.appearancesQueries.insertWeight(
                    heroId = heroId,
                    weight = "-"
                )
            }
        }
    }
    override suspend fun insertWork(database: HeroDatabase, work: WorkResponse, heroId: Long) {
        database.workQueries.insert(
            id = heroId,  // Используем heroId как id для Work
            occupation = work.occupation,
            base = work.base
        )
    }
    override suspend fun insertConnections(database: HeroDatabase, connections: ConnectionsResponse, heroId: Long) {
        database.connectionsQueries.insert(
            id = heroId,  // Используем heroId как id для Connections
            groupAffiliation = connections.groupAffiliation,
            relatives = connections.relatives
        )
    }
    override suspend fun insertPowerstats(database: HeroDatabase, powerstats: PowerstatsResponse, heroId: Long) {
        database.powerstatsQueries.insert(
            id = heroId,  // Используем heroId как id для Powerstats
            intelligence = powerstats.intelligence,
            strength = powerstats.strength,
            speed = powerstats.speed,
            durability = powerstats.durability,
            power = powerstats.power,
            combat = powerstats.combat
        )
    }
    override suspend fun insertImage(database: HeroDatabase, image: ImageResponse, heroId: Long) {
        database.imagesQueries.insert(
            heroId = heroId,
            url = image.url
        )
    }

}


