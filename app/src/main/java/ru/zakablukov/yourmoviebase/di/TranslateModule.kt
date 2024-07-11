package ru.zakablukov.yourmoviebase.di

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TranslateModule {

    @Singleton
    @Provides
    fun provideTranslatorOptions() = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.RUSSIAN)
        .setTargetLanguage(TranslateLanguage.ENGLISH)
        .build()

    @Singleton
    @Provides
    fun provideDownloadConditions() = DownloadConditions.Builder()
        .requireWifi()
        .build()

    @Singleton
    @Provides
    fun provideRussianEnglishTranslator(
        translatorOptions: TranslatorOptions,
        downloadConditions: DownloadConditions
    ): Translator {
        val translator = Translation.getClient(translatorOptions)
        translator.downloadModelIfNeeded(downloadConditions)
        return translator
    }
}