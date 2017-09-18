package com.example.vinicius.popularmoviesstage2.presenter.movie_activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.RequestQueueSingleton;
import com.example.vinicius.popularmoviesstage2.dependency_injection.PerActivity;
import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.presenter.base.BasePresenter;
import com.example.vinicius.popularmoviesstage2.model.api.GetReviewsResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetVideosResponse;
import com.example.vinicius.popularmoviesstage2.utils.NetworkUtils;
import com.example.vinicius.popularmoviesstage2.utils.VolleyUtils;
import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieActivity;
import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieMvpView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by vinicius on 18/09/17.
 */

@PerActivity
public class MoviePresenter<V extends MovieMvpView> extends BasePresenter<V> implements MovieMvpPresenter<V>
{
	@Named("ActivityContext")Context context;
	@Named("ApplicationContext") Context applicationContext;
	private MovieDTO movieDTO;
	AppCompatActivity activity;

	@Inject
	public MoviePresenter(@Named("ActivityContext")Context context,
								 @Named("ApplicationContext") Context applicationContext, DataManager dataManager,
								 AppCompatActivity activity)
	{
		super(dataManager);
		this.context = context;
		this.applicationContext = applicationContext;
		this.activity = activity;
	}

	@Override
	public void onCreate()
	{
		Bundle bundle = activity.getIntent().getExtras();
		movieDTO = bundle.getParcelable(MovieDTO.PARCELABLE_KEY);
	}

	@Override
	public void loadMovieTrailers(long id)
	{
		getMvpView().progressBarTrailerVisibility(View.VISIBLE);

		final Response.Listener<GetVideosResponse> successResponseRequestListener = new Response.Listener<GetVideosResponse>()
		{
			@Override
			public void onResponse(GetVideosResponse response)
			{
				getMvpView().loadMovieTrailersResponse(response);
			}
		};

		final Response.ErrorListener errorResponseRequestListener = new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Log.e(MovieActivity.MOVIEACTIVITYTAG, error.getLocalizedMessage());
			}
		};

		if(NetworkUtils.isOnline(context))
		{
			getDataManager().GetMovieVideos(successResponseRequestListener, errorResponseRequestListener,
					  GetVideosResponse.class, context, ((MovieActivity)getMvpView()).TRAILERSREQUESTTAG, id);
		}
		else
		{
			getMvpView().showSnackBar(context.getResources().getString(R.string.no_internet_connection));
		}
	}

	@Override
	public void loadMovieReviews(long id)
	{
		getMvpView().progressBarReviewsVisibility(View.VISIBLE);

		final Response.Listener<GetReviewsResponse> successResponseRequestListener = new Response.Listener<GetReviewsResponse>()
		{
			@Override
			public void onResponse(GetReviewsResponse response)
			{
				getMvpView().loadMovieReviewsResponse(response);
			}
		};

		final Response.ErrorListener errorResponseRequestListener = new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Log.e(MovieActivity.MOVIEACTIVITYTAG, error.getLocalizedMessage());
			}
		};

		if(NetworkUtils.isOnline(context))
		{
			getDataManager().GetMovieReviews(successResponseRequestListener, errorResponseRequestListener,
					  GetReviewsResponse.class, context, ((MovieActivity)getMvpView()).REVIEWSREQUESTTAG, id);
		}
		else
		{
			getMvpView().showSnackBar(context.getResources().getString(R.string.no_internet_connection));
		}
	}

	@Override
	public boolean isTrailersRequestsCanceled()
	{
		try
		{
			int numberOfRequests = VolleyUtils.getNumberOfRequestsInQueue(applicationContext);

			if(numberOfRequests > 0)
			{
				if(VolleyUtils.isPendingToRequest(applicationContext, MovieActivity.TRAILERSREQUESTTAG))
				{
					RequestQueueSingleton.getInstance(applicationContext).getRequestQueue().cancelAll(
							  MovieActivity.TRAILERSREQUESTTAG);
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch(NoSuchFieldException e)
		{
			Log.e(MovieActivity.MOVIEACTIVITYTAG, e.toString());
			return false;
		}
	}

	@Override
	public boolean isReviewsRequestsCanceled()
	{
		try
		{
			int numberOfRequests = VolleyUtils.getNumberOfRequestsInQueue(applicationContext);

			if(numberOfRequests > 0)
			{
				if(VolleyUtils.isPendingToRequest(applicationContext, MovieActivity.REVIEWSREQUESTTAG))
				{
					RequestQueueSingleton.getInstance(applicationContext).getRequestQueue().cancelAll(
							  MovieActivity.REVIEWSREQUESTTAG);

					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch(NoSuchFieldException e)
		{
			Log.e(MovieActivity.MOVIEACTIVITYTAG, e.toString());
			return false;
		}
	}

	@Override
	public void deleteMovieFromLocalDatabase(long id, String posterPath)
	{
		getDataManager().deleteMovie(id, posterPath, movieDTO);
	}

	@Override
	public void addMovieToLocalDatabase(MovieDTO movieDTO)
	{
		getDataManager().insertMovie(movieDTO);
	}

	@Override
	public Cursor findMovieById(long id)
	{
		return getDataManager().queryMovieById(id);
	}
}
