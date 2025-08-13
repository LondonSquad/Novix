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
        currentDestination?.hasRoute<Screen.Lists>() == true -> Screen.Lists
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

fun NavController.navigateToAuthGraph() = navigateTo(NovixAppNavGraph.Auth)
fun NavController.navigateToMainGraph() = navigateTo(NovixAppNavGraph.Main)
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
) = navigation<NovixAppNavGraph.Splash>(startDestination = Screen.Splash) {
    composable<Screen.Splash> {
        SplashRoute(
            onNavigateToOnboarding = { navController.navigateToOnboardingGraph() },
            onNavigateToWelcome = { navController.navigateTo(Screen.OnBoarding.Welcome) },
            onNavigateToHome = { navController.navigateToMainGraph() },
        )
    }
}

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavHostController,
) = navigation<NovixAppNavGraph.OnBoarding>(startDestination = Screen.OnBoarding) {
    composable<Screen.OnBoarding> {
        OnboardingRoute(
            onNavigateToWelcome = { navController.navigateTo(Screen.OnBoarding.Welcome) },
        )
    }

    composable<Screen.OnBoarding.Welcome> {
        WelcomeScreen(
            onNavigateLogin = { navController.navigateToAuthGraph() },
            onNavigateContinue = { navController.navigateToMainGraph() }
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
                navController.navigate(Screen.OnBoarding.Welcome)
            },
            onNavigateToWebViewRegistration = {
                navController.navigate(Screen.Register)
            }
        )
    }

    composable<Screen.Register>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        RegistrationScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onRegistrationComplete = { navController.navigate(Screen.Login) }
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
            onNavigateMovie = { movieId ->
                navController.navigate(MovieDetails(movieId))
            },
            onNavigateTvShow = { tvShowId ->
                navController.navigate(TvShowDetails(tvShowId))
            },
            onNavigateTopRated = { navController.navigate(Screen.TopRated) },
            onNavigateTrendingMovies = { navController.navigate(TrendingMovies) },
            onNavigateTrendingTvShows = { navController.navigate(TrendingTvShows) },
            onNavigateTrendingActors = { navController.navigate(TrendingActors) },
            onNavigateContinueWatching = { navController.navigate(Screen.ContinueWatching) }
        )
    }
    composable<TrendingMovies> {
        TrendingMoviesScreen(
            onNavigateMovie = { id ->
                navController.navigate(MovieDetails(id))
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
    composable<TrendingTvShows> {
        TrendingTvShowsScreen(
            onNavigateTvShow = { id ->
                navController.navigate(TvShowDetails(id))
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
    composable<TrendingActors> {
        TrendingActorsScreen(
            onNavigateActor = { id ->
                navController.navigate(ActorDetails(id))
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }

    composable<Screen.TvShowsByCategory>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        TvShowByCategoryScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToTvShowDetails = { tvShowId ->
                navController.navigate(TvShowDetails(tvShowId))
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
                navController.navigate(TvShowDetails(tvShowId))
            },
            onNavigateToActorDetails = { actorId ->
                navController.navigate(ActorDetails(actorId))
            },
            onNavigateToMovieDetails = { movieId ->
                navController.navigate(MovieDetails(movieId))
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

    composable<Screen.Lists>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        ListScreen(
            onNavigateToDetails = {
                navController.navigate(Screen.ViewListItems(listId = it))
            },
            onNavigateToLogin = {
                navController.navigate(Screen.Login) {
                    popUpTo(NovixAppNavGraph.Main) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<Screen.Account>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        AccountScreen(
            onNavigateToWatchingHistory = { navController.navigate(WatchingHistory) },
            onNavigateToMyRating = { navController.navigate(Screen.MyRating) },
            onNavigateToLogin = {
                navController.navigate(Screen.Login) {
                    popUpTo(NovixAppNavGraph.Main) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<WatchingHistory>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        ContinueWatchingScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onMovieClick = { id ->
                navController.navigate(MovieDetails(id))
            },
            onTvShowClick = { id ->
                navController.navigate(TvShowDetails(id))
            },
            screenTitle = stringResource(R.string.watching_history)
        )
    }

    composable<Screen.MyRating>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        MyRatingScreen(
            onNavigateMovie = { movieId ->
                navController.navigate(MovieDetails(movieId))
            },
            onNavigateTvShow = { tvShowId ->
                navController.navigate(TvShowDetails(tvShowId))
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }

    composable<TvShowDetails>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        TvShowsDetailsScreen(
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
                navController.navigate(ActorDetails(actorId))
            },
            onNavigateBack = { navController.navigateUp() },
            onNavigateToGenre = { genreId ->
                navController.navigate(Screen.TvShowsByCategory(genreId))
            },
            onNavigateToLogin = { navController.navigate(Screen.Login) }
        )
    }

    composable<Screen.TopTvShowsPicksDetails>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        TopTvShowsPicksScreen(
            onNavigateTvShow = { tvShowId ->
                navController.navigate(TvShowDetails(tvShowId))
            },
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
    composable<Screen.ActorTopMoviesPicksDetails> {
        TopMoviesPicksScreen(
            onNavigateMovie = { movieId ->
                navController.navigate(MovieDetails(movieId))
            },
            onNavigateBack = { navController.navigateUp() },
        )
    }

    composable<MovieDetails>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        MovieDetailsScreen(
            onNavigateBack = {
                navController.navigateUp()
            },
            onNavigateGenre = { genreId ->
                navController.navigate(Screen.MoviesByCategory(genreId))
            },
            onNavigateToMovie = { movieId ->
                navController.navigate(MovieDetails(movieId))
            },
            onNavigateToActor = { actorId ->
                navController.navigate(ActorDetails(actorId))
            },
            onNavigateToReviews = { movieId, mediaType ->
                navController.navigate(Screen.Reviews(movieId, mediaType))
            },
            onNavigateToLogin = {
                navController.navigate(Screen.Login)
            }
        )
    }

    composable<Screen.Reviews> {
        ReviewsScreen(
            onNavigateBack = {
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
                navController.navigate(MovieDetails(movieId))
            },
            onNavigateBack = {
                navController.navigateUp()
            },
        )
    }

    composable<ActorDetails> {
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
                navController.navigate(MovieDetails(movieId))
            },
            onNavigateToTvShowScreen = { tvShowId ->
                navController.navigate(TvShowDetails(tvShowId))
            },
            onNavigateBack = { navController.navigateUp() }
        )
    }

    composable<Screen.EpisodeDetails>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        EpisodeDetailsScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCast = { actorId ->
                navController.navigate(ActorDetails(actorId))
            },
            onNavigateLogin = { navController.navigate(Screen.Login) }
        )
    }
    composable<Screen.ActorGallery> {
        ActorsGalleryScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }

    composable<Screen.TopRated> {
        TopRatedScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateMovie = { navController.navigate(MovieDetails(it)) },
            onNavigateTvShow = { navController.navigate(TvShowDetails(it)) }
        )
    }

    composable<Screen.ContinueWatching> {
        ContinueWatchingScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onMovieClick = { id ->
                navController.navigate(MovieDetails(id))
            },
            onTvShowClick = { id ->
                navController.navigate(TvShowDetails(id))
            },
            screenTitle = stringResource(R.string.continue_watch)
        )
    }

    composable<Screen.ViewListItems>(
        exitTransition = { fadeOut(tween(500)) },
        popEnterTransition = { fadeIn(tween(500)) },
        enterTransition = { fadeIn(tween(500)) },
        popExitTransition = { fadeOut(tween(500)) },
    ) {
        ViewListItemsScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToMovieDetails = {
                navController.navigate(MovieDetails(it))
            },
        )
    }
}

private fun navigateToBottomBarDestination(
    navController: NavHostController,
    destination: Screen
) {
    if (navController.currentBackStackEntry?.destination?.hasRoute(destination::class) == true) {
        return
    }

    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}