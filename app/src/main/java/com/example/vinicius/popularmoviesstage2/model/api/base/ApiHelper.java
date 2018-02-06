package com.example.vinicius.popularmoviesstage2.model.api.base;


import com.example.vinicius.popularmoviesstage2.model.api.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetReviewsResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetVideosResponse;

import retrofit2.Call;

/**
 * Created by vinicius on 13/09/17.
 */

public interface ApiHelper<T>
{
	Call<GetMoviesResponse> getPopularMovies();
	Call<GetMoviesResponse> getTopRatedMovies();
	Call<GetVideosResponse> getMovieVideos(String id);
	Call<GetReviewsResponse> getMovieReviews(String id);
}
