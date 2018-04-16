package com.example.vinicius.popularmoviesstage2.presenter.base;

import com.example.vinicius.popularmoviesstage2.view.base.MvpView;

/**
 * Created by vinicius on 12/09/17.
 */

/*
 * Esta é a interface que deve ser implementada por todos os Presenters.
 * Ela contém os métodos mais genéricos que um Presenter
 * deve implementar e esses métodos serão expostos para comunicação com a View
 *
 * Como a interface MvpPresenter vai se comunicar com a View e toda a View extends
 * a BaseActivity que implementa o MvpView, a interface MvpPresenter tem que ser de
 * algum tipo que extenda MvpView
 */
public interface MvpPresenter<V extends MvpView>
{
	void registerView(V mvpView);

	void unregisterView();
}
