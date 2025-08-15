package com.london.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.london.designsystem.component.NavBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.account.AccountScreen
import com.london.presentation.feature.account.rating.MyRatingScreen
import com.london.presentation.feature.authentication.login.LoginScreen
import com.london.presentation.feature.authentication.register.RegistrationScreen
import com.london.presentation.feature.category.main.CategoriesScreen
import com.london.presentation.feature.category.movie.MoviesByCategoryScreen
import com.london.presentation.feature.category.tvshow.TvShowByCategoryScreen
import com.london.presentation.feature.details.actor.ActorDetailsScreen
import com.london.presentation.feature.details.actor.info.gallery.ActorsGalleryScreen
import com.london.presentation.feature.details.actor.info.topmoviespicks.TopMoviesPicksScreen
import com.london.presentation.feature.details.actor.info.toptvshowspicks.TopTvShowsPicksScreen
import com.london.presentation.feature.details.movie.MovieDetailsScreen
import com.london.presentation.feature.details.tvshow.episode.EpisodeDetailsScreen
import com.london.presentation.feature.details.tvshow.info.TvShowsDetailsScreen
import com.london.presentation.feature.home.HomeScreen
import com.london.presentation.feature.home.continuewatching.ContinueWatchingScreen
import com.london.presentation.feature.home.toprated.TopRatedScreen
import com.london.presentation.feature.home.trending.actor.TrendingActorsScreen
import com.london.presentation.feature.home.trending.movie.TrendingMoviesScreen
import com.london.presentation.feature.home.trending.tvshow.TrendingTvShowsScreen
import com.london.presentation.feature.list.savedlist.ListScreen
import com.london.presentation.feature.list.viewitems.ViewListItemsScreen
import com.london.presentation.feature.reviews.ReviewsScreen
import com.london.presentation.feature.search.SearchScreen
import com.london.presentation.feature.welcome.onboarding.OnboardingRoute
import com.london.presentation.feature.welcome.onboarding.WelcomeScreen
import com.london.presentation.feature.welcome.splash.SplashRoute
import com.london.presentation.navigation.LocalNavController
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.Screen.ActorDetails
import com.london.presentation.navigation.Screen.MovieDetails
import com.london.presentation.navigation.Screen.TrendingActors
import com.london.presentation.navigation.Screen.TrendingMovies
import com.london.presentation.navigation.Screen.TrendingTvShows
import com.london.presentation.navigation.Screen.TvShowDetails
import com.london.presentation.navigation.Screen.WatchingHistory
import kotlinx.serialization.Serializable
import timber.log.Timber

@Composable
fun NovixApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = when {
        currentDestination?.hasRoute<Screen.Home>() == true -> Screen.Home
        currentDestination?.hasRoute<Screen.Search>() == true -> Screen.Search
        currentDestination?.hasRoute<Screen.Categories>() == true -> Screen.Categories
        currentDestination?.hasRoute<Screen.Lists>() == true -> Screen.Lists()
        currentDestination?.hasRoute<Screen.Account>() == true -> Screen.Account
        currentDestination?.hasRoute<Screen.Login>() == true -> Screen.Login
        else -> Screen.Home
    }

    val showBottomNav = currentDestination?.hasRoute<Screen.Home>() == true ||
            currentDestination?.hasRoute<Screen.Search>() == true ||
            currentDestination?.hasRoute<Screen.Categories>() == true ||
            currentDestination?.hasRoute<Screen.Lists>() == true ||
            currentDestination?.hasRoute<Screen.Account>() == true

    Scaffold(
        backgroundColor = NovixTheme.colors.surface,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNav,
                enter = slideInVertically(animationSpec = tween(), initialOffsetY = { it }),
                exit = slideOutVertically(animationSpec = tween(), targetOffsetY = { it })
            ) {
                NavBar(
                    modifier = Modifier.navigationBarsPadding(),
                    navDestinations = NavigationHelper.getNavigationTabs(),
                    currentSelectedDestination = currentScreen,
                    onNavDestinationClicked = { destination ->
                        navigateToBottomBarDestination(navController, destination)
                    }
                )
            }
        }
    ) { innerPadding ->
        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(
                navController = navController,
                startDestination = NovixAppNavGraph.Splash,
                modifier = Modifier.padding(innerPadding)
            ) {
                onboardingNavGraph(navController)
                splashNavGraph(navController)
                authNavGraph(navController)
                mainNavGraph(navController)
            }
        }
    }

}
