package com.example.vinicius.popularmoviesstage2.view.movies_list_activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.database.MovieContract;
import com.example.vinicius.popularmoviesstage2.model.api.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.view.base.BaseActivity;
import com.example.vinicius.popularmoviesstage2.presenter.movies_list.MovieListMvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/*
 * Já que a minha activity extende BaseActivity ela não irá precisar implementar os métodos da interface MvpView
 * que a interface MoviesListMvpView está extendendo (sempre que a interface A extender a interface B, quem
 * implementar a interface A também deverá implementar os métodos da interface B).
 * Isso acontece pois BaseActivity já implementa os métodos da interface MvpView, portanto, já que a minha
 * activity extende esta classe, ela não precisa implementar os métodos declarados na interface MvpView.
 */
public class MoviesListActivity extends BaseActivity implements MoviesPostersRecyclerAdapter.ListItemClickListener, FavoritesRecyclerAdapter.FavoritesListItemClickListener,
		  android.app.LoaderManager.LoaderCallbacks<Cursor>, MoviesListMvpView
{
	private RecyclerView moviesPostersRecyclerView;
	private MoviesPostersRecyclerAdapter moviesPostersRecyclerAdapter;
	private FavoritesRecyclerAdapter favoritesRecyclerAdapter;
	private CoordinatorLayout coordinatorLayout;
	private List<MovieDTO> moviesList = new ArrayList<MovieDTO>();
	public static final String MOVIESLISTACTIVITYTAG = "MoviesListActivity";
	private Toolbar toolbar;
	private Snackbar snackbar;
	private TextView noFavoritesTextView;
	private ProgressBar progressBar;

	@Inject
	MovieListMvpPresenter<MoviesListMvpView> mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movies_list);

		getActivityComponent().inject(this);
		mPresenter.registerView(this);

		moviesPostersRecyclerView = (RecyclerView) findViewById(R.id.moviesPostersRecyclerView);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		noFavoritesTextView = (TextView) findViewById(R.id.noFavoritesTextView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowTitleEnabled(false);

		moviesPostersRecyclerView.setLayoutManager(new GridLayoutManager(this, mPresenter.recyclerViewNumberOfColumns()));

		moviesPostersRecyclerView.setHasFixedSize(true);

		/* Adapter usado pelo recycler view para mostrar os dados recebidos da internet */
		moviesPostersRecyclerAdapter = new MoviesPostersRecyclerAdapter(this.getApplicationContext(), moviesList, this);
		/* Adapter usado pelo recycler view para mostrar os dados do SQLite utilizando um cursor  */
		favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(this.getApplicationContext(), null, 0, this);

		Log.d("MoviesListActivity", "onCreate()");
		mPresenter.loadMoviesByPreference(mPresenter.getMenuOptionItemSelected());
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		mPresenter.unregisterView();
		mPresenter.onDestroy();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menuSortMostPopular:
				mPresenter.onMenuOptionItemSelected(item.getTitle().toString());
				mPresenter.loadMoviesByPreference(item.getTitle().toString());
				return true;

			case R.id.menuSortHighestRated:
				mPresenter.onMenuOptionItemSelected(item.getTitle().toString());
				mPresenter.loadMoviesByPreference(item.getTitle().toString());
				return true;

			case R.id.menuSortFavorites:
				mPresenter.onMenuOptionItemSelected(item.getTitle().toString());
				mPresenter.loadMoviesByPreference(item.getTitle().toString());
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(int clickedItemIndex)
	{
		MovieDTO movieDTO = moviesList.get(clickedItemIndex).clone();

		mPresenter.onRecyclerViewItemClick(movieDTO);
	}

	@Override
	public void onFavoriteListItemClick(MovieDTO movieDTO)
	{
		mPresenter.onFavoriteRecyclerViewItemClick(movieDTO);
	}

	@Override
	public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		return new android.content.CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI,
				  null,
				  null,
				  null,
				  null);
	}

	@Override
	public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data)
	{
		if(data == null || !data.moveToFirst())
		{
			noFavoritesTextViewVisibility(View.VISIBLE);
		}
		else
		{
			noFavoritesTextViewVisibility(View.INVISIBLE);
		}

		favoritesRecyclerAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(android.content.Loader<Cursor> loader)
	{
		favoritesRecyclerAdapter.swapCursor(null);
	}

	@Override
	public void noFavoritesTextViewVisibility(int visibility)
	{
		noFavoritesTextView.setVisibility(visibility);
	}

	@Override
	public void changeAdapterAccordingToPreference(String preference)
	{
		if(preference.equals(getResources().getString(R.string.first_pref_list_entry)))
		{
			RecyclerView.Adapter adapter = moviesPostersRecyclerView.getAdapter();

			if((adapter == null) || (adapter instanceof FavoritesRecyclerAdapter))
			{
				moviesPostersRecyclerView.setAdapter(moviesPostersRecyclerAdapter);
			}
		}

		if(preference.equals(getResources().getString(R.string.second_pref_list_entry)))
		{
			RecyclerView.Adapter adapter = moviesPostersRecyclerView.getAdapter();

			if((adapter == null) || (adapter instanceof FavoritesRecyclerAdapter))
			{
				moviesPostersRecyclerView.setAdapter(moviesPostersRecyclerAdapter);
			}
		}

		if(preference.equals(getResources().getString(R.string.third_pref_list_entry)))
		{
			if(snackbar != null)
			{
				snackbar.dismiss();
			}

			RecyclerView.Adapter adapter = moviesPostersRecyclerView.getAdapter();

			if((adapter == null) || (adapter instanceof MoviesPostersRecyclerAdapter))
			{
				moviesPostersRecyclerView.setAdapter(favoritesRecyclerAdapter);
				getLoaderManager().initLoader(0, null, this);
			}
		}
	}

	@Override
	public void getPopularMoviesResponse(GetMoviesResponse response)
	{
		moviesList.clear();
		moviesList.addAll(response.getData().subList(0, response.getData().size()));
		moviesPostersRecyclerAdapter.notifyDataSetChanged();
	}

	@Override
	public void getTopRatedMoviesResponse(GetMoviesResponse response)
	{
		moviesList.clear();
		moviesList.addAll(response.getData().subList(0, response.getData().size()));
		moviesPostersRecyclerAdapter.notifyDataSetChanged();
	}

	@Override
	public void showSnackBar(String message)
	{
		snackbar = Snackbar.make(coordinatorLayout, message,
				  Snackbar.LENGTH_LONG)
				  .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  snackbar.dismiss();
						  mPresenter.loadMoviesByPreference(mPresenter.getMenuOptionItemSelected());
					  }
				  });

		snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
		snackbar.show();
	}

	@Override
	public void showProgressBar()
	{
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressBar()
	{
		progressBar.setVisibility(View.INVISIBLE);
	}
}
