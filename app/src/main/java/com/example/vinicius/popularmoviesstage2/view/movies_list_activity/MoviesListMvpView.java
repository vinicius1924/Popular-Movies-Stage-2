package com.example.vinicius.popularmoviesstage2.view.movies_list_activity;

import com.example.vinicius.popularmoviesstage2.model.api.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.view.base.MvpView;

/**
 * Created by vinicius on 12/09/17.
 */

/*
 * Esta interface deve ser implementada por MovieListActivity e vai conter os métodos
 * que serão chamados pelo presenter para se comunicar com a Activity MoviesListActivity
 */
public interface MoviesListMvpView extends MvpView
{
	void noFavoritesTextViewVisibility(int visibility);
	void changeAdapterAccordingToPreference(String preference);
	void getPopularMoviesResponse(GetMoviesResponse response);
	void getTopRatedMoviesResponse(GetMoviesResponse response);
	void showSnackBar(String message);
	void showProgressBar();
	void hideProgressBar();
}
