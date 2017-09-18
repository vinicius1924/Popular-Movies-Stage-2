package com.example.vinicius.popularmoviesstage2.model.api;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.RequestQueueSingleton;
import com.example.vinicius.popularmoviesstage2.model.api.base.ApiHelper;

import java.lang.reflect.Type;

/**
 * Created by vinicius on 13/09/17.
 */

public class AppApiHelper<T> implements ApiHelper<T>
{
	@Override
	public void GetPopularMovies(Response.Listener<T> successResponseRequestListener,
										  Response.ErrorListener errorResponseRequestListener, Class<T> clazz,
										  Context context, String requestTag)
	{
		GsonRequest<T> myReq = new GsonRequest<T>(Request.Method.GET, "https://api.themoviedb.org/3/movie/popular?api_key="
				  + context.getResources().getString(R.string.movie_db_api_key),
				  clazz, null, null, null, successResponseRequestListener, errorResponseRequestListener);

		int socketTimeout = 10000;
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);
		myReq.setTag(requestTag);

		RequestQueueSingleton.getInstance(context).addToRequestQueue(myReq);
	}

	@Override
	public void GetTopRatedMovies(Response.Listener<T> successResponseRequestListener,
											Response.ErrorListener errorResponseRequestListener, Class<T> clazz,
											Context context, String requestTag)
	{
		GsonRequest<T> myReq = new GsonRequest<T>(Request.Method.GET, "https://api.themoviedb.org/3/movie/top_rated?api_key="
				  + context.getResources().getString(R.string.movie_db_api_key),
				  clazz, null, null, null, successResponseRequestListener, errorResponseRequestListener);

		int socketTimeout = 10000;
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);
		myReq.setTag(requestTag);

		RequestQueueSingleton.getInstance(context).addToRequestQueue(myReq);
	}

	@Override
	public void GetMovieDetails(Response.Listener<T> successResponseRequestListener,
										 Response.ErrorListener errorResponseRequestListener, Type type, Context context,
										 String requestTag, long id)
	{
		GsonRequest<T> myReq = new GsonRequest<T>(Request.Method.GET, "https://api.themoviedb.org/3/movie/" +
				  String.valueOf(id) + "?api_key=" + context.getResources().getString(R.string.movie_db_api_key),
				  null, type, null, null, successResponseRequestListener, errorResponseRequestListener);

		int socketTimeout = 10000;
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);
		myReq.setTag(requestTag);

		RequestQueueSingleton.getInstance(context).addToRequestQueue(myReq);
	}

	@Override
	public void GetMovieVideos(Response.Listener<T> successResponseRequestListener,
										Response.ErrorListener errorResponseRequestListener, Class<T> clazz, Context context,
										String requestTag, long id)
	{
		GsonRequest<T> myReq = new GsonRequest<T>(Request.Method.GET, "https://api.themoviedb.org/3/movie/" +
				  String.valueOf(id) + "/videos?api_key=" + context.getResources().getString(R.string.movie_db_api_key),
				  clazz, null, null, null, successResponseRequestListener, errorResponseRequestListener);

		int socketTimeout = 10000;
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);
		myReq.setTag(requestTag);

		RequestQueueSingleton.getInstance(context).addToRequestQueue(myReq);
	}

	@Override
	public void GetMovieReviews(Response.Listener<T> successResponseRequestListener, Response.ErrorListener errorResponseRequestListener, Class<T> clazz, Context context, String requestTag, long id)
	{
		GsonRequest<T> myReq = new GsonRequest<T>(Request.Method.GET, "https://api.themoviedb.org/3/movie/" +
				  String.valueOf(id) + "/reviews?api_key=" + context.getResources().getString(R.string.movie_db_api_key),
				  clazz, null, null, null, successResponseRequestListener, errorResponseRequestListener);

		int socketTimeout = 10000;
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		myReq.setRetryPolicy(policy);
		myReq.setTag(requestTag);

		RequestQueueSingleton.getInstance(context).addToRequestQueue(myReq);
	}
}
