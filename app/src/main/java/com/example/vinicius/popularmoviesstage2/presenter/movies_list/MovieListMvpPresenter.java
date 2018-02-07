package com.example.vinicius.popularmoviesstage2.presenter.movies_list;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.presenter.base.MvpPresenter;
import com.example.vinicius.popularmoviesstage2.view.movies_list_activity.MoviesListMvpView;

/**
 * Created by vinicius on 12/09/17.
 */

/*
 * Esta interface deve ser implementada por MovieListPresenter e vai conter os métodos
 * que serão chamados pela view para se comunicar com o Presenter MovieListPresenter
 */
public interface MovieListMvpPresenter<V extends MoviesListMvpView> extends MvpPresenter<V>
{
	void loadMoviesByPreference(String preference);
	int recyclerViewNumberOfColumns();
	void onMenuOptionItemSelected(String orderBy);
	String getMenuOptionItemSelected();
	void onRecyclerViewItemClick(MovieDTO movieDTO);
	void onFavoriteRecyclerViewItemClick(MovieDTO movieDTO);
	void onDestroy();
}
