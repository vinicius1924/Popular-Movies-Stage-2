package com.example.vinicius.popularmoviesstage2.model.api.base;


import com.example.vinicius.popularmoviesstage2.model.api.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetReviewsResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetVideosResponse;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Created by vinicius on 13/09/17.
 */

public interface ApiHelper<T>
{
	Observable<GetMoviesResponse> getPopularMovies();
	Observable<GetMoviesResponse> getTopRatedMovies();
	Observable<GetVideosResponse> getMovieVideos(String id);
	Observable<GetReviewsResponse> getMovieReviews(String id);
}
