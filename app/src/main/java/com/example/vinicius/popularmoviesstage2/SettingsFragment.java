package com.example.vinicius.popularmoviesstage2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vinicius on 14/03/17.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
	public interface PreferenceChangedEventListener
	{
		public void onPreferenceChangedEvent(String preference);
	}

	PreferenceChangedEventListener preferenceChangedEventListener;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		/* Adapted from: http://codetheory.in/saving-user-settings-with-android-preferences/ */
		for(int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++)
		{
			pickPreferenceObject(getPreferenceScreen().getPreference(i));
		}
	}

	private void pickPreferenceObject(Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory cat = (PreferenceCategory) p;
			for (int i = 0; i < cat.getPreferenceCount(); i++) {
				pickPreferenceObject(cat.getPreference(i));
			}
		} else {
			initSummary(p);
		}
	}

	private void initSummary(Preference p) {

		if (p instanceof ListPreference) {
			ListPreference listPref = (ListPreference) p;
			p.setSummary(listPref.getValue());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = super.onCreateView(inflater, container, savedInstanceState);
		view.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));

		return view;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
		{
			return;
		}

		if (activity instanceof PreferenceChangedEventListener)
		{
			preferenceChangedEventListener = (PreferenceChangedEventListener) activity;
		}
		else
		{
			throw new RuntimeException(activity.toString() + " must implement PreferenceChangedEventListener");
		}
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);

		if (context instanceof PreferenceChangedEventListener)
		{
			preferenceChangedEventListener = (PreferenceChangedEventListener) context;
		}
		else
		{
			throw new RuntimeException(context.toString() + " must implement PreferenceChangedEventListener");
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();

		preferenceChangedEventListener = null;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();

		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		preferenceChangedEventListener.onPreferenceChangedEvent(sharedPreferences.getString(key, ""));

		if(key.equals(getResources().getString(R.string.movies_order_by_shared_preferences_key)))
		{
			Preference preference = findPreference(key);
			preference.setSummary(sharedPreferences.getString(key, ""));
		}
	}
}
