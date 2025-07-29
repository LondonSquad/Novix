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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.london.designsystem.component.NavBar
import com.london.designsystem.theme.NovixTheme
import com.london.domain.AppPreferencesService
import com.london.presentation.feature.account.AccountScreen
import com.london.presentation.feature.bookmark.BookmarksScreen
import com.london.presentation.feature.category.CategoriesScreen
import com.london.presentation.feature.category.moviesbycategory.MoviesByCategoryScreen
import com.london.presentation.feature.category.tvshowbycategory.TvShowByCategoryScreen
import com.london.presentation.feature.details.actor.ActorDetailsScreen
import com.london.presentation.feature.details.actordetails.gallery.ActorGalleryScreen
import com.london.presentation.feature.details.actordetails.topmoviespicks.TopMoviesPicksScreen
import com.london.presentation.feature.details.actordetails.toptvshowspicks.TopTvShowsPicksScreen
import com.london.presentation.feature.details.movieDetalis.MovieDetailsScreen
import com.london.presentation.feature.details.tvshow.episodedetails.EpisodeDetailsScreen
import com.london.presentation.feature.details.tvshow.tvshowdetails.TvShowsDetailsScreen
import com.london.presentation.feature.home.HomeScreen
import com.london.presentation.feature.home.trending.actor.TrendingActorsScreen
import com.london.presentation.feature.home.trending.movies.TrendingMoviesScreen
import com.london.presentation.feature.home.trending.tvshows.TrendingTvShowsScreen
import com.london.presentation.feature.login.LoginScreen
import com.london.presentation.feature.onboarding.OnboardingRoute
import com.london.presentation.feature.onboarding.SplashRoute
import com.london.presentation.feature.onboarding.WelcomeScreen
import com.london.presentation.feature.reviews.ReviewsScreen
import com.london.presentation.feature.search.SearchScreen
import com.london.presentation.feature.toprated.TopRatedScreen
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.Screen.Account
import com.london.presentation.navigation.Screen.ActorDetails
import com.london.presentation.navigation.Screen.ActorGallery
import com.london.presentation.navigation.Screen.ActorTopMoviesPicksDetails
import com.london.presentation.navigation.Screen.Bookmarks
import com.london.presentation.navigation.Screen.Categories
import com.london.presentation.navigation.Screen.EpisodeDetails
import com.london.presentation.navigation.Screen.Home
import com.london.presentation.navigation.Screen.Login
import com.london.presentation.navigation.Screen.MovieDetails
import com.london.presentation.navigation.Screen.MoviesByCategory
import com.london.presentation.navigation.Screen.OnboardingPager
import com.london.presentation.navigation.Screen.Reviews
import com.london.presentation.navigation.Screen.Search
import com.london.presentation.navigation.Screen.Splash
import com.london.presentation.navigation.Screen.TopRated
import com.london.presentation.navigation.Screen.TopTvShowsPicksDetails
import com.london.presentation.navigation.Screen.TrendingActors
import com.london.presentation.navigation.Screen.TrendingMovies
import com.london.presentation.navigation.Screen.TrendingTvShows
import com.london.presentation.navigation.Screen.TvShowDetails
import com.london.presentation.navigation.Screen.TvShowsByCategory
import com.london.presentation.navigation.Screen.Welcome
import org.koin.compose.getKoin

