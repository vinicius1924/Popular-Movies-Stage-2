package com.example.vinicius.popularmoviesstage2.model.data_manager;

import android.content.Context;
import android.database.Cursor;

import com.android.volley.Response;
import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.model.database.base.DbHelper;
import com.example.vinicius.popularmoviesstage2.model.api.base.ApiHelper;
import com.example.vinicius.popularmoviesstage2.model.preferences.base.PreferenceHelper;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

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
	public void GetPopularMovies(Response.Listener successResponseRequestListener, Response.ErrorListener
			  errorResponseRequestListener, Class clazz, Context context, String requestTag)
	{
		mApiHelper.GetPopularMovies(successResponseRequestListener, errorResponseRequestListener, clazz,
				  context, requestTag);
	}

	@Override
	public void GetTopRatedMovies(Response.Listener successResponseRequestListener, Response.ErrorListener
			  errorResponseRequestListener, Class clazz, Context context, String requestTag)
	{
		mApiHelper.GetTopRatedMovies(successResponseRequestListener, errorResponseRequestListener, clazz,
				  context, requestTag);
	}

	@Override
	public void GetMovieDetails(Response.Listener successResponseRequestListener, Response.ErrorListener errorResponseRequestListener, Type type, Context context, String requestTag, long id)
	{
		mApiHelper.GetMovieDetails(successResponseRequestListener, errorResponseRequestListener, type,
				  context, requestTag, id);
	}

	@Override
	public void GetMovieVideos(Response.Listener successResponseRequestListener, Response.ErrorListener errorResponseRequestListener, Class clazz, Context context, String requestTag, long id)
	{
		mApiHelper.GetMovieVideos(successResponseRequestListener, errorResponseRequestListener, clazz,
				  context, requestTag, id);
	}

	@Override
	public void GetMovieReviews(Response.Listener successResponseRequestListener, Response.ErrorListener errorResponseRequestListener, Class clazz, Context context, String requestTag, long id)
	{
		mApiHelper.GetMovieReviews(successResponseRequestListener, errorResponseRequestListener, clazz,
				  context, requestTag, id);
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
