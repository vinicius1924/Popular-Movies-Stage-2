package com.example.vinicius.popularmoviesstage2.model.api;

import com.example.vinicius.popularmoviesstage2.model.api.base.ApiHelper;
import com.example.vinicius.popularmoviesstage2.model.data_manager.IApiServices;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by vinicius on 13/09/17.
 */

public class AppApiHelper<T> implements ApiHelper<T>
{
	private Retrofit retrofit;
	private IApiServices apiServices;

	public AppApiHelper(Retrofit retrofit)
	{
		this.retrofit = retrofit;
		apiServices = retrofit.create(IApiServices.class);
	}

	@Override
	public Observable<GetMoviesResponse> getPopularMovies()
	{
		return apiServices.getPopularMovies();
	}

	@Override
	public Observable<GetMoviesResponse> getTopRatedMovies()
	{
		return apiServices.getTopRatedMovies();
	}

	@Override
	public Observable<GetVideosResponse> getMovieVideos(String id)
	{
		return apiServices.getMovieVideos(id);
	}

	@Override
	public Observable<GetReviewsResponse> getMovieReviews(String id)
	{
		return apiServices.getMovieReviews(id);
	}
}
