package com.example.vinicius.popularmoviesstage2.view.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.example.vinicius.popularmoviesstage2.MvpApp;
import com.example.vinicius.popularmoviesstage2.dependency_injection.components.ActivityComponent;
import com.example.vinicius.popularmoviesstage2.dependency_injection.components.DaggerActivityComponent;
import com.example.vinicius.popularmoviesstage2.dependency_injection.modules.ActivityModule;

/**
 * Created by vinicius on 12/09/17.
 */

/*
 * Já que todas as Views (Activities, Fragments e CustomViews) devem implementar MvpView,
 * então foi criada uma classe abstrata chamada BaseActivity que implementa MvpView
 * e que serve para que as Activities extendam ela para herdar todos os seus atributos e métodos.
 */
public abstract class BaseActivity extends AppCompatActivity implements MvpView
{
	private ActivityComponent mActivityComponent;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mActivityComponent = DaggerActivityComponent.builder()
				  .activityModule(new ActivityModule(this))
				  .applicationComponent(((MvpApp) getApplication()).getComponent())
				  .build();
	}

	public ActivityComponent getActivityComponent() {
		return mActivityComponent;
	}

	@Override
	public void showToast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
	}
}
