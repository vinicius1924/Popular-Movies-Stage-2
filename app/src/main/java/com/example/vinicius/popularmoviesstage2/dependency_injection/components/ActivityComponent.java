package com.example.vinicius.popularmoviesstage2.dependency_injection.components;

import com.example.vinicius.popularmoviesstage2.view.movie_activity.MovieActivity;
import com.example.vinicius.popularmoviesstage2.view.movies_list_activity.MoviesListActivity;
import com.example.vinicius.popularmoviesstage2.dependency_injection.PerActivity;
import com.example.vinicius.popularmoviesstage2.dependency_injection.modules.ActivityModule;

import dagger.Component;

/**
 * Created by vinicius on 12/09/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent
{
	void inject(MoviesListActivity activity);
	void inject(MovieActivity activity);
}
