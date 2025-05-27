package com.example.feature_heroes_list.domain.repository

import com.example.HeroDatabase
import com.example.core.database.Heroes.Heroes
import com.example.core.database.entities.AppearancesAfterDb
import com.example.core.database.entities.BiographiesAfterDb
import com.example.core.database.entities.ConnectionsAfterDb
import com.example.core.database.entities.HeroEntity
import com.example.core.database.entities.ImagesList
import com.example.core.database.entities.PowerstatsAfterDb
import com.example.core.database.entities.WorkAfterDb
import com.example.core.network.models.AppearanceResponse
import com.example.core.network.models.BiographyResponse
import com.example.core.network.models.ConnectionsResponse
import com.example.core.network.models.HeroResponse
import com.example.core.network.models.ImageResponse
import com.example.core.network.models.PowerstatsResponse
import com.example.core.network.models.WorkResponse
import kotlinx.coroutines.flow.Flow

interface HeroRepository {
    suspend fun getHeroes(): List<Heroes>
    suspend fun getHeroById(id: Long): Flow<HeroEntity?>
    suspend fun insertHero(hero: Heroes)
//    suspend fun getHeroesByRange(startId: Long, count: Int): List<HeroDto>
//    suspend fun toggleFavorite(heroId: Long)

    suspend fun mapDbHeroToEntity(dbHero: Heroes): HeroEntity

    suspend fun getPowerstats(database: HeroDatabase, heroId: Long) : PowerstatsAfterDb
    suspend fun getBiography(database: HeroDatabase, heroId: Long) : BiographiesAfterDb
    suspend fun getAppearance(database: HeroDatabase, heroId: Long) : AppearancesAfterDb
    suspend fun getWork(database: HeroDatabase, heroId: Long) : WorkAfterDb
    suspend fun getConnections(database: HeroDatabase, heroId: Long) : ConnectionsAfterDb
    suspend fun getImage(database: HeroDatabase, heroId: Long) : ImagesList

    suspend fun insertHeroIfNotExists(database: HeroDatabase, hero: HeroResponse)

    suspend fun insertHero(database: HeroDatabase, hero: HeroResponse)
    suspend fun insertBiography(database: HeroDatabase, biography: BiographyResponse, heroId: Long)
    suspend fun insertAppearance(database: HeroDatabase, appearance: AppearanceResponse, heroId: Long)
    suspend fun insertWork(database: HeroDatabase, work: WorkResponse, heroId: Long)
    suspend fun insertConnections(database: HeroDatabase, connections: ConnectionsResponse, heroId: Long)
    suspend fun insertPowerstats(database: HeroDatabase, powerstats: PowerstatsResponse, heroId: Long)
    suspend fun insertImage(database: HeroDatabase, image: ImageResponse, heroId: Long)
}


