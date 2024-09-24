package ru.zakablukov.yourmoviebase.presentation.viewmodel

import ru.zakablukov.yourmoviebase.domain.model.TranslateText

interface LocalizedViewModel {

    fun tryTextLocalization(vararg translateTexts: TranslateText): Boolean
}