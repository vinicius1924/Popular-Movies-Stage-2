package com.example.vinicius.popularmoviesstage2.view.movie_activity;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.DTO.MovieVideoDTO;
import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.presenter.movie_activity.MovieMvpPresenter;
import com.example.vinicius.popularmoviesstage2.model.api.GetReviewsResponse;
import com.example.vinicius.popularmoviesstage2.model.api.GetVideosResponse;
import com.example.vinicius.popularmoviesstage2.view.base.BaseActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MovieActivity extends BaseActivity implements View.OnClickListener, MovieMvpView
{
	private Toolbar toolbar;
	private TextView movieTitle;
	private ImageView moviePoster;
	private TextView movieReleaseDate;
	private TextView movieVoteAverage;
	private TextView movieSynopsis;
	private ProgressBar progressBarTrailers;
	private ProgressBar progressBarReviews;
	public static final String MOVIEACTIVITYTAG = "MovieActivity";
	private MovieDTO movieDTO;
	private LinearLayout linearLayoutTrailers;
	private LinearLayout linearLayoutReviews;
	private List<String> youtubeTrailers = new ArrayList<String>();
	private ShareActionProvider mShareActionProvider;
	private Snackbar snackbar;
	private CoordinatorLayout coordinatorLayout;
	private FloatingActionButton fab;

	@Inject
	MovieMvpPresenter<MovieMvpView> mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie);

		getActivityComponent().inject(this);
		mPresenter.registerView(this);
		mPresenter.onCreate();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		movieTitle = (TextView) findViewById(R.id.movieTitle);
		moviePoster = (ImageView) findViewById(R.id.moviePoster);
		movieReleaseDate = (TextView) findViewById(R.id.movieReleaseDate);
		movieVoteAverage = (TextView) findViewById(R.id.movieVoteAverage);
		movieSynopsis = (TextView) findViewById(R.id.movieSynopsis);
		linearLayoutTrailers = (LinearLayout) findViewById(R.id.linearLayoutTrailers);
		linearLayoutReviews = (LinearLayout) findViewById(R.id.linearLayoutReviews);
		progressBarTrailers = (ProgressBar) findViewById(R.id.progressBarTrailers);
		progressBarReviews = (ProgressBar) findViewById(R.id.progressBarReviews);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(this);

		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowTitleEnabled(false);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		movieDTO = bundle.getParcelable(MovieDTO.PARCELABLE_KEY);

		movieTitle.setText(movieDTO.getOriginalTitle());

		if(movieDTO.getPoster(getResources().getBoolean(R.bool.smallestWidth600),
				  getResources().getBoolean(R.bool.smallestWidth720)).startsWith("/data/data"))
		{
			Picasso.with(this).load(new File(movieDTO.getPoster(getResources().getBoolean(R.bool.smallestWidth600),
					  getResources().getBoolean(R.bool.smallestWidth720)))).into(moviePoster);
		}
		else
		{
			Picasso.with(this).load(movieDTO.getPoster(getResources().getBoolean(R.bool.smallestWidth600),
					  getResources().getBoolean(R.bool.smallestWidth720))).into(moviePoster);
		}

		movieReleaseDate.setText(movieDTO.getReleaseDate());
		movieVoteAverage.setText(String.format(getResources().getString(R.string.movie_vote_average),
				  String.valueOf(movieDTO.getVoteAverage())));
		movieSynopsis.setText(movieDTO.getOverview());

		Cursor movieCursor = mPresenter.findMovieById(movieDTO.getId());

		if(movieCursor.moveToFirst())
		{
			floatActionButtonSelected(true);
		}
		else
		{
			floatActionButtonSelected(false);
		}

		mPresenter.loadMovieTrailers(movieDTO.getId());
		mPresenter.loadMovieReviews(movieDTO.getId());
	}

	@Override
	protected void onStart()
	{
		super.onStart();
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
		getMenuInflater().inflate(R.menu.share_trailer, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;

			case R.id.menu_item_share:
				if(!youtubeTrailers.isEmpty())
				{
					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(Intent.EXTRA_TEXT, youtubeTrailers.get(0));
					String title = getResources().getString(R.string.share_trailer_title);

					Intent chooser = Intent.createChooser(shareIntent, title);

					if(shareIntent.resolveActivity(getPackageManager()) != null)
					{
						startActivity(chooser);
					}
				}
				else
				{
					showToast(getResources().getString(R.string.no_trailer_share));
				}

				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view)
	{
		String tag = (String) view.getTag();

		if(tag == null)
		{
			switch(view.getId())
			{
				case R.id.fab:
					if(view.isSelected())
					{
						floatActionButtonSelected(false);

						mPresenter.deleteMovieFromLocalDatabase(movieDTO.getId(), movieDTO.getPoster(getResources().getBoolean(R.bool.smallestWidth600),
								  getResources().getBoolean(R.bool.smallestWidth720)));
					}
					else
					{
						floatActionButtonSelected(true);

						mPresenter.addMovieToLocalDatabase(movieDTO.clone());
					}
					break;

				default:
					break;
			}
		}
		else
		{
			int tagNumber = Integer.valueOf(tag.substring(tag.length() - 1));

			Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeTrailers.get(tagNumber)));

			String title = getResources().getString(R.string.chooser_title);

			Intent chooser = Intent.createChooser(videoIntent, title);

			/*
			 * Testa se existe alguma activity que possa ser usada para
			 * enviar os dados da intent
			 */
			if(videoIntent.resolveActivity(getPackageManager()) != null)
			{
				startActivity(chooser);
			}
		}
	}

	@Override
	public void floatActionButtonSelected(boolean selected)
	{
		fab.setSelected(selected);
	}

	@Override
	public void progressBarTrailerVisibility(int visibility)
	{
		progressBarTrailers.setVisibility(visibility);
	}

	@Override
	public void progressBarReviewsVisibility(int visibility)
	{
		progressBarReviews.setVisibility(visibility);
	}

	@Override
	public void loadMovieTrailersResponse(GetVideosResponse response)
	{
		progressBarTrailerVisibility(View.GONE);

		for(int i = 0; i < response.getData().size(); i++)
		{
			youtubeTrailers.clear();

			for(MovieVideoDTO movieVideoDTO : response.getData())
			{
				youtubeTrailers.add(movieVideoDTO.getVideoUrl());
			}

			LinearLayout trailerLinearLayout = new LinearLayout(MovieActivity.this);
			trailerLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			trailerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
			trailerLinearLayout.setPadding((int) getResources().getDimension(R.dimen.activity_horizontal_margin), 0, 0, 0);


			ImageView imageView = new ImageView(MovieActivity.this);

			imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			imageView.setImageResource(R.drawable.ic_play_circle_outline_black_36dp);
			imageView.setTag(String.format(getResources().getString(R.string.play_video), String.valueOf(i)));
			imageView.setOnClickListener(MovieActivity.this);


			TextView textView = new TextView(MovieActivity.this);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;

			textView.setLayoutParams(params);

			textView.setPadding((int) getResources().getDimension(R.dimen.activity_horizontal_margin), 0, 0, 0);
			textView.setText(String.format(getResources().getString(R.string.trailer_text_view), String.valueOf(i + 1)));


			trailerLinearLayout.addView(imageView);
			trailerLinearLayout.addView(textView);

			linearLayoutTrailers.addView(trailerLinearLayout);
		}
	}

	@Override
	public void loadMovieReviewsResponse(GetReviewsResponse response)
	{
		progressBarReviewsVisibility(View.GONE);

		for(int i = 0; i < response.getData().size(); i++)
		{
			LinearLayout reviewLinearLayout = new LinearLayout(MovieActivity.this);
			reviewLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					  ViewGroup.LayoutParams.WRAP_CONTENT));
			reviewLinearLayout.setOrientation(LinearLayout.VERTICAL);
			reviewLinearLayout.setPadding((int) getResources().getDimension(R.dimen.activity_horizontal_margin),
					  (int) getResources().getDimension(R.dimen.activity_horizontal_margin), 0, 0);


			TextView textViewAuthor = new TextView(MovieActivity.this);

			textViewAuthor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					  ViewGroup.LayoutParams.WRAP_CONTENT));

			textViewAuthor.setTextColor(ContextCompat.getColor(MovieActivity.this, R.color.primary_text_color_light));
			textViewAuthor.setText(response.getData().get(i).getAuthor());

			TextView textViewReview = new TextView(MovieActivity.this);

			LinearLayout.LayoutParams textViewReviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					  ViewGroup.LayoutParams.WRAP_CONTENT);

			textViewReviewParams.setMargins(0, (int) getResources().getDimension(R.dimen.trailer_divider_height), 0,
					  (int) getResources().getDimension(R.dimen.toolbar_shadow_height));

			textViewReview.setLayoutParams(textViewReviewParams);
			textViewReview.setTextColor(ContextCompat.getColor(MovieActivity.this, R.color.secondary_text_color_light));
			textViewReview.setText(response.getData().get(i).getContent());

			reviewLinearLayout.addView(textViewAuthor);
			reviewLinearLayout.addView(textViewReview);

			linearLayoutReviews.addView(reviewLinearLayout);
		}
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
						  mPresenter.loadMovieReviews(movieDTO.getId());

						  if(progressBarTrailers.getVisibility() == View.VISIBLE)
						  {
							  mPresenter.loadMovieTrailers(movieDTO.getId());
						  }
					  }
				  });

		snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
		snackbar.show();
	}
}
