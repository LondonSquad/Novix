package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ProfileDto
import com.london.data.datasource.remote.details.actordetails.model.ProfilesResponseDto
import com.london.domain.entity.actordetails.ProfileDetails
import com.london.domain.entity.actordetails.ProfilesResponseDetails

fun ProfilesResponseDto.toEntity(): ProfilesResponseDetails {
    return ProfilesResponseDetails(
    id = this.id,
    profiles = this.profiles.map { it.toEntity() }
    )
}

fun ProfileDto.toEntity(): ProfileDetails {
    return ProfileDetails(
        aspectRatio = this.aspectRatio,
        height = this.height,
        iso = this.iso,
        filePath = this.filePath,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        width = this.width
    )
}