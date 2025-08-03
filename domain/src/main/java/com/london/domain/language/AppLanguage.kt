package com.london.domain.language

enum class AppLanguage(val code: String) {
    ENGLISH("en"),
    ARABIC("ar");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return AppLanguage.entries.find { it.code == code } ?: ENGLISH
        }
    }
}