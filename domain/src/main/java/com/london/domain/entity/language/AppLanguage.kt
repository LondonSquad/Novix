package com.london.domain.entity.language

enum class AppLanguage(val code: String) {
    ENGLISH("en"),
    ARABIC("ar");

    companion object {
        fun fromCode(languageCode: String): AppLanguage =
            AppLanguage.entries.find { it.code == languageCode } ?: ARABIC
    }
}
