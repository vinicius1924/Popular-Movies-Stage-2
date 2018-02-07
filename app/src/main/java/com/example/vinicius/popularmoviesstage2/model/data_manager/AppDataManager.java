package com.example.vinicius.popularmoviesstage2.model.data_manager;

import android.database.Cursor;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.model.api.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetReviewsResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetVideosResponse;
import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.model.database.base.DbHelper;
import com.example.vinicius.popularmoviesstage2.model.api.base.ApiHelper;
import com.example.vinicius.popularmoviesstage2.model.preferences.base.PreferenceHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Created by vinicius on 11/09/17.
 */

@Singleton
public class AppDataManager implements DataManager
{
	private final DbHelper mDbHelper;
	private final ApiHelper mApiHelper;
	private final PreferenceHelper mPreferencesHelper;

	@Inject
	public AppDataManager(DbHelper dbHelper, ApiHelper apiHelper, PreferenceHelper preferenceHelper)
	{
		this.mDbHelper = dbHelper;
		this.mApiHelper = apiHelper;
		this.mPreferencesHelper = preferenceHelper;
	}

	@Override
	public void insertMovie(final MovieDTO movieDTO)
	{
		mDbHelper.insertMovie(movieDTO);
	}

	@Override
	public void deleteMovie(long id, String posterPath, MovieDTO movieDTO)
	{
		mDbHelper.deleteMovie(id, posterPath, movieDTO);
	}

	@Override
	public Cursor queryMovieById(long id)
	{
		return mDbHelper.queryMovieById(id);
	}

	@Override
	public Observable<GetMoviesResponse> getPopularMovies()
	{
		return mApiHelper.getPopularMovies();
	}

	@Override
	public Observable<GetMoviesResponse> getTopRatedMovies()
	{
		return mApiHelper.getTopRatedMovies();
	}

	@Override
	public Observable<GetVideosResponse> getMovieVideos(String id)
	{
		return mApiHelper.getMovieVideos(id);
	}

	@Override
	public Observable<GetReviewsResponse> getMovieReviews(String id)
	{
		return mApiHelper.getMovieReviews(id);
	}

	@Override
	public void setMoviesSortBy(String orderBy)
	{
		mPreferencesHelper.setMoviesSortBy(orderBy);
	}

	@Override
	public String getMoviesSortBy()
	{
		return mPreferencesHelper.getMoviesSortBy();
	}
}
