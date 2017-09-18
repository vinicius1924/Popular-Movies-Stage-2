package com.example.vinicius.popularmoviesstage2.model.api.base;

import android.content.Context;

import com.android.volley.Response;

import java.lang.reflect.Type;

/**
 * Created by vinicius on 13/09/17.
 */

public interface ApiHelper<T>
{
	void GetPopularMovies(Response.Listener<T> successResponseRequestListener,
								 Response.ErrorListener errorResponseRequestListener,
								 Class<T> clazz, Context context, String requestTag);

	void GetTopRatedMovies(Response.Listener<T> successResponseRequestListener,
								  Response.ErrorListener errorResponseRequestListener,
								  Class<T> clazz, Context context, String requestTag);

	void GetMovieDetails(Response.Listener<T> successResponseRequestListener,
								Response.ErrorListener errorResponseRequestListener,
								Type type, Context context, String requestTag, long id);

	void GetMovieVideos(Response.Listener<T> successResponseRequestListener,
							  Response.ErrorListener errorResponseRequestListener,
							  Class<T> clazz, Context context, String requestTag, long id);

	void GetMovieReviews(Response.Listener<T> successResponseRequestListener,
								Response.ErrorListener errorResponseRequestListener,
								Class<T> clazz, Context context, String requestTag, long id);
}
