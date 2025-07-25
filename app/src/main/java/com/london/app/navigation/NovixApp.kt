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
import com.london.presentation.navigation.Screen.TrendingActors
import com.london.presentation.navigation.Screen.TrendingMovies
import com.london.presentation.navigation.Screen.TrendingTvShows
import com.london.presentation.navigation.Screen.TvShowDetails
import com.london.presentation.navigation.Screen.Welcome
import com.london.presentation.screen.account.AccountScreen
import com.london.presentation.screen.bookmark.BookmarksScreen
import com.london.presentation.screen.category.CategoriesScreen
import com.london.presentation.screen.category.moviesbycategory.MoviesByCategoryScreen
import com.london.presentation.screen.details.actor.ActorDetailsScreen
import com.london.presentation.screen.details.actordetails.gallery.ActorGalleryScreen
import com.london.presentation.screen.details.actordetails.topmoviespicks.TopMoviesPicksScreen
import com.london.presentation.screen.details.actordetails.toptvshowspicks.TopTvShowsPicksScreen
import com.london.presentation.screen.details.movieDetalis.MovieDetailsScreen
import com.london.presentation.screen.details.tvshow.episodedetails.EpisodeDetailsScreen
import com.london.presentation.screen.details.tvshow.tvshowdetails.TvShowsDetailsScreen
import com.london.presentation.screen.home.HomeScreen
import com.london.presentation.screen.home.trending.actor.TrendingActorsContract
import com.london.presentation.screen.home.trending.actor.TrendingActorsScreen
import com.london.presentation.screen.home.trending.movie.TrendingMoviesContract
import com.london.presentation.screen.home.trending.movie.TrendingMoviesScreen
import com.london.presentation.screen.home.trending.tvshow.TrendingTvShowsContract
import com.london.presentation.screen.home.trending.tvshow.TrendingTvShowsScreen
import com.london.presentation.screen.login.LoginScreen
import com.london.presentation.screen.onboarding.OnboardingRoute
import com.london.presentation.screen.onboarding.SplashRoute
import com.london.presentation.screen.onboarding.WelcomeScreen
import com.london.presentation.screen.reviews.ReviewsScreen
import com.london.presentation.screen.search.SearchScreen
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
            startDestination = Screen.Splash,
            modifier = Modifier.padding(innerPadding)
        ) {

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
                    onMovieClick = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    },

                    onTvShowClick = { tvShowId ->
                        navController.navigate(TvShowDetails(tvShowId))
                    },
                    onTrendingMovies = {
                        navController.navigate(TrendingMovies)
                    },
                    onTrendingTvShows = {
                        navController.navigate(TrendingTvShows)
                    },
                    onTrendingActors = {
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
                    onBackClick = {
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
                        navController.navigate(TvShowDetails(tvShowId))
                    }
                )
            }
            composable<ActorTopMoviesPicksDetails> {
                TopMoviesPicksScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onMovieClick = { movieId ->
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
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onGenreClick = { genreId ->
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
                    onBackClick = {
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
                    onBackClick = {
                        navController.navigateUp()
                    },
                )
            }

            composable<ActorDetails> {
                ActorDetailsScreen(
                    onNavigateToMoviePicks = { actorId ->
                        navController.navigate(ActorTopMoviesPicksDetails(actorId))
                    }, onNavigateToTvShowPicks = { actorId ->
                        navController.navigate(Screen.TopTvShowsPicksDetails(actorId))
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
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable<EpisodeDetails>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                EpisodeDetailsScreen(
                    onNavigateBackClick = { navController.popBackStack() },
                    onNavigateToCast = { actorId ->
                        navController.navigate(ActorDetails(actorId))
                    }
                )
            }
            composable<ActorGallery> {
                ActorGalleryScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable<Splash> {
                SplashRoute(
                    onNavigateToOnboarding = { navController.navigate(Screen.OnboardingPager) },
                    onNavigateToWelcome = { navController.navigate(Screen.Welcome) },
                    onNavigateToHome = { navController.navigate(Home) },
                    appPreferencesService = appPreferencesService,
                    authRepository = getKoin().get(),
                )
            }

            composable<OnboardingPager> {
                OnboardingRoute(
                    onNavigateToWelcome = { navController.navigate(Screen.Welcome) },
                    appPreferencesService = appPreferencesService
                )
            }

            composable<Welcome> {
                WelcomeScreen(
                    onLoginClicked = {
                        navController.navigate(Login)
                    },
                    onContinueClicked = {
                        navController.navigate(Home)
                    }
                )
            }

            composable<Login> {
                LoginScreen(
                    onNavigateBack = {
                        navController.navigate(Screen.Welcome)
                    },
                    onNavigateToHome = {
                        navController.navigate(Home)
                    },
                )
            }
            composable<TrendingMovies> {
                TrendingMoviesScreen(
                    contract = object : TrendingMoviesContract {
                        override fun onMovieClick(id: Int) {
                            navController.navigate(MovieDetails(id))
                        }

                        override fun onBackClick() {
                            navController.navigateUp()
                        }
                    }
                )
            }
            composable<TrendingTvShows> {
                TrendingTvShowsScreen(
                    contract = object : TrendingTvShowsContract {
                        override fun onTvShowClick(id: Int) {
                            navController.navigate(TvShowDetails(id))
                        }

                        override fun onBackClick() {
                            navController.navigateUp()
                        }
                    }
                )
            }
            composable<TrendingActors> {
                TrendingActorsScreen(
                    contract = object : TrendingActorsContract {
                        override fun onActorClick(id: Int) {
                            navController.navigate(ActorDetails(id))
                        }

                        override fun onBackClick() {
                            navController.navigateUp()
                        }
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