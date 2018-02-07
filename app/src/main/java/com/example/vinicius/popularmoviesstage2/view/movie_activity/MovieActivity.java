package com.example.vinicius.popularmoviesstage2.view.movie_activity;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.vinicius.popularmoviesstage2.utils.View.captureValues;
import static com.example.vinicius.popularmoviesstage2.utils.View.scaleDelta;
import static com.example.vinicius.popularmoviesstage2.utils.View.translationDelta;

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
	private View shadowView;
	public static final String MOVIEACTIVITYTAG = "MovieActivity";
	private MovieDTO movieDTO;
	private LinearLayout linearLayoutTrailers;
	private LinearLayout linearLayoutReviews;
	private List<String> youtubeTrailers = new ArrayList<String>();
	private ShareActionProvider mShareActionProvider;
	private Snackbar snackbar;
	private CoordinatorLayout coordinatorLayout;
	private FloatingActionButton fab;

	// Bundle that will contain the transition start values
	private Bundle mStartValues;
	// Bundle that will contain the transition end values
	private Bundle mEndValues = new Bundle();

	public static final String TRANSITION_NAME = "TRANSITION_NAME";
	public static final String SHARED_VIEW_INFO_EXTRA = "SHARED_VIEW_INFO_EXTRA";
	public static final String SHARED_VIEW_SCREEN_LOCATION_LEFT = "rsspace:location:left";
	public static final String SHARED_VIEW_SCREEN_LOCATION_TOP = "rsspace:location:top";
	public static final String SHARED_VIEW_WIDTH = "rsspace:width";
	public static final String SHARED_VIEW_HEIGHT = "rsspace:height";

	//private static final int DEFAULT_DURATION = 225;
	private static final AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();

	private float scaleX;
	private float scaleY;
	private int deltaX;
	private int deltaY;

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
		shadowView = findViewById(R.id.shadowView);

		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		{
			hideAllViews();
		}

		fab.setOnClickListener(this);

		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowTitleEnabled(false);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		movieDTO = bundle.getParcelable(MovieDTO.PARCELABLE_KEY);
		/* Recebe a posição e o tamanho iniciais da view que será animada */
		mStartValues = bundle.getBundle(SHARED_VIEW_INFO_EXTRA);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			moviePoster.setTransitionName(bundle.getString(TRANSITION_NAME));
		}

		movieTitle.setText(movieDTO.getOriginalTitle());

		if(movieDTO.getPoster(getResources().getBoolean(R.bool.smallestWidth600), getResources().getBoolean(R.bool
				  .smallestWidth720)).startsWith("/data/data"))
		{
			Picasso.with(this).load(new File(movieDTO.getPoster(getResources().getBoolean(R.bool.smallestWidth600),
					  getResources().getBoolean(R.bool.smallestWidth720)))).into(moviePoster, new Callback()
			{
				@Override
				public void onSuccess()
				{
					if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
					{
						/* Quando temos a imagem carregada podemos preparar a animação */
						onUiReady();
					}
				}

				@Override
				public void onError()
				{

				}
			});
		}
		else
		{
			Picasso.with(this).load(movieDTO.getPoster(getResources().getBoolean(R.bool.smallestWidth600), getResources()
					  .getBoolean(R.bool.smallestWidth720))).into(moviePoster, new Callback()
			{
				@Override
				public void onSuccess()
				{
					if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
					{
						/* Quando temos a imagem carregada podemos preparar a animação */
						onUiReady();
					}
				}

				@Override
				public void onError()
				{

				}
			});
		}

		movieReleaseDate.setText(movieDTO.getReleaseDate());
		movieVoteAverage.setText(String.format(getResources().getString(R.string.movie_vote_average), String.valueOf
				  (movieDTO.getVoteAverage())));
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


	private void hideAllViews()
	{
		moviePoster.setVisibility(View.INVISIBLE);
		toolbar.setAlpha(0f);
		shadowView.setAlpha(0f);
		movieTitle.setAlpha(0f);
		movieReleaseDate.setAlpha(0f);
		movieVoteAverage.setAlpha(0f);
		movieSynopsis.setAlpha(0f);
		linearLayoutTrailers.setAlpha(0f);
		linearLayoutReviews.setAlpha(0f);
		fab.setAlpha(0f);



//		toolbar.setVisibility(View.INVISIBLE);
//		shadowView.setVisibility(View.INVISIBLE);
//		movieTitle.setVisibility(View.INVISIBLE);
//		moviePoster.setVisibility(View.INVISIBLE);
//		movieReleaseDate.setVisibility(View.INVISIBLE);
//		movieVoteAverage.setVisibility(View.INVISIBLE);
//		movieSynopsis.setVisibility(View.INVISIBLE);
//		linearLayoutTrailers.setVisibility(View.INVISIBLE);
//		linearLayoutReviews.setVisibility(View.INVISIBLE);
//		fab.setVisibility(View.INVISIBLE);
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
	public void onBackPressed()
	{
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		{
			runExitAnimation();
		}
		else
		{
			super.onBackPressed();
		}
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
			trailerLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
					  .LayoutParams.WRAP_CONTENT));
			trailerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
			trailerLinearLayout.setPadding((int) getResources().getDimension(R.dimen.activity_horizontal_margin), 0, 0,
					  0);


			ImageView imageView = new ImageView(MovieActivity.this);

			imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
					  .LayoutParams.WRAP_CONTENT));
			imageView.setImageResource(R.drawable.ic_play_circle_outline_black_36dp);
			imageView.setTag(String.format(getResources().getString(R.string.play_video), String.valueOf(i)));
			imageView.setOnClickListener(MovieActivity.this);


			TextView textView = new TextView(MovieActivity.this);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					  ViewGroup.LayoutParams.WRAP_CONTENT);
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
			reviewLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
					  .LayoutParams.WRAP_CONTENT));
			reviewLinearLayout.setOrientation(LinearLayout.VERTICAL);
			reviewLinearLayout.setPadding((int) getResources().getDimension(R.dimen.activity_horizontal_margin), (int)
					  getResources().getDimension(R.dimen.activity_horizontal_margin), 0, 0);


			TextView textViewAuthor = new TextView(MovieActivity.this);

			textViewAuthor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
					  .LayoutParams.WRAP_CONTENT));

			textViewAuthor.setTextColor(ContextCompat.getColor(MovieActivity.this, R.color.primary_text_color_light));
			textViewAuthor.setText(response.getData().get(i).getAuthor());

			TextView textViewReview = new TextView(MovieActivity.this);

			LinearLayout.LayoutParams textViewReviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
					  .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
		snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).setAction(getResources().getString(R
				  .string.retry), new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
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

	/*
	 * Já temos os valores de inicio da view que será compartilhada, passados via intent, agora é hora
	 * de capturar os valores do final. Para isso precisamos de um lugar onde o layout foi finalizado
	 * mas ainda não foi desenhado. Esse lugar é o método onPreDraw().
	 * O método onPreDraw() é chamado logo após a view criar o seu layout e um pouco antes da view
	 * ser desenhada
	 */
	private void onUiReady()
	{
		moviePoster.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
		{
			@Override
			public boolean onPreDraw()
			{
				moviePoster.getViewTreeObserver().removeOnPreDrawListener(this);

				// prep the scene
				prepareScene();
				// run the animation
				runEnterAnimation();
				return true;
			}
		});
	}

	private void prepareScene()
	{
		/* Pega a posição e o tamanho finais da view que será animada */
		mEndValues = captureValues(moviePoster);

		/* Calcula o delta do tamanho e da posicao a serem animados utilizando os valores iniciais e finais da view */
		scaleX = scaleDelta(mStartValues, mEndValues, SHARED_VIEW_WIDTH);
		scaleY = scaleDelta(mStartValues, mEndValues, SHARED_VIEW_HEIGHT);
		deltaX = translationDelta(mStartValues, mEndValues, SHARED_VIEW_SCREEN_LOCATION_LEFT);
		deltaY = translationDelta(mStartValues, mEndValues, SHARED_VIEW_SCREEN_LOCATION_TOP);

		/*
		 * Seta o quanto a view está escalada no eixo X, como uma proporção da largura da view não escalada.
		 * Se o valor for igual a 1 significa que a view não foi escalada(aumentada ou diminuida)
		 */
		moviePoster.setScaleX(scaleX);
		/*
		 * Seta o quanto a view está escalada no eixo Y, como uma proporção da largura da view não escalada
		 * Se o valor for igual a 1 significa que a view não foi escalada(aumentada ou diminuida)
		 */
		moviePoster.setScaleY(scaleY);
		moviePoster.setTranslationX(deltaX);
		moviePoster.setTranslationY(deltaY);
	}

	private void showAllViewsWithSlideAnimationAndAnimateWindowBackgroundColor()
	{
		Animator animFadeInToolbar = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInToolbar = createSlideRightInObjectAnimator(toolbar);
		Animator animFadeInShadowView = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInShadowView = createSlideRightInObjectAnimator(shadowView);
		Animator animFadeInMovieTitle = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInMovieTitle = createSlideRightInObjectAnimator(movieTitle);
		Animator animFadeInMovieReleaseDate = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInMovieReleaseDate = createSlideRightInObjectAnimator(movieReleaseDate);
		Animator animFadeInMovieVoteAverage = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInMovieVoteAverage = createSlideRightInObjectAnimator(movieVoteAverage);
		Animator animFadeInMovieSynopsis = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInMovieSynopsis = createSlideRightInObjectAnimator(movieSynopsis);
		Animator animFadeInLinearLayoutTrailers = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInLinearLayoutTrailers = createSlideRightInObjectAnimator(linearLayoutTrailers);
		Animator animFadeInLinearLayoutReviews = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInLayoutReviews = createSlideRightInObjectAnimator(linearLayoutReviews);
		Animator animFadeInFab = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in);
		Animator animSlideRightInfab = createSlideRightInObjectAnimator(fab);

		animFadeInToolbar.setTarget(toolbar);
		animFadeInShadowView.setTarget(shadowView);
		animFadeInMovieTitle.setTarget(movieTitle);
		animFadeInMovieReleaseDate.setTarget(movieReleaseDate);
		animFadeInMovieVoteAverage.setTarget(movieVoteAverage);
		animFadeInMovieSynopsis.setTarget(movieSynopsis);
		animFadeInLinearLayoutTrailers.setTarget(linearLayoutTrailers);
		animFadeInLinearLayoutReviews.setTarget(linearLayoutReviews);
		animFadeInFab.setTarget(fab);

		int colorFrom = ActivityCompat.getColor(getApplicationContext(), android.R.color.transparent);
		int colorTo = ActivityCompat.getColor(getApplicationContext(), R.color.standard_background);
		/* Animator para o background color do window */
		ObjectAnimator colorAnimator = ObjectAnimator.ofInt(getWindow().getDecorView(),
				  "backgroundColor", colorFrom, colorTo)
				  .setDuration(getResources().getInteger(R.integer.anim_element_leaving_screen));
		colorAnimator.setEvaluator(new ArgbEvaluator());

		/* Cria um AnimatorSet para executarem juntas as animações de fade in, slide e de background color */
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(colorAnimator, animFadeInToolbar, animSlideRightInToolbar,
				  animFadeInShadowView, animSlideRightInShadowView, animFadeInMovieTitle,
				  animSlideRightInMovieTitle, animFadeInMovieReleaseDate, animSlideRightInMovieReleaseDate,
				  animFadeInMovieVoteAverage, animSlideRightInMovieVoteAverage, animFadeInMovieSynopsis,
				  animSlideRightInMovieSynopsis, animFadeInLinearLayoutTrailers, animSlideRightInLinearLayoutTrailers,
				  animFadeInLinearLayoutReviews, animSlideRightInLayoutReviews, animFadeInFab, animSlideRightInfab);
		animatorSet.start();
	}

	private void runEnterAnimation()
	{
		/* A view a ser animada pode ser mostrada */
		moviePoster.setVisibility(View.VISIBLE);

		/*
		 * Executa a animação de entrada da view.
		 * A view será animada do quanto ela está escalada(setado anteriormente no método setScaleX e setScaleY)
		 * até o tamanho normal que é 1 e será transladada(movida para uma posição diferente)
		 */
		moviePoster.animate().setDuration(getResources().getInteger(R.integer.anim_element_enter_screen))
		  .setInterpolator(DEFAULT_INTERPOLATOR)
		  .scaleX(1f).scaleY(1f)
		  .translationX(0).translationY(0).withStartAction(new Runnable()
		{
			@Override
			public void run()
			{
				/*
				 * Quando a animação de translation da imagem começar também irá começar a animação de fade
				 * e de entrada do resto das views
				 */
				showAllViewsWithSlideAnimationAndAnimateWindowBackgroundColor();
			}
		}).start();
	}

	private void hideAllViewsWithSlideAnimationAndAnimateWindowBackgroundColor()
	{
		Animator animSlideRightOutToolbar = createSlideRightOutObjectAnimator(toolbar);
		Animator animSlideRightOutShadowView = createSlideRightOutObjectAnimator(shadowView);
		Animator animSlideRightOutMovieTitle = createSlideRightOutObjectAnimator(movieTitle);
		Animator animSlideRightOutMovieReleaseDate = createSlideRightOutObjectAnimator(movieReleaseDate);
		Animator animSlideRightOutMovieVoteAverage = createSlideRightOutObjectAnimator(movieVoteAverage);
		Animator animSlideRightOutMovieSynopsis = createSlideRightOutObjectAnimator(movieSynopsis);
		Animator animSlideRightOutLinearLayoutTrailers = createSlideRightOutObjectAnimator(linearLayoutTrailers);
		Animator animSlideRightOutLayoutReviews = createSlideRightOutObjectAnimator(linearLayoutReviews);
		Animator animSlideRightOutfab = createSlideRightOutObjectAnimator(fab);

		/* Cria um AnimatorSet para executarem juntas as animações de fade in, slide e de background color */
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(animSlideRightOutToolbar, animSlideRightOutShadowView, animSlideRightOutMovieTitle,
				  animSlideRightOutMovieReleaseDate, animSlideRightOutMovieVoteAverage, animSlideRightOutMovieSynopsis,
				  animSlideRightOutLinearLayoutTrailers, animSlideRightOutLayoutReviews, animSlideRightOutfab);
		animatorSet.start();
	}

	private void runExitAnimation()
	{
//		/* Executa a animação de saída da view */
		moviePoster.animate().setDuration(getResources().getInteger(R.integer.anim_element_enter_screen))
		 .setInterpolator(DEFAULT_INTERPOLATOR)
		 .scaleX(scaleX).scaleY(scaleY)
		 .translationX(deltaX).translationY(deltaY).withStartAction(new Runnable()
		{
			@Override
			public void run()
			{
				hideAllViewsWithSlideAnimationAndAnimateWindowBackgroundColor();
			}
		}).withEndAction(new Runnable()
		{
			@Override
			public void run()
			{
				finish();
				overridePendingTransition(0, 0);
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		}).start();
//
//
//
//		Animator animFadeInToolbar = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//		Animator animFadeInShadowView = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//		Animator animFadeInMovieTitle = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//		Animator animFadeInMovieReleaseDate = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//		Animator animFadeInMovieVoteAverage = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//		Animator animFadeInMovieSynopsis = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//		Animator animFadeInLinearLayoutTrailers = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//		Animator animFadeInLinearLayoutReviews = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//		Animator animFadeInFab = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out);
//
//
//		animFadeInToolbar.setTarget(toolbar);
//		animFadeInShadowView.setTarget(shadowView);
//		animFadeInMovieTitle.setTarget(movieTitle);
//		animFadeInMovieReleaseDate.setTarget(movieReleaseDate);
//		animFadeInMovieVoteAverage.setTarget(movieVoteAverage);
//		animFadeInMovieSynopsis.setTarget(movieSynopsis);
//		animFadeInLinearLayoutTrailers.setTarget(linearLayoutTrailers);
//		animFadeInLinearLayoutReviews.setTarget(linearLayoutReviews);
//		animFadeInFab.setTarget(fab);
//
//
//		AnimatorSet animatorSet = new AnimatorSet();
//		animatorSet.playTogether(animFadeInToolbar, animFadeInShadowView, animFadeInMovieTitle,
//				  animFadeInMovieReleaseDate, animFadeInMovieVoteAverage, animFadeInMovieSynopsis,
//				  animFadeInLinearLayoutTrailers, animFadeInLinearLayoutReviews, animFadeInFab);
//		animatorSet.start();
	}

	/* Cria um ObjectAnimator com animação de slide da direita para a esquerda */
	private Animator createSlideRightInObjectAnimator(View view)
	{
		/*
		 * "getRootView()" acha a view pai da hierarquia de views do layout.
		 * Esse método é usado para pegar a largura da view pai da hierarquia do layout para dizer
		 * que a animação de slide vai começar do ponto mais a direita
		 */
		ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0);
		animation.setDuration(getResources().getInteger(R.integer.anim_full_screen));
		return animation;
	}

	/* Cria um ObjectAnimator com animação de slide da esquerda para a direita */
	private Animator createSlideRightOutObjectAnimator(View view)
	{
		/*
		 * "getRootView()" acha a view pai da hierarquia de views do layout.
		 * Esse método é usado para pegar a largura da view pai da hierarquia do layout para dizer
		 * que a animação de slide vai começar do ponto mais a direita
		 */
		ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", 0, view.getRootView().getWidth());
		animation.setDuration(getResources().getInteger(R.integer.anim_element_leaving_screen));
		return animation;
	}
}
