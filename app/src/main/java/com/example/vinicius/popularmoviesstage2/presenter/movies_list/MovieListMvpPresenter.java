package com.example.vinicius.popularmoviesstage2.presenter.movies_list;

import android.view.View;
import android.widget.ImageView;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.presenter.base.MvpPresenter;
import com.example.vinicius.popularmoviesstage2.view.movies_list_activity.MoviesListMvpView;

/**
 * Created by vinicius on 12/09/17.
 */

/*
 * Esta interface deve ser implementada por MovieListPresenter e vai conter os métodos
 * que serão chamados pela view para se comunicar com o Presenter MovieListPresenter
 *
 * Como MovieListMvpPresenter extends a classe MvpPresenter e MvpPresenter tem que ser de
 * algum tipo que extenda MvpView, a interface MovieListMvpPresenter tem que ser de algum
 * tipo que extends MvpView. Nesse caso tem que ser do tipo MoviesListMvpView que é uma
 * interface que extends MvpView
 */
public interface MovieListMvpPresenter<V extends MoviesListMvpView> extends MvpPresenter<V>
{
	void loadMoviesByPreference(String preference);
	int recyclerViewNumberOfColumns();
	void onMenuOptionItemSelected(String orderBy);
	String getMenuOptionItemSelected();
	void onRecyclerViewItemClick(MovieDTO movieDTO, View shredElementTransition);
	void onFavoriteRecyclerViewItemClick(MovieDTO movieDTO, View shredElementTransition);
	void onDestroy();
}
