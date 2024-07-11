package ru.zakablukov.yourmoviebase.data.repositoryimpl

import com.google.mlkit.nl.translate.Translator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import ru.zakablukov.yourmoviebase.data.util.Request
import ru.zakablukov.yourmoviebase.data.util.RequestUtils.requestFlow
import ru.zakablukov.yourmoviebase.domain.model.TranslateText
import ru.zakablukov.yourmoviebase.domain.repository.TranslateRepository
import javax.inject.Inject

class TranslateRepositoryImpl @Inject constructor(
    private val translator: Translator
) : TranslateRepository {

    override suspend fun translateRUtoEN(translateText: TranslateText): Flow<Request<TranslateText>> {
        return requestFlow {
            val translated = translator.translate(translateText.text).await()
            TranslateText(translateText.type, translated)
        }
    }
}