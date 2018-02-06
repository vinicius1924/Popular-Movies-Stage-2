package com.example.vinicius.popularmoviesstage2.dependency_injection.components;

import android.app.Application;
import android.content.Context;

import com.example.vinicius.popularmoviesstage2.MvpApp;
import com.example.vinicius.popularmoviesstage2.dependency_injection.modules.NetworkModule;
import com.example.vinicius.popularmoviesstage2.model.data_manager.base.DataManager;
import com.example.vinicius.popularmoviesstage2.dependency_injection.modules.ApplicationModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by vinicius on 11/09/17.
 */

/*
 * @Component é uma interface que faz a ponte entre modulos e as injecoes.
 *
 * Componentes definem de quais módulos (ou outros componentes declarados como dependencies)
 * as dependencias serão providas
 *
 * Aqui o componente ApplicationComponent utiliza o modulo ApplicationModule para criar objetos
 * do tipo Context, Application e DataManager
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent
{
	/*
	 * Os métodos definidos na interface estão disponíveis para ser usados
	 * pelos objetos gerados através de DaggerApplicationComponent.builder().
	 * applicationModule(new ApplicationModule(this)).build()
	 *
	 * OBS: quando passar "this" no construtor de ApplicationModule, certifique-se
	 * de que a classe onde este construtor está sendo chamado extenda a classe
	 * Application
	 *
	 * O método inject abaixo irá atribuir referencias de objetos aos campos declarados
	 * com @Inject na classe que o chamou
	 */
	void inject(MvpApp app);

	@Named("ApplicationContext")
	Context context();

	Application application();

	DataManager getDataManager();
}
