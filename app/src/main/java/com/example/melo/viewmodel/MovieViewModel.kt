package com.example.melo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.melo.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieViewModel : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    init {
        loadMovies()
    }

    private fun loadMovies() {
        val moviesList = listOf(
            Movie(
                id = 1,
                title = "Avengers: Endgame",
                imageUrl = "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
                rating = 4.8f,
                genre = "Acción, Aventura",
                description = "Los Vengadores restantes deben encontrar una manera de recuperar a sus aliados para un enfrentamiento épico con Thanos.",
                duration = "3h 1m"
            ),
            Movie(
                id = 2,
                title = "Joker",
                imageUrl = "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
                rating = 4.5f,
                genre = "Drama, Crimen",
                description = "Un comediante fallido se vuelve loco y se convierte en un criminal psicópata.",
                duration = "2h 2m"
            ),
            Movie(
                id = 3,
                title = "Parásitos",
                imageUrl = "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
                rating = 4.6f,
                genre = "Drama, Thriller",
                description = "La familia Kim, todos desempleados, se interesan por la vida de la rica familia Park.",
                duration = "2h 12m"
            ),
            Movie(
                id = 4,
                title = "Spider-Man: No Way Home",
                imageUrl = "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                rating = 4.7f,
                genre = "Acción, Aventura",
                description = "Peter Parker busca la ayuda del Doctor Strange para olvidar su identidad como Spider-Man.",
                duration = "2h 28m"
            ),
            Movie(
                id = 5,
                title = "Dune",
                imageUrl = "https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg",
                rating = 4.4f,
                genre = "Ciencia ficción, Aventura",
                description = "Paul Atreides lidera nómadas en una batalla para controlar el planeta desértico Arrakis.",
                duration = "2h 35m"
            ),
            Movie(
                id = 6,
                title = "Top Gun: Maverick",
                imageUrl = "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg",
                rating = 4.6f,
                genre = "Acción, Drama",
                description = "Después de más de 30 años de servicio, Pete 'Maverick' Mitchell sigue siendo un piloto de pruebas.",
                duration = "2h 10m"
            ),
            Movie(
                id = 7,
                title = "Black Panther",
                imageUrl = "https://image.tmdb.org/t/p/w500/uxzzxijgPIY7slzFvMotPv8wjKA.jpg",
                rating = 4.5f,
                genre = "Acción, Aventura",
                description = "T'Challa regresa a casa para ser coronado rey de Wakanda, pero se ve envuelto en un conflicto.",
                duration = "2h 14m"
            ),
            Movie(
                id = 8,
                title = "Inception",
                imageUrl = "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
                rating = 4.7f,
                genre = "Ciencia ficción, Acción",
                description = "Un ladrón que roba secretos corporativos a través de la tecnología de compartir sueños.",
                duration = "2h 28m"
            )
        )

        _movies.value = moviesList
    }
}
