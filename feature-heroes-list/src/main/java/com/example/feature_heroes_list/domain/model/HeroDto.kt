package com.example.feature_heroes_list.domain.model

import com.example.core.database.entities.BiographiesAfterDb
import com.example.core.database.entities.HeroEntity

//то, что отображается в пользовательском интерфейсе
//.upd хотя я справляюсь и без этого

data class HeroDto(
    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val imageUrl: String,
    val fullName: String?,
    val publisher: String?
) {
    constructor(
        id: Long,
        name: String,
        isFavorite: Boolean,
        imageUrl: String,
        biographies: BiographiesAfterDb
    ): this(
        id=id,
        name=name,
        isFavorite=isFavorite,
        imageUrl=imageUrl,
        fullName=biographies.fullName,
        publisher=biographies.publisher
    )

}

fun HeroEntity.toHeroDto(): HeroDto {
    return HeroDto(
        id = this.id,
        name = this.name,
        isFavorite = this.isFavorite,
//        imageUrl = this.images.urls.getOrElse(0, placeholder),
        imageUrl = this.images.urls?.get(0) ?: "nothing",
        fullName = this.biographies.fullName,
        publisher = this.biographies.publisher
    )
}


