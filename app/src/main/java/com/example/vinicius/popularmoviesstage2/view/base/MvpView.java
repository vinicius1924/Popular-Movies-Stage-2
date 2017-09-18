package com.example.vinicius.popularmoviesstage2.view.base;

/**
 * Created by vinicius on 12/09/17.
 */

/*
 * Esta é a interface que deve ser implementada por todas as Views (Activities,
 * Fragments e CustomViews). Ela contém os métodos mais genéricos que uma view
 * deve implementar e esses métodos serão expostos para comunicação com o Presenter
 */
public interface MvpView
{
	void showToast(String message);
}
