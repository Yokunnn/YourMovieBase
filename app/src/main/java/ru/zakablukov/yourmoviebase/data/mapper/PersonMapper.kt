package ru.zakablukov.yourmoviebase.data.mapper

import ru.zakablukov.yourmoviebase.data.model.PersonResponse
import ru.zakablukov.yourmoviebase.domain.model.Person

fun PersonResponse.toDomain(): Person =
    Person(
        id,
        enName?.let { enName } ?: name.orEmpty(),
        enProfession,
        photoUrl
    )