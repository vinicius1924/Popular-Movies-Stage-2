package com.example.vinicius.popularmoviesstage2.model.data_manager;

import com.example.vinicius.popularmoviesstage2.model.api.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetReviewsResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetVideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by vinicius on 05/02/18.
 */

public interface IApiServices
{
	@GET("movie/popular?api_key=114727fcab4f71df1ec5e7e2431c9481")
	Call<GetMoviesResponse> getPopularMovies();

	@GET("movie/top_rated?api_key=114727fcab4f71df1ec5e7e2431c9481")
	Call<GetMoviesResponse> getTopRatedMovies();

	@GET("movie/{id}/videos?api_key=114727fcab4f71df1ec5e7e2431c9481")
	Call<GetVideosResponse> getMovieVideos(@Path(value = "id") String id);

	@GET("movie/{id}/reviews?api_key=114727fcab4f71df1ec5e7e2431c9481")
	Call<GetReviewsResponse> getMovieReviews(@Path(value = "id") String id);
}
