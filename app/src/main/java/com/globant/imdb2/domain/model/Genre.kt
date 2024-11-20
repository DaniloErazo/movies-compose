package com.globant.imdb2.domain.model

import com.globant.imdb2.presentation.model.GenreDTO

data class Genre (
    val name: String
)

fun Genre.toDTO(): GenreDTO {
    return GenreDTO(name=this.name)
}
