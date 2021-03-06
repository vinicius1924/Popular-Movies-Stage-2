package com.example.vinicius.popularmoviesstage2.presenter.movies_list;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieActivity;
import com.example.vinicius.popularmoviesstage2.view.movies_list_activity.MoviesListActivity;
import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.dependency_injection.PerActivity;
import com.example.vinicius.popularmoviesstage2.model.api.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.utils.NetworkUtils;
import com.example.vinicius.popularmoviesstage2.presenter.base.BasePresenter;
import com.example.vinicius.popularmoviesstage2.view.movies_list_activity.MoviesListMvpView;

import javax.inject.Inject;
import javax.inject.Named;

import static com.example.vinicius.popularmoviesstage2.utils.View.captureValues;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by vinicius on 12/09/17.
 */

@PerActivity
public class MovieListPresenter<V extends MoviesListMvpView> extends BasePresenter<V>
		  implements MovieListMvpPresenter<V>
{
	@Named("ActivityContext")Context context;
	AppCompatActivity activity;
	private Observable<GetMoviesResponse> callPopularMoviesResponse = null;
	private Observable<GetMoviesResponse> callTopRatedMoviesResponse = null;
	/*
	 * Coleta todos os subscriptions para fazer unsubscribe depois
	 */
	private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


	@Inject
	public MovieListPresenter(@Named("ActivityContext")Context context, DataManager dataManager, AppCompatActivity activity)
	{
		super(dataManager);
		this.context = context;
		this.activity = activity;
	}

	@Override
	public void loadMoviesByPreference(String preference)
	{
		getMvpView().showProgressBar();

		if(preference.equals(context.getResources().getString(R.string.first_pref_list_entry)))
		{
			getMvpView().noFavoritesTextViewVisibility(View.INVISIBLE);
			getMvpView().changeAdapterAccordingToPreference(preference);

			if(NetworkUtils.isOnline(context))
			{
				callPopularMoviesResponse = getDataManager().getPopularMovies();

				/*
				 * subscribeOn(Schedulers.io()) - diz que o Observable(que representa a fonte de dados) fará seu
				 * trabalho fora da thread principal
				 *
				 * observeOn(AndroidSchedulers.mainThread()) - diz que o Observer(aquele que se inscreve em um Observable
				 * para receber os dados) irá receber os dados do Observable na thread principal
				 */
				mCompositeDisposable.add(callPopularMoviesResponse.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Consumer<GetMoviesResponse>()
					{
						@Override
						public void accept(GetMoviesResponse getMoviesResponse) throws Exception
						{
							getMvpView().hideProgressBar();
							getMvpView().getPopularMoviesResponse(getMoviesResponse);
						}
					}, new Consumer<Throwable>()
					{
						@Override
						public void accept(Throwable throwable) throws Exception
						{
							getMvpView().hideProgressBar();
							Log.e(((MoviesListActivity)getMvpView()).MOVIESLISTACTIVITYTAG, throwable.getLocalizedMessage());
						}
					}));
			}
			else
			{
				getMvpView().hideProgressBar();
				getMvpView().showSnackBar(context.getResources().getString(R.string.no_internet_connection));
			}
		}

		if(preference.equals(context.getResources().getString(R.string.second_pref_list_entry)))
		{
			getMvpView().noFavoritesTextViewVisibility(View.INVISIBLE);
			getMvpView().changeAdapterAccordingToPreference(preference);

			if(NetworkUtils.isOnline(context))
			{
				callTopRatedMoviesResponse = getDataManager().getTopRatedMovies();

				mCompositeDisposable.add(callTopRatedMoviesResponse.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Consumer<GetMoviesResponse>()
					{
						@Override
						public void accept(GetMoviesResponse getMoviesResponse) throws Exception
						{
							getMvpView().hideProgressBar();
							getMvpView().getTopRatedMoviesResponse(getMoviesResponse);
						}
					}, new Consumer<Throwable>()
					{
						@Override
						public void accept(Throwable throwable) throws Exception
						{
							getMvpView().hideProgressBar();
							Log.e(((MoviesListActivity)getMvpView()).MOVIESLISTACTIVITYTAG, throwable.getLocalizedMessage());
						}
					}));
			}
			else
			{
				getMvpView().hideProgressBar();
				getMvpView().showSnackBar(context.getResources().getString(R.string.no_internet_connection));
			}
		}

		if(preference.equals(context.getResources().getString(R.string.third_pref_list_entry)))
		{
			getMvpView().hideProgressBar();
			getMvpView().changeAdapterAccordingToPreference(preference);
		}
	}

	@Override
	public int recyclerViewNumberOfColumns()
	{
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		// Esse valor deve ser ajustado de acordo com a largura do poster que será mostrado
		int widthDivider = 185;

		if(context.getResources().getBoolean(R.bool.smallestWidth600))
		{
			widthDivider = 342;
		}

		if(context.getResources().getBoolean(R.bool.smallestWidth720))
		{
			widthDivider = 500;
		}

		int width = displayMetrics.widthPixels;
		int nColumns = width / widthDivider;
		if (nColumns < 2) return 2;
		return nColumns;
	}

	@Override
	public void onMenuOptionItemSelected(String orderBy)
	{
		getDataManager().setMoviesSortBy(orderBy);
	}

	@Override
	public String getMenuOptionItemSelected()
	{
		return getDataManager().getMoviesSortBy();
	}

	@Override
	public void onRecyclerViewItemClick(MovieDTO movieDTO, View shredElementTransition)
	{
		Intent i = new Intent(context, MovieActivity.class);
		i.putExtra(MovieDTO.PARCELABLE_KEY, movieDTO);
		i.putExtra(MovieActivity.TRANSITION_NAME, ViewCompat.getTransitionName(shredElementTransition));

		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		{
			/* Manda para a activity que será chamada a posição e o tamanho iniciais da view que será animada */
			i.putExtra(MovieActivity.SHARED_VIEW_INFO_EXTRA, captureValues(shredElementTransition));
			context.startActivity(i);
			/*
			 * Especifica uma animação a ser executada na transição entre duas activities para dispositivos
			 * com API < 21
			 */
			//((MoviesListActivity) getMvpView()).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			((MoviesListActivity) getMvpView()).overridePendingTransition(0, 0);
		}
		else
		{
			context.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((MoviesListActivity) getMvpView(), shredElementTransition, ViewCompat.getTransitionName(shredElementTransition)).toBundle());
		}
	}

	@Override
	public void onFavoriteRecyclerViewItemClick(MovieDTO movieDTO, View shredElementTransition)
	{
		Intent i = new Intent(context, MovieActivity.class);
		i.putExtra(MovieDTO.PARCELABLE_KEY, movieDTO);
		i.putExtra(MovieActivity.TRANSITION_NAME, ViewCompat.getTransitionName(shredElementTransition));

		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		{
			i.putExtra(MovieActivity.SHARED_VIEW_INFO_EXTRA, captureValues(shredElementTransition));
		}

		context.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((MoviesListActivity)getMvpView(),
				  shredElementTransition, ViewCompat.getTransitionName(shredElementTransition)).toBundle());

		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		{
			((MoviesListActivity) getMvpView()).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		}
	}

	@Override
	public void onDestroy()
	{
		mCompositeDisposable.clear();
	}
}
