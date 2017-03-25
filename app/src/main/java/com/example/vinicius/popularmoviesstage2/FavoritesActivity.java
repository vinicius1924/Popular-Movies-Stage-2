package com.example.vinicius.popularmoviesstage2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.vinicius.popularmoviesstage2.database.MovieContract;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity
{
	private RecyclerView favoritesRecyclerView;
	private FavoritesRecyclerAdapter favoritesRecyclerAdapter;
	private TextView noFavoritesTextView;
	private List<String> favoritesList = new ArrayList<String>();
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);

		favoritesRecyclerView = (RecyclerView) findViewById(R.id.favoritesRecyclerView);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		noFavoritesTextView = (TextView) findViewById(R.id.noFavoritesTextView);

		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowTitleEnabled(false);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
		favoritesRecyclerView.addItemDecoration(itemDecoration);

		favoritesRecyclerView.setHasFixedSize(true);

		favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(this.getApplicationContext(), favoritesList);

		favoritesRecyclerView.setAdapter(favoritesRecyclerAdapter);

		Cursor movieCursor = loadFavorites();

		while(movieCursor.moveToNext())
		{
			long id = movieCursor.getLong(movieCursor.getColumnIndex(MovieContract.MovieEntry._ID));

			favoritesList.add(String.valueOf(id));
		}

		if(favoritesList.isEmpty())
		{
			noFavoritesTextView.setVisibility(View.VISIBLE);
		}
		else
		{
			noFavoritesTextView.setVisibility(View.INVISIBLE);
			favoritesRecyclerAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public Cursor loadFavorites()
	{
		Cursor movieCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
				  new String[]{MovieContract.MovieEntry._ID}, null, null, null);

		return movieCursor;
	}
}
