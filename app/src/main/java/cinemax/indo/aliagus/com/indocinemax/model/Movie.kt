package cinemax.indo.aliagus.com.indocinemax.model

import java.io.Serializable

/**
 * Created by ali on 26/02/18.
 */
class Movie: Serializable {
    lateinit var id: String
    lateinit var voteAverage: String
    lateinit var title: String
    lateinit var popularity: String
    lateinit var posterPath: String
    lateinit var overView: String
    lateinit var releaseDate: String
    lateinit var genresList: List<String>
}