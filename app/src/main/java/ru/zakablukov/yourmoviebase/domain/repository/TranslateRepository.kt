package ru.zakablukov.yourmoviebase.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.domain.model.TranslateText

interface TranslateRepository {

    suspend fun translateRUtoEN(translateText: TranslateText): Flow<Request<TranslateText>>

    suspend fun translateListRUtoEN(list: List<TranslateText>): Flow<Request<List<TranslateText>>>
}