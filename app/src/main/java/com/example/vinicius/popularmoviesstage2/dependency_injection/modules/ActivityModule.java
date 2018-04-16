package com.example.vinicius.popularmoviesstage2.dependency_injection.modules;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.vinicius.popularmoviesstage2.dependency_injection.PerActivity;
import com.example.vinicius.popularmoviesstage2.presenter.movie_activity.MovieMvpPresenter;
import com.example.vinicius.popularmoviesstage2.presenter.movie_activity.MoviePresenter;
import com.example.vinicius.popularmoviesstage2.presenter.movies_list.MovieListMvpPresenter;
import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieMvpView;
import com.example.vinicius.popularmoviesstage2.view.movies_list_activity.MoviesListMvpView;
import com.example.vinicius.popularmoviesstage2.presenter.movies_list.MovieListPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vinicius on 12/09/17.
 */

@Module
public class ActivityModule
{
	private AppCompatActivity mActivity;

	public ActivityModule(AppCompatActivity activity)
	{
		this.mActivity = activity;
	}

	@Provides
	@Named("ActivityContext")
	Context provideContext()
	{
		return mActivity;
	}

	@Provides
	AppCompatActivity provideActivity()
	{
		return mActivity;
	}


	/*
	 * métodos anotados com @Provides podem ter dependencias próprias, ou seja, aqui
	 * quando for pedido uma instância da interface MovieListMvpPresenter será retornado
	 * uma instância de MovieListPresenter que imlementa MovieListMvpPresenter.
	 *
	 * OBS: para isso funcionar, a classe MovieListPresenter deve ter um construtor anotado com
	 * @Inject
	 */
	@Provides
	@PerActivity
	MovieListMvpPresenter<MoviesListMvpView> provideMovieListMvpPresenter(MovieListPresenter<MoviesListMvpView> presenter)
	{
		return presenter;
	}

	@Provides
	@PerActivity
	MovieMvpPresenter<MovieMvpView> provideMovieMvpPresenter(MoviePresenter<MovieMvpView> presenter)
	{
		return presenter;
	}
}
