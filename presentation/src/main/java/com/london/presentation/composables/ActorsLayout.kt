package com.london.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.ActorItem
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Actor

@Composable
fun ActorsLayout(
    actorsUis: List<Actor>,
    onActorClick: (Actor) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(actorsUis) { actor ->
            ActorItem(
                modifier = Modifier.clickable(onClick = { onActorClick(actor) }),
                actorName = actor.name,
                characterName = null,
                imageRes = actor.profilePicture
            )
        }
    }
}

@ThemePreviews
@Composable
fun ActorsLayoutPreviews() {
    NovixTheme {
        ActorsLayout(
            actorsUis = listOf(
                Actor(1, "Tom Hanks", ""),
                Actor(2, "Robert Downey Jr.", ""),
                Actor(3, "Ahmed Helmy", ""),
                Actor(4, "Matt Damon", ""),
                Actor(5, "Will Smith", "")
            ),
            onActorClick = {}
        )
    }
}