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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.london.domain.AppPreferencesService
import com.london.presentation.feature.account.AccountScreen
import com.london.presentation.feature.bookmark.BookmarksScreen
import com.london.presentation.feature.category.CategoriesScreen
import com.london.presentation.feature.category.moviesbycategory.MoviesByCategoryScreen
import com.london.presentation.feature.details.actor.ActorDetailsScreen
import com.london.presentation.feature.details.actordetails.gallery.ActorGalleryScreen
import com.london.presentation.feature.details.actordetails.topmoviespicks.TopMoviesPicksScreen
import com.london.presentation.feature.details.actordetails.toptvshowspicks.TopTvShowsPicksScreen
import com.london.presentation.feature.details.movieDetalis.MovieDetailsScreen
import com.london.presentation.feature.details.tvshow.episodedetails.EpisodeDetailsScreen
import com.london.presentation.feature.details.tvshow.tvshowdetails.TvShowsDetailsScreen
import com.london.presentation.feature.home.HomeScreen
import com.london.presentation.feature.login.LoginScreen
import com.london.presentation.feature.onboarding.OnboardingRoute
import com.london.presentation.feature.onboarding.SplashRoute
import com.london.presentation.feature.onboarding.WelcomeScreen
import com.london.presentation.feature.reviews.ReviewsScreen
import com.london.presentation.feature.search.SearchScreen
import com.london.presentation.feature.toprated.TopRatedScreen
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.Screen.MovieDetails
import com.london.presentation.navigation.Screen.TvShowDetails

import kotlinx.serialization.Serializable
import org.koin.compose.getKoin
import timber.log.Timber


@Serializable
sealed interface NovixAppNavGraph {
    @Serializable
    data object Splash : NovixAppNavGraph

    @Serializable
    data object OnBoarding : NovixAppNavGraph

    @Serializable
    data object Auth : NovixAppNavGraph
    @Serializable
    data object Main : NovixAppNavGraph
}

fun NavGraphBuilder.splashNavGraph(
    navController: NavHostController,
    appPreferencesService: AppPreferencesService
) = navigation<NovixAppNavGraph.Splash>(startDestination = Screen.Splash) {
    composable<Screen.Splash> {
        SplashRoute(
            onNavigateToOnboarding = { navController.navigateToOnboardingGraph() },
            onNavigateToWelcome = { navController.navigateTo(Screen.OnBoarding.Welcome) },
            onNavigateToHome = { navController.navigateToMainGraph() },
            appPreferencesService = appPreferencesService,
            authRepository = getKoin().get(),
        )
    }
}

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavHostController,
    appPreferencesService: AppPreferencesService
) = navigation<NovixAppNavGraph.OnBoarding>(startDestination = Screen.OnBoarding) {
    composable<Screen.OnBoarding> {
        OnboardingRoute(
            onNavigateToWelcome = { navController.navigateTo(Screen.OnBoarding.Welcome) },
            appPreferencesService = appPreferencesService
        )
    }

    composable<Screen.OnBoarding.Welcome> {
        WelcomeScreen(
            onLoginClicked = {
                navController.navigateToAuthGraph()
            },
            onContinueClicked = {
                navController.navigateToMainGraph()
            }
        )
    }
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) = navigation<NovixAppNavGraph.Auth>(startDestination = Screen.Login) {
    composable<Screen.Login>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        LoginScreen(
            onNavigateToHome = {
                navController.navigateToMainGraph()
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController
) = navigation<NovixAppNavGraph.Main>(startDestination = Screen.Home) {
    composable<Screen.Home>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        HomeScreen(
            onMovieClick = { movieId ->
                navController.navigate(Screen.MovieDetails(movieId))
            },

            onTvShowClick = { tvShowId ->
                navController.navigate(Screen.TvShowDetails(tvShowId))
            },
            onTopRatedClick = {
                navController.navigate(Screen.TopRated)
            }
        )
    }

    composable<Screen.Search>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        SearchScreen(
            onNavigateToTvShowDetails = { tvShowId ->
                navController.navigate(Screen.TvShowDetails(tvShowId))
            },
            onNavigateToActorDetails = { actorId ->
                navController.navigate(Screen.ActorDetails(actorId))
            },
            onNavigateToMovieDetails = { movieId ->
                navController.navigate(Screen.MovieDetails(movieId))
            }
        )
    }

    composable<Screen.Categories>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        CategoriesScreen()
    }

    composable<Screen.Bookmarks>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        BookmarksScreen()
    }

    composable<Screen.Account>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        AccountScreen()
    }

    composable<Screen.TvShowDetails>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        TvShowsDetailsScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onNavigateToEpisodeDetails = { tvShowId, episodeNumber, seasonNumber ->
                navController.navigate(
                    Screen.EpisodeDetails(
                        tvShowId,
                        seasonNumber,
                        episodeNumber
                    )
                )
            },
            onNavigateToReviews = { tvShowId, mediaType ->
                navController.navigate(Screen.Reviews(tvShowId, mediaType))
            }, onNavigateToCast = { actorId ->
                navController.navigate(Screen.ActorDetails(actorId))
            }
        )
    }

    composable<Screen.TopTvShowsPicksDetails>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        TopTvShowsPicksScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onTvShowClick = { tvShowId ->
                navController.navigate(Screen.TvShowDetails(tvShowId))
            }
        )
    }
    composable<Screen.ActorTopMoviesPicksDetails> {
        TopMoviesPicksScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onMovieClick = { movieId ->
                navController.navigate(Screen.MovieDetails(movieId))
            }
        )
    }

    composable<Screen.MovieDetails>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        MovieDetailsScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onGenreClick = { genreId ->
                navController.navigate(Screen.MoviesByCategory(genreId))
            },
            onNavigateToMovie = { movieId ->
                navController.navigate(Screen.MovieDetails(movieId))
            },
            onNavigateToActor = { actorId ->
                navController.navigate(Screen.ActorDetails(actorId))
            },
            onNavigateToReviews = { movieId, mediaType ->
                navController.navigate(Screen.Reviews(movieId, mediaType))
            }
        )
    }

    composable<Screen.Reviews> {
        ReviewsScreen(
            onBackClick = {
                navController.navigateUp()
            }
        )
    }

    composable<Screen.MoviesByCategory>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        MoviesByCategoryScreen(
            onNavigateToMovieDetails = { movieId ->
                navController.navigate(Screen.MovieDetails(movieId))
            },
            onBackClick = {
                navController.navigateUp()
            },
        )
    }

    composable<Screen.ActorDetails> {
        ActorDetailsScreen(
            onNavigateToMoviePicks = { actorId ->
                navController.navigate(Screen.ActorTopMoviesPicksDetails(actorId))
            }, onNavigateToTvShowPicks = { actorId ->
                navController.navigate(Screen.TopTvShowsPicksDetails(actorId))
            },
            onNavigateToGallery = { actorId ->
                navController.navigate(Screen.ActorGallery(actorId))
            },
            onNavigateToMovieScreen = { movieId ->
                navController.navigate(Screen.MovieDetails(movieId))
            },
            onNavigateToTvShowScreen = { tvShowId ->
                navController.navigate(Screen.TvShowDetails(tvShowId))
            },
            onBackClick = { navController.navigateUp() }
        )
    }

    composable<Screen.EpisodeDetails>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        EpisodeDetailsScreen(
            onNavigateBackClick = { navController.popBackStack() },
            onNavigateToCast = { actorId ->
                navController.navigate(Screen.ActorDetails(actorId))
            }
        )
    }
    composable<Screen.ActorGallery> {
        ActorGalleryScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<Screen.TopRated> {
        TopRatedScreen(
            onBackClick = {navController.popBackStack()},
            onMovieClick = {navController.navigate(MovieDetails(it))},
            onTvShowClick = {navController.navigate(TvShowDetails(it))}
        )
    }
}

@Composable
fun NovixApp(appPreferencesService: AppPreferencesService) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = when {
        currentDestination?.hasRoute<Screen.Home>() == true -> Screen.Home
        currentDestination?.hasRoute<Screen.Search>() == true -> Screen.Search
        currentDestination?.hasRoute<Screen.Categories>() == true -> Screen.Categories
        currentDestination?.hasRoute<Screen.Bookmarks>() == true -> Screen.Bookmarks
        currentDestination?.hasRoute<Screen.Account>() == true -> Screen.Account
        currentDestination?.hasRoute<Screen.Login>() == true -> Screen.Login
        else -> Screen.Home
    }

    val showBottomNav = currentDestination?.hasRoute<Screen.Home>() == true ||
            currentDestination?.hasRoute<Screen.Search>() == true ||
            currentDestination?.hasRoute<Screen.Categories>() == true ||
            currentDestination?.hasRoute<Screen.Bookmarks>() == true ||
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
        NavHost(
            navController = navController,
            startDestination = NovixAppNavGraph.Splash,
            modifier = Modifier.padding(innerPadding)
        ) {
            onboardingNavGraph(navController, appPreferencesService)
            splashNavGraph(navController, appPreferencesService)
            authNavGraph(navController)
            mainNavGraph(navController)
        }
    }
}

fun NavController.navigateToAuthGraph() = navigateTo(NovixAppNavGraph.Auth)
fun NavController.navigateToMainGraph() = navigateTo(NovixAppNavGraph.Main)
fun NavController.navigateToSplashGraph() = navigateTo(NovixAppNavGraph.Splash)
fun NavController.navigateToOnboardingGraph() = navigateTo(NovixAppNavGraph.OnBoarding)

fun NavController.navigateTo(
    route: Any,
    popBackStack: Boolean = true
) = runCatching {
    if (currentBackStackEntry?.destination?.hasRoute(route::class) == true) return@runCatching
    navigate(
        route = route,
        builder = {
            if (popBackStack)
                popUpTo(0) {
                    inclusive = true
                    saveState = true
                }
            launchSingleTop = true
            restoreState = true
        }
    )
}.onFailure(Timber::e)


private fun navigateToBottomBarDestination(
    navController: NavHostController,
    destination: Screen
) {
    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}