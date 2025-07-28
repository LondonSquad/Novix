package com.london.designsystem.component.carousel

import com.london.designsystem.component.carousel.m3.CarouselItemScope

/**
 * Extension function to check if an item is currently the hero
 */
val CarouselItemScope.isHero: Boolean
    get() = carouselItemDrawInfo.size >= carouselItemDrawInfo.maxSize * 0.9f

/**
 * Extension function to get the hero ratio (0.0 to 1.0)
 */
val CarouselItemScope.heroRatio: Float
    get() {
        val range = carouselItemDrawInfo.maxSize - carouselItemDrawInfo.minSize
        return if (range > 0) {
            ((carouselItemDrawInfo.size - carouselItemDrawInfo.minSize) / range).coerceIn(0f, 1f)
        } else {
            1f
        }
    }
