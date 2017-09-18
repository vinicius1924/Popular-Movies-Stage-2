package com.example.vinicius.popularmoviesstage2.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.model.preferences.base.PreferenceHelper;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by vinicius on 14/09/17.
 */

@Singleton
public class AppPreferenceHelper implements PreferenceHelper
{
	private final SharedPreferences mPrefs;
	private final @Named("ApplicationContext") Context context;

	@Inject
	public AppPreferenceHelper(@Named("ApplicationContext") Context context)
	{
		this.context = context;
		this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public void setMoviesSortBy(String orderBy)
	{
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString(context.getResources().getString(R.string.movies_order_by_shared_preferences_key),
				  orderBy);
		editor.apply();
	}

	@Override
	public String getMoviesSortBy()
	{
		String value = mPrefs.getString(context.getResources().getString(R.string.movies_order_by_shared_preferences_key),
				  context.getResources().getString(R.string.first_pref_list_entry));

		return value;
	}
}
