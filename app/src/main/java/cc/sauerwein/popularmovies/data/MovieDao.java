package cc.sauerwein.popularmovies.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    List<Movie> loadAllMovies();

    @Query("SELECT * FROM movies WHERE id = :id")
    Movie loadMovieById(int id);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
