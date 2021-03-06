package cc.sauerwein.popularmovies.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cc.sauerwein.popularmovies.model.Movie;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<Movie> loadMovieById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
