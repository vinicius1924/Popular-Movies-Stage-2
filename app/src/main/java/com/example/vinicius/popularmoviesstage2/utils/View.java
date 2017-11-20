package com.example.vinicius.popularmoviesstage2.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieActivity;

import java.util.Locale;

/**
 * Created by vinicius on 10/10/17.
 */

public final class View
{
	public static Bundle captureValues(@NonNull android.view.View view)
	{
		Bundle b = new Bundle();
		int[] screenLocation = new int[2];
		view.getLocationOnScreen(screenLocation);
		b.putInt(MovieActivity.SHARED_VIEW_SCREEN_LOCATION_LEFT, screenLocation[0]);
		b.putInt(MovieActivity.SHARED_VIEW_SCREEN_LOCATION_TOP, screenLocation[1]);
		b.putInt(MovieActivity.SHARED_VIEW_WIDTH, view.getWidth());
		b.putInt(MovieActivity.SHARED_VIEW_HEIGHT, view.getHeight());

		return b;
	}

	/* Calcula quanto a view deve aumentar ou diminuir do ponto inicial ao ponto final */
	public static float scaleDelta(
			  @NonNull Bundle startValues,
			  @NonNull Bundle endValues,
			  @NonNull String propertyName) {

		int startValue = startValues.getInt(propertyName);
		int endValue = endValues.getInt(propertyName);
		float delta = (float) startValue / endValue;

		//Log.d("MyLog", String.format(Locale.US, "%s: startValue = %d, endValue = %d, delta = %f", propertyName, startValue, endValue, delta));

		return delta;
	}

	/* Calcula quanto a view deve se mover do ponto inicial ao ponto final */
	public static int translationDelta(
			  @NonNull Bundle startValues,
			  @NonNull Bundle endValues,
			  @NonNull String propertyName) {

		int startValue = startValues.getInt(propertyName);
		int endValue = endValues.getInt(propertyName);
		int delta = startValue - endValue;

		//Log.d("MyLog",String.format(Locale.US, "%s: startValue = %d, endValue = %d, delta = %d", propertyName, startValue, endValue, delta));

		return delta;
	}
}
