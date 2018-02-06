package com.example.vinicius.popularmoviesstage2.dependency_injection.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vinicius on 05/02/18.
 */
@Module
public class NetworkModule
{
	private String mBaseUrl;

	public NetworkModule(String mBaseUrl)
	{
		this.mBaseUrl = mBaseUrl;
	}

	@Provides
	@Singleton
	Retrofit provideRetrofit()
	{
		return new Retrofit.Builder()
				  .baseUrl(mBaseUrl)
				  .addConverterFactory(GsonConverterFactory.create())
				  .build();
	}
}
