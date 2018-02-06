package com.example.vinicius.popularmoviesstage2;

import android.app.Application;

import com.example.vinicius.popularmoviesstage2.dependency_injection.modules.NetworkModule;
import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.dependency_injection.components.ApplicationComponent;
import com.example.vinicius.popularmoviesstage2.dependency_injection.components.DaggerApplicationComponent;
import com.example.vinicius.popularmoviesstage2.dependency_injection.modules.ApplicationModule;

import javax.inject.Inject;

/**
 * Created by vinicius on 11/09/17.
 */

public class MvpApp extends Application
{
	/* Ver explicação de como o @Inject vai achar esses objetos mais abaixo */
	@Inject
	DataManager mDataManager;

	private ApplicationComponent mApplicationComponent;

	@Override
	public void onCreate()
	{
		super.onCreate();

		mApplicationComponent = DaggerApplicationComponent.builder()
				  .applicationModule(new ApplicationModule(this))
				  .networkModule(new NetworkModule("https://api.themoviedb.org/3/"))
				  .build();

		/*
		 * Ao chamar o método inject do component ApplicationComponent o dagger vai procurar nos módulos
		 * de ApplicationComponent, ou nos componentes declarados como dependencies em ApplicationComponent,
		 * um método anotado com @Provides que retorne os tipos anotados com @Inject e vai atribuir
		 * referencias desses objetos a esses campos
		 */
		mApplicationComponent.inject(this);
	}

	public ApplicationComponent getComponent() {
		return mApplicationComponent;
	}
}
