package com.london.presentation.shared.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.domain.entity.actor.Actor
import com.london.presentation.shared.item.ActorItem

@Composable
fun ActorLazyVerticalColumn(
    items: LazyPagingItems<Actor>,
    onActorClick: (Actor) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items.itemCount) { index ->
            val actor = items[index]
            if (actor != null) {
                ActorItem(
                    actorName = actor.name,
                    characterName = null,
                    imageRes = actor.profilePictureUrl,
                    onClick = { onActorClick(actor) }
                )
            }
        }
    }
}
