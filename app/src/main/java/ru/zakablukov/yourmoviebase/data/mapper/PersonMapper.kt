package ru.zakablukov.yourmoviebase.data.mapper

import ru.zakablukov.yourmoviebase.data.database.entity.PersonEntity
import ru.zakablukov.yourmoviebase.data.model.PersonResponse
import ru.zakablukov.yourmoviebase.domain.model.Person
import java.util.Locale

fun PersonResponse.toDomain(): Person =
    Person(
        id,
        name?.let { if (Locale.getDefault().language == "ru") name else enName }
            ?: enName.orEmpty(),
        if (Locale.getDefault().language == "ru") profession else enProfession,
        photoUrl
    )

fun Person.toEntity(): PersonEntity =
    PersonEntity(
        externalId = id,
        photoUrl = photoUrl,
        name = name,
        enName = name,
        profession = profession,
        enProfession = profession
    )

fun PersonEntity.toDomain(): Person =
    Person(
        externalId,
        name?.let { if (Locale.getDefault().language == "ru") name else enName }
            ?: enName.orEmpty(),
        if (Locale.getDefault().language == "ru") profession else enProfession,
        photoUrl
    )