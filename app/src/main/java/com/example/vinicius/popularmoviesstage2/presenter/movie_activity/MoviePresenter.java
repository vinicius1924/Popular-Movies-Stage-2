package com.example.vinicius.popularmoviesstage2.presenter.movie_activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.dependency_injection.PerActivity;
import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.presenter.base.BasePresenter;
import com.example.vinicius.popularmoviesstage2.model.api.GetReviewsResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetVideosResponse;
import com.example.vinicius.popularmoviesstage2.utils.NetworkUtils;
import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieActivity;
import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieMvpView;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by vinicius on 18/09/17.
 */

@PerActivity
public class MoviePresenter<V extends MovieMvpView> extends BasePresenter<V> implements MovieMvpPresenter<V>
{
	@Named("ActivityContext")Context context;
	@Named("ApplicationContext") Context applicationContext;
	private MovieDTO movieDTO;
	AppCompatActivity activity;
	private Observable<GetVideosResponse> callGetMovieVideos;
	private Observable<GetReviewsResponse> callGetMovieReviews;
	/*
	 * Coleta todos os subscriptions para fazer unsubscribe depois
	 */
	private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

	@Inject
	public MoviePresenter(@Named("ActivityContext")Context context,
								 @Named("ApplicationContext") Context applicationContext, DataManager dataManager,
								 AppCompatActivity activity)
	{
		super(dataManager);
		this.context = context;
		this.applicationContext = applicationContext;
		this.activity = activity;
	}

	@Override
	public void onCreate()
	{
		Bundle bundle = activity.getIntent().getExtras();
		movieDTO = bundle.getParcelable(MovieDTO.PARCELABLE_KEY);
	}

	@Override
	public void loadMovieTrailers(long id)
	{
		getMvpView().progressBarTrailerVisibility(View.VISIBLE);

		if(NetworkUtils.isOnline(context))
		{
			callGetMovieVideos = getDataManager().getMovieVideos(String.valueOf(id));

			/*
			 * subscribeOn(Schedulers.io()) - diz que o Observable(que representa a fonte de dados) fará seu
			 * trabalho fora da thread principal
			 *
			 * observeOn(AndroidSchedulers.mainThread()) - diz que o Observer(aquele que se inscreve em um Observable
			 * para receber os dados) irá receber os dados do Observable na thread principal
			 */
			mCompositeDisposable.add(callGetMovieVideos.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<GetVideosResponse>()
				{
				  @Override
				  public void accept(GetVideosResponse getVideosResponse) throws Exception
				  {
					  getMvpView().loadMovieTrailersResponse(getVideosResponse);
				  }
				}, new Consumer<Throwable>()
				{
				  @Override
				  public void accept(Throwable throwable) throws Exception
				  {
					  Log.e(MovieActivity.MOVIEACTIVITYTAG, throwable.getLocalizedMessage());
				  }
				}));
		}
		else
		{
			getMvpView().showSnackBar(context.getResources().getString(R.string.no_internet_connection));
		}
	}

	@Override
	public void loadMovieReviews(long id)
	{
		getMvpView().progressBarReviewsVisibility(View.VISIBLE);

		if(NetworkUtils.isOnline(context))
		{
			callGetMovieReviews = getDataManager().getMovieReviews(String.valueOf(id));

			mCompositeDisposable.add(callGetMovieReviews.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<GetReviewsResponse>()
				{
					@Override
					public void accept(GetReviewsResponse getReviewsResponse) throws Exception
					{
						getMvpView().loadMovieReviewsResponse(getReviewsResponse);
					}
				}, new Consumer<Throwable>()
				{
					@Override
					public void accept(Throwable throwable) throws Exception
					{
						Log.e(MovieActivity.MOVIEACTIVITYTAG, throwable.getLocalizedMessage());
					}
				}));
		}
		else
		{
			getMvpView().showSnackBar(context.getResources().getString(R.string.no_internet_connection));
		}
	}

	@Override
	public void deleteMovieFromLocalDatabase(long id, String posterPath)
	{
		getDataManager().deleteMovie(id, posterPath, movieDTO);
	}

	@Override
	public void addMovieToLocalDatabase(MovieDTO movieDTO)
	{
		getDataManager().insertMovie(movieDTO);
	}

	@Override
	public Cursor findMovieById(long id)
	{
		return getDataManager().queryMovieById(id);
	}

	@Override
	public void onDestroy()
	{
		mCompositeDisposable.clear();
	}
}
