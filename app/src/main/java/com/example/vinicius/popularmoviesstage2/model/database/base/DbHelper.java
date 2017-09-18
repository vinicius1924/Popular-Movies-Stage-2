package com.example.vinicius.popularmoviesstage2.model.database.base;

import android.database.Cursor;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;

/**
 * Created by vinicius on 11/09/17.
 */

public interface DbHelper
{
	void insertMovie(final MovieDTO movieDTO);
	void deleteMovie(long id, String posterPath, MovieDTO movieDTO);
	Cursor queryMovieById(long id);
}
