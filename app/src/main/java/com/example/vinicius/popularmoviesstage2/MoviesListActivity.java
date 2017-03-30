package com.example.vinicius.popularmoviesstage2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.database.MovieContract;
import com.example.vinicius.popularmoviesstage2.server.ApiServices;
import com.example.vinicius.popularmoviesstage2.server.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.utils.NetworkUtils;
import com.example.vinicius.popularmoviesstage2.utils.VolleyUtils;

import java.util.ArrayList;
import java.util.List;

public class MoviesListActivity extends AppCompatActivity implements MoviesPostersRecyclerAdapter.ListItemClickListener,
		  FavoritesRecyclerAdapter.FavoritesListItemClickListener,SettingsFragment.PreferenceChangedEventListener,
		  android.app.LoaderManager.LoaderCallbacks<Cursor>
{
	private RecyclerView moviesPostersRecyclerView;
	private MoviesPostersRecyclerAdapter moviesPostersRecyclerAdapter;
	private FavoritesRecyclerAdapter favoritesRecyclerAdapter;
	private CoordinatorLayout coordinatorLayout;
	private List<MovieDTO> moviesList = new ArrayList<MovieDTO>();
	private final String MOVIESLISTACTIVITYTAG = getClass().getSimpleName();
	private boolean requestsCanceled = false;
	private Toolbar toolbar;
	private Snackbar snackbar;
	private TextView noFavoritesTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.e(MOVIESLISTACTIVITYTAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movies_list);

		moviesPostersRecyclerView = (RecyclerView) findViewById(R.id.moviesPostersRecyclerView);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		noFavoritesTextView = (TextView) findViewById(R.id.noFavoritesTextView);

		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowTitleEnabled(false);

		moviesPostersRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));

		moviesPostersRecyclerView.setHasFixedSize(true);

		/* Adapter usado pelo recycler view para mostrar os dados recebidos da internet */
		moviesPostersRecyclerAdapter = new MoviesPostersRecyclerAdapter(this.getApplicationContext(), moviesList, this);
		/* Adapter usado pelo recycler view para mostrar os dados do SQLite utilizando um cursor  */
		favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(this.getApplicationContext(), null, 0, this);

		loadMoviesByPreference(getPreferenceValue(getResources().getString(R.string.movies_order_by_shared_preferences_key)));
	}

	private int numberOfColumns() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		// Esse valor deve ser ajustado de acordo com a largura do poster que será mostrado

		int widthDivider = 185;

		if(getResources().getBoolean(R.bool.smallestWidth600))
		{
			widthDivider = 342;
		}

		if(getResources().getBoolean(R.bool.smallestWidth720))
		{
			widthDivider = 500;
		}

		int width = displayMetrics.widthPixels;
		int nColumns = width / widthDivider;
		if (nColumns < 2) return 2;
		return nColumns;
	}

	@Override
	protected void onStart()
	{
		Log.e(MOVIESLISTACTIVITYTAG, "onStart");

		super.onStart();

		if(requestsCanceled)
		{
			loadMoviesByPreference(getPreferenceValue(getResources().getString(R.string.movies_order_by_shared_preferences_key)));
		}
	}

	@Override
	protected void onResume()
	{
		Log.e(MOVIESLISTACTIVITYTAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		try
		{
			int numberOfRequests = VolleyUtils.getNumberOfRequestsInQueue(getApplicationContext());

			if(numberOfRequests > 0)
			{
				RequestQueueSingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll(MOVIESLISTACTIVITYTAG);

				requestsCanceled = true;
			}
			else
			{
				requestsCanceled = false;
			}
		}
		catch(NoSuchFieldException e)
		{
			Log.e(MOVIESLISTACTIVITYTAG, e.toString());
		}
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
			case R.id.action_settings:
				getFragmentManager().beginTransaction()
						  .replace(android.R.id.content, new SettingsFragment())
						  .addToBackStack("Settings")
						  .commit();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(int clickedItemIndex)
	{
		MovieDTO movieDTO = moviesList.get(clickedItemIndex).clone();

		Intent i = new Intent(this, MovieActivity.class);
		i.putExtra(MovieDTO.PARCELABLE_KEY, movieDTO);
		startActivity(i);
	}

	@Override
	public void onFavoriteListItemClick(MovieDTO movieDTO)
	{
		Intent i = new Intent(this, MovieActivity.class);
		i.putExtra(MovieDTO.PARCELABLE_KEY, movieDTO);
		startActivity(i);
	}

	public void loadPopularMoviesFromApi()
	{
		final Response.Listener<GetMoviesResponse> successResponseRequestListener = new Response.Listener<GetMoviesResponse>()
		{
			@Override
			public void onResponse(GetMoviesResponse response)
			{
				moviesList.clear();
				moviesList.addAll(response.getData().subList(0, response.getData().size()));
				moviesPostersRecyclerAdapter.notifyDataSetChanged();
			}
		};

		final Response.ErrorListener errorResponseRequestListener = new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Log.e(MOVIESLISTACTIVITYTAG, error.getLocalizedMessage());
			}
		};

		if(NetworkUtils.isOnline(getApplicationContext()))
		{
			ApiServices<GetMoviesResponse> apiServices = new ApiServices<>();
			apiServices.GetPopularMovies(successResponseRequestListener, errorResponseRequestListener,
					  GetMoviesResponse.class, getApplicationContext(), MOVIESLISTACTIVITYTAG);
		}
		else
		{
			snackbar = Snackbar.make(coordinatorLayout, getResources().getString(R.string.no_internet_connection),
					  Snackbar.LENGTH_LONG)
					  .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
						  @Override
						  public void onClick(View view) {
							  snackbar.dismiss();
							  loadPopularMoviesFromApi();
						  }
					  });

			snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
			snackbar.show();
		}
	}

	public void loadTopRatedMoviesFromApi()
	{
		final Response.Listener<GetMoviesResponse> successResponseRequestListener = new Response.Listener<GetMoviesResponse>()
		{
			@Override
			public void onResponse(GetMoviesResponse response)
			{
				moviesList.clear();
				moviesList.addAll(response.getData().subList(0, response.getData().size()));
				moviesPostersRecyclerAdapter.notifyDataSetChanged();
			}
		};

		final Response.ErrorListener errorResponseRequestListener = new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Log.e(MOVIESLISTACTIVITYTAG, error.getLocalizedMessage());
			}
		};

		if(NetworkUtils.isOnline(getApplicationContext()))
		{
			ApiServices<GetMoviesResponse> apiServices = new ApiServices<>();
			apiServices.GetTopRatedMovies(successResponseRequestListener, errorResponseRequestListener,
					  GetMoviesResponse.class, getApplicationContext(), MOVIESLISTACTIVITYTAG);
		}
		else
		{
			snackbar = Snackbar.make(coordinatorLayout, getResources().getString(R.string.no_internet_connection),
					  Snackbar.LENGTH_LONG)
					  .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
						  @Override
						  public void onClick(View view) {
							  snackbar.dismiss();
							  loadTopRatedMoviesFromApi();
						  }
					  });

			snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
			snackbar.show();
		}
	}

	@Override
	public void onPreferenceChangedEvent(String preference)
	{
		loadMoviesByPreference(preference);
	}

	public void loadMoviesByPreference(String preference)
	{
		if(preference.equals(getResources().getString(R.string.first_pref_list_entry)))
		{
			noFavoritesTextView.setVisibility(View.INVISIBLE);
			RecyclerView.Adapter adapter = moviesPostersRecyclerView.getAdapter();

			if((adapter == null) || (adapter instanceof FavoritesRecyclerAdapter))
			{
				moviesPostersRecyclerView.setAdapter(moviesPostersRecyclerAdapter);
			}

			loadPopularMoviesFromApi();
		}

		if(preference.equals(getResources().getString(R.string.second_pref_list_entry)))
		{
			noFavoritesTextView.setVisibility(View.INVISIBLE);
			RecyclerView.Adapter adapter = moviesPostersRecyclerView.getAdapter();

			if((adapter == null) || (adapter instanceof FavoritesRecyclerAdapter))
			{
				moviesPostersRecyclerView.setAdapter(moviesPostersRecyclerAdapter);
			}

			loadTopRatedMoviesFromApi();
		}

		if(preference.equals(getResources().getString(R.string.third_pref_list_entry)))
		{
			RecyclerView.Adapter adapter = moviesPostersRecyclerView.getAdapter();

			if((adapter == null) || (adapter instanceof MoviesPostersRecyclerAdapter))
			{
				moviesPostersRecyclerView.setAdapter(favoritesRecyclerAdapter);
				getLoaderManager().initLoader(0, null, this);
			}
		}
	}

	public String getPreferenceValue(String preferenceKey)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String value = sharedPref.getString(preferenceKey, getResources().getString(R.string.first_pref_list_entry));

		return value;
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
			noFavoritesTextView.setVisibility(View.VISIBLE);
		}
		else
		{
			noFavoritesTextView.setVisibility(View.INVISIBLE);
		}

		favoritesRecyclerAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(android.content.Loader<Cursor> loader)
	{
		favoritesRecyclerAdapter.swapCursor(null);
	}
}
