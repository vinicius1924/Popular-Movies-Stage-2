package com.example.vinicius.popularmoviesstage2;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.server.ApiServices;
import com.example.vinicius.popularmoviesstage2.server.GetMoviesResponse;
import com.example.vinicius.popularmoviesstage2.utils.NetworkUtils;
import com.example.vinicius.popularmoviesstage2.utils.VolleyUtils;

import java.util.ArrayList;
import java.util.List;

public class PopularMoviesActivity extends AppCompatActivity implements MoviesPostersRecyclerAdapter.ListItemClickListener, SettingsFragment.PreferenceChangedEventListener
{
	private RecyclerView moviesPostersRecyclerView;
	private MoviesPostersRecyclerAdapter moviesPostersRecyclerAdapter;
	private CoordinatorLayout coordinatorLayout;
	private List<MovieDTO> moviesList = new ArrayList<MovieDTO>();
	private final String POPULARMOVIESACTIVITYTAG = getClass().getSimpleName();
	private boolean requestsCanceled = false;
	private Toolbar toolbar;
	private Snackbar snackbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popular_movies);

		moviesPostersRecyclerView = (RecyclerView) findViewById(R.id.moviesPostersRecyclerView);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowTitleEnabled(false);

		moviesPostersRecyclerView.setHasFixedSize(true);

		moviesPostersRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));

		moviesPostersRecyclerAdapter = new MoviesPostersRecyclerAdapter(this.getApplicationContext(), moviesList, this);

		moviesPostersRecyclerView.setAdapter(moviesPostersRecyclerAdapter);

		loadMoviesByPreference(getPreferenceValue(getResources().getString(R.string.movies_order_by_shared_preferences_key)));
	}

	private int numberOfColumns() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		// You can change this divider to adjust the size of the poster
		int widthDivider = 400;
		int width = displayMetrics.widthPixels;
		int nColumns = width / widthDivider;
		if (nColumns < 2) return 2;
		return nColumns;
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		if(requestsCanceled)
		{
			loadMoviesByPreference(getPreferenceValue(getResources().getString(R.string.movies_order_by_shared_preferences_key)));
		}
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

		try
		{
			int numberOfRequests = VolleyUtils.getNumberOfRequestsInQueue(getApplicationContext());

			if(numberOfRequests > 0)
			{
				RequestQueueSingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll(POPULARMOVIESACTIVITYTAG);

				requestsCanceled = true;
			}
			else
			{
				requestsCanceled = false;
			}
		}
		catch(NoSuchFieldException e)
		{
			Log.e(POPULARMOVIESACTIVITYTAG, e.toString());
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
				Log.e(POPULARMOVIESACTIVITYTAG, error.getLocalizedMessage());
			}
		};

		if(NetworkUtils.isOnline(getApplicationContext()))
		{
			ApiServices<GetMoviesResponse> apiServices = new ApiServices<>();
			apiServices.GetPopularMovies(successResponseRequestListener, errorResponseRequestListener,
					  GetMoviesResponse.class, getApplicationContext(), POPULARMOVIESACTIVITYTAG);
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
				Log.e(POPULARMOVIESACTIVITYTAG, error.getLocalizedMessage());
			}
		};

		if(NetworkUtils.isOnline(getApplicationContext()))
		{
			ApiServices<GetMoviesResponse> apiServices = new ApiServices<>();
			apiServices.GetTopRatedMovies(successResponseRequestListener, errorResponseRequestListener,
					  GetMoviesResponse.class, getApplicationContext(), POPULARMOVIESACTIVITYTAG);
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
			loadPopularMoviesFromApi();
		}
		else
		{
			loadTopRatedMoviesFromApi();
		}
	}

	public String getPreferenceValue(String preferenceKey)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String value = sharedPref.getString(preferenceKey, "");

		return value;
	}
}
