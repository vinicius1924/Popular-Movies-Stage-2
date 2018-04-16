package com.example.vinicius.popularmoviesstage2.presenter.movie_activity;

import android.database.Cursor;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.presenter.base.MvpPresenter;
import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieMvpView;

/**
 * Created by vinicius on 18/09/17.
 */

public interface MovieMvpPresenter<V extends MovieMvpView> extends MvpPresenter<V>
{
	void onCreate();
	void loadMovieTrailers(long id);
	void loadMovieReviews(long id);
//	boolean isTrailersRequestsCanceled();
//	boolean isReviewsRequestsCanceled();
	void deleteMovieFromLocalDatabase(long id, String posterPath);
	void addMovieToLocalDatabase(MovieDTO movieDTO);
	Cursor findMovieById(long id);
	void onDestroy();
}
