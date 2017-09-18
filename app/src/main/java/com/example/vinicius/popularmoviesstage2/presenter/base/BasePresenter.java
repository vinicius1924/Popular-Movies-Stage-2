package com.example.vinicius.popularmoviesstage2.presenter.base;

import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.view.base.MvpView;

/**
 * Created by vinicius on 12/09/17.
 */

/*
 * Já que todos os Presenters devem implementar MvpPresenter, então foi criada uma classe
 * chamada BasePresenter que implementa MvpPresenter e que serve para que os Presenters
 * de cada View extendam ela para herdar todos os seus atributos e métodos.
 */
public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V>
{
	private final DataManager mDataManager;
	private V mMvpView;

	public BasePresenter(DataManager dataManager)
	{
		this.mDataManager = dataManager;
	}

	@Override
	public void registerView(V mvpView)
	{
		mMvpView = mvpView;
	}

	@Override
	public void unregisterView()
	{
		mMvpView = null;
	}

	public V getMvpView() throws NullPointerException
	{
		if(mMvpView != null)
		{
			return mMvpView;
		}
		else
		{
			throw new NullPointerException("View is unavailable");
		}
	}

	public DataManager getDataManager() throws NullPointerException
	{
		if(mDataManager != null)
		{
			return mDataManager;
		}
		else
		{
			throw new NullPointerException("DataManager is unavailable");
		}
	}
}
