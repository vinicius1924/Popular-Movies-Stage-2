package com.example.vinicius.popularmoviesstage2.dependency_injection.modules;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;

import com.example.vinicius.popularmoviesstage2.model.data_manager.AppDataManager;
import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.model.database.AppDbHelper;
import com.example.vinicius.popularmoviesstage2.model.api.base.ApiHelper;
import com.example.vinicius.popularmoviesstage2.model.api.AppApiHelper;
import com.example.vinicius.popularmoviesstage2.model.database.base.DbHelper;
import com.example.vinicius.popularmoviesstage2.model.preferences.AppPreferenceHelper;
import com.example.vinicius.popularmoviesstage2.model.preferences.base.PreferenceHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by vinicius on 11/09/17.
 */

/*
 * @Module é usado para classes cujos métodos fornecem dependencias
 *
 * Esta classe provê dependencia das classes Context, Application, String,
 * DataManager, DbHelper, PreferencesHelper e ApiHelper
 */
@Module
public class ApplicationModule
{
	private final Application mApplication;

	public ApplicationModule(Application mApplication)
	{
		this.mApplication = mApplication;
	}

	@Provides
	@Named("ApplicationContext")
	Context provideContext()
	{
		return mApplication;
	}

	@Provides
	Application provideApplication() {
		return mApplication;
	}

	@Provides
	ContentValues provideContentValues() {
		return new ContentValues();
	}

	/*
	 * métodos anotados com @Provides podem ter dependencias próprias, ou seja, aqui
	 * quando for pedido uma instância da interface DataManager será retornado uma instância
	 * de AppDataManager que imlementa DataManager.
	 *
	 * OBS: para isso funcionar, a classe AppDataManager deve ter um construtor anotado com
	 * @Inject
	 */
	@Provides
	@Singleton
	DataManager provideDataManager(AppDataManager appDataManager)
	{
		return appDataManager;
	}

	@Provides
	@Singleton
	DbHelper provideDbHelper(AppDbHelper appDbHelper)
	{
		return appDbHelper;
	}

	@Provides
	@Singleton
	ApiHelper provideApiHelper(Retrofit retrofit)
	{
		return new AppApiHelper<>(retrofit);
	}

	@Provides
	@Singleton
	PreferenceHelper providePreferenceHelper(AppPreferenceHelper preferenceHelper)
	{
		return preferenceHelper;
	}
}
