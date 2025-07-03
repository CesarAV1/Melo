package com.example.melo.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.melo.model.Movie
import com.example.melo.navigation.Screen
import com.example.melo.ui.theme.*
import com.example.melo.viewmodel.AuthViewModel
import com.example.melo.viewmodel.MovieViewModel
import com.example.melo.viewmodel.TicketViewModel
import kotlinx.coroutines.delay
<<<<<<< HEAD
import androidx.compose.foundation.lazy.itemsIndexed
=======
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    movieViewModel: MovieViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    ticketViewModel: TicketViewModel = viewModel()
) {
    val movies by movieViewModel.movies.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            ticketViewModel.loadTicketsForUser(currentUser!!.id)
        } else {
            ticketViewModel.clearTickets()
        }
    }

    val featuredMovies = movies.take(4)
    val allMovies = movies

    Scaffold(
        containerColor = DarkGreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CINE MELO",
                        color = LightBeige,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen
                ),
                actions = {
                    if (isLoggedIn && currentUser != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Hola, ${currentUser!!.name.split(" ").first()}",
                                color = LightBeige,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            AnimatedButton(
                                onClick = { navController.navigate(Screen.Profile.route) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Perfil",
                                    tint = GoldButton
                                )
                            }
                        }
                    } else {
                        AnimatedTextButton(
                            onClick = { navController.navigate(Screen.Login.route) },
                            text = "LOGIN"
                        )
                        AnimatedTextButton(
                            onClick = { navController.navigate(Screen.Register.route) },
                            text = "REGISTER"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkGreen)
        ) {
            EnhancedFeaturedMoviesCarousel(featuredMovies, navController)

            Spacer(modifier = Modifier.height(24.dp))

            CarteleraSection(allMovies, navController)
        }
    }
}

@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "button_scale"
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = !isPressed
            }
    ) {
        content()
    }
}

@Composable
fun AnimatedTextButton(
    onClick: () -> Unit,
    text: String
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "text_button_scale"
    )

    TextButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier.scale(scale)
    ) {
        Text(
            text,
            color = LightBeige,
            fontWeight = FontWeight.Bold
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}
@Composable
fun EnhancedFeaturedMoviesCarousel(movies: List<Movie>, navController: NavController) {
    val listState = rememberLazyListState()
<<<<<<< HEAD
    var currentIndex by remember { mutableStateOf(0) }
=======
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd

    LaunchedEffect(movies) {
        if (movies.isNotEmpty()) {
            while (true) {
                delay(2000)
<<<<<<< HEAD
                val nextIndex = if (currentIndex == movies.lastIndex) 0 else currentIndex + 1
                currentIndex = nextIndex
                listState.animateScrollToItem(currentIndex)
=======
                val nextIndex = (listState.firstVisibleItemIndex + 1) % movies.size
                listState.animateScrollToItem(index = nextIndex)
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            }
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MediumGreen)
                .padding(16.dp)
        ) {
            Text(
                text = "ESTRENO",
                color = LightBeige,
<<<<<<< HEAD
                fontSize = 21.sp,
=======
                fontSize = 18.sp,
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
                fontWeight = FontWeight.Bold
            )
        }

        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.background(MediumGreen)
        ) {
<<<<<<< HEAD
            itemsIndexed(movies) { index, movie ->
                val isSelected = index == currentIndex
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.1f else 0.95f,
                    animationSpec = tween(durationMillis = 600),
                    label = "carousel_card_scale"
                )
                val alpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0.6f,
                    animationSpec = tween(durationMillis = 600),
                    label = "carousel_card_alpha"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        }
                ) {
                    AnimatedFeaturedMovieCard(movie, navController)
                }
=======
            items(movies) { movie ->
                AnimatedFeaturedMovieCard(movie, navController)
>>>>>>> c0be89d2374180835bb4ad8494770ad1acb0e4cd
            }
        }
    }
}
@Composable
fun AnimatedFeaturedMovieCard(movie: Movie, navController: NavController) {
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.05f else 1f,
        animationSpec = CineMeloAnimations.cardHover,
        label = "card_scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isHovered) 8f else 4f,
        animationSpec = CineMeloAnimations.cardHover,
        label = "card_elevation"
    )

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(240.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = LightBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            AnimatedRatingBadge(
                rating = movie.rating,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            AnimatedTitleOverlay(
                title = movie.title,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    LaunchedEffect(isHovered) {
        if (isHovered) {
            delay(200)
            isHovered = false
        }
    }
}

@Composable
fun AnimatedRatingBadge(
    rating: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rating_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(GoldButton.copy(alpha = glowAlpha))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$rating",
            color = DarkText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AnimatedTitleOverlay(
    title: String,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = 0.8f,
        animationSpec = CineMeloAnimations.fadeInOut,
        label = "title_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color.Black.copy(alpha = alpha)
            )
            .padding(8.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CarteleraSection(movies: List<Movie>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBeige)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MediumGreen)
                .padding(16.dp)
        ) {
            Text(
                text = "CARTELERA",
                color = LightBeige,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(movies) { movie ->
                AnimatedCarteleraMovieCard(movie, navController)
            }
        }
    }
}

@Composable
fun AnimatedCarteleraMovieCard(movie: Movie, navController: NavController) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = CineMeloAnimations.cardHover,
        label = "cartelera_card_scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 0f,
        animationSpec = CineMeloAnimations.cardHover,
        label = "cartelera_card_rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 8.dp else 4.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            AnimatedRatingBadge(
                rating = movie.rating,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            AnimatedTitleOverlay(
                title = movie.title,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200)
            isPressed = false
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = DarkGreen,
        contentColor = LightBeige
    ) {
        AnimatedNavigationBarItem(
            icon = Icons.Default.Home,
            contentDescription = "Home",
            selected = true,
            onClick = { }
        )
        AnimatedNavigationBarItem(
            icon = Icons.Default.ShoppingCart,
            contentDescription = "Cart",
            selected = false,
            onClick = { navController.navigate(Screen.Cart.route) }
        )
        AnimatedNavigationBarItem(
            icon = Icons.Default.Person,
            contentDescription = "Profile",
            selected = false,
            onClick = { navController.navigate(Screen.Profile.route) }
        )
    }
}

@Composable
fun RowScope.AnimatedNavigationBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = CineMeloAnimations.buttonScale,
        label = "nav_item_scale"
    )

    NavigationBarItem(
        icon = {
            Icon(
                icon,
                contentDescription = contentDescription,
                tint = if (selected) GoldButton else LightBeige,
                modifier = Modifier.scale(scale)
            )
        },
        selected = selected,
        onClick = {
            isPressed = true
            onClick()
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = GoldButton,
            unselectedIconColor = LightBeige,
            indicatorColor = Color.Transparent
        )
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}