@Composable
fun NovixApp(appPreferencesService: AppPreferencesService) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = when {
        currentDestination?.hasRoute<Home>() == true -> Home
        currentDestination?.hasRoute<Search>() == true -> Search
        currentDestination?.hasRoute<Categories>() == true -> Categories
        currentDestination?.hasRoute<Bookmarks>() == true -> Bookmarks
        currentDestination?.hasRoute<Account>() == true -> Account
        currentDestination?.hasRoute<Login>() == true -> Login
        else -> Home
    }

    val showBottomNav = currentDestination?.hasRoute<Home>() == true ||
            currentDestination?.hasRoute<Search>() == true ||
            currentDestination?.hasRoute<Categories>() == true ||
            currentDestination?.hasRoute<Bookmarks>() == true ||
            currentDestination?.hasRoute<Account>() == true


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
            startDestination = Splash,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable<Splash> {
                SplashRoute(
                    onNavigateToOnboarding = { navController.navigate(OnboardingPager) },
                    onNavigateToWelcome = { navController.navigate(Welcome) },
                    onNavigateToHome = {
                        navController.navigate(Home) {
                            popUpTo(Splash) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    appPreferencesService = appPreferencesService,
                    authRepository = getKoin().get(),
                )
            }

            composable<OnboardingPager> {
                OnboardingRoute(
                    onNavigateToWelcome = { navController.navigate(Welcome) },
                    appPreferencesService = appPreferencesService
                )
            }

            composable<Welcome> {
                WelcomeScreen(
                    onNavigateLogin = {
                        navController.navigate(Login){
                            popUpTo(Welcome) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateContinue = {
                           navController.navigate(Home)
                    }
                )
            }

            composable<Login>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                LoginScreen(
                    onNavigateToHome = {
                        navController.navigate(Home) {
                            popUpTo(Login) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Home>(
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
                    onNavigateTopRated = {
                        navController.navigate(TopRated)
                    },
                    onNavigateTrendingMovies = {
                        navController.navigate(TrendingMovies)
                    },
                    onNavigateTrendingTvShows = {
                        navController.navigate(TrendingTvShows)
                    },
                    onNavigateTrendingActors = {
                        navController.navigate(TrendingActors)
                    }
                )
            }

            composable<Search>(
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

            composable<Categories>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                CategoriesScreen()
            }

            composable<Bookmarks>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                BookmarksScreen()
            }

            composable<Account>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                AccountScreen()
            }

            composable<TvShowDetails>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                TvShowsDetailsScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateToEpisodeDetails = { tvShowId, episodeNumber, seasonNumber ->
                        navController.navigate(
                            EpisodeDetails(
                                tvShowId,
                                seasonNumber,
                                episodeNumber
                            )
                        )
                    },
                    onNavigateToReviews = { tvShowId, mediaType ->
                        navController.navigate(Reviews(tvShowId, mediaType))
                    }, onNavigateToCast = { actorId ->
                        navController.navigate(ActorDetails(actorId))
                    },
                    onNavigateToGenre = { genreId ->
                        navController.navigate(TvShowsByCategory(genreId))
                    }
                )
            }

            composable<TopTvShowsPicksDetails>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                TopTvShowsPicksScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateTvShow = { tvShowId ->
                        navController.navigate(TvShowDetails(tvShowId))
                    }
                )
            }

            composable<ActorTopMoviesPicksDetails> {
                TopMoviesPicksScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onNavigateMovie = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    }
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
                        navController.navigate(MoviesByCategory(genreId))
                    },
                    onNavigateToMovie = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    },
                    onNavigateToActor = { actorId ->
                        navController.navigate(ActorDetails(actorId))
                    },
                    onNavigateToReviews = { movieId, mediaType ->
                        navController.navigate(Reviews(movieId, mediaType))
                    }
                )
            }

            composable<Reviews> {
                ReviewsScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable<MoviesByCategory>(
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

            composable<TvShowsByCategory>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                TvShowByCategoryScreen(
                    onNavigateToTvShowDetails = { tvShowId ->
                        navController.navigate(TvShowDetails(tvShowId))
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                )
            }

            composable<ActorDetails> {
                ActorDetailsScreen(
                    onNavigateToMoviePicks = { actorId ->
                        navController.navigate(ActorTopMoviesPicksDetails(actorId))
                    }, onNavigateToTvShowPicks = { actorId ->
                        navController.navigate(TopTvShowsPicksDetails(actorId))
                    },
                    onNavigateToGallery = { actorId ->
                        navController.navigate(ActorGallery(actorId))
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

            composable<EpisodeDetails>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                EpisodeDetailsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCast = { actorId ->
                        navController.navigate(ActorDetails(actorId))
                    }
                )
            }

            composable<ActorGallery> {
                ActorGalleryScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<TopRated> {
                TopRatedScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateMovie = { navController.navigate(MovieDetails(it)) },
                    onNavigateTvShow = { navController.navigate(TvShowDetails(it)) }
                )
            }

            composable<Login> {
                LoginScreen(
                    onNavigateBack = {
                        navController.navigate(Welcome)
                    },
                    onNavigateToHome = {
                        navController.navigate(Home) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
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
        }
    }
}

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