package com.example.vinicius.popularmoviesstage2.view.movie_activity;

import com.example.vinicius.popularmoviesstage2.model.api.GetReviewsResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetVideosResponse;
import com.example.vinicius.popularmoviesstage2.view.base.MvpView;

/**
 * Created by vinicius on 18/09/17.
 */

public interface MovieMvpView extends MvpView
{
	void floatActionButtonSelected(boolean selected);
	void progressBarTrailerVisibility(int visibility);
	void progressBarReviewsVisibility(int visibility);
	void loadMovieTrailersResponse(GetVideosResponse response);
	void loadMovieReviewsResponse(GetReviewsResponse response);
	void showSnackBar(String message);
}
