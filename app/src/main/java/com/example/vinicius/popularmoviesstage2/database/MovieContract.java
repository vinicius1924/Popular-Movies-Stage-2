package com.example.vinicius.popularmoviesstage2.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vinicius on 17/03/17.
 */

/* Define os nomes das tabelas e das colunas */
public class MovieContract
{
	/*
	 * O "Content authority" é um nome para o content provider, similar como a relação entre um dominio
	 * e seu website. Uma string conveniente para usar no content authority é o package name do app, que
	 * é garantido que será único
	 */
	public static final String CONTENT_AUTHORITY = "com.example.vinicius.popularmoviesstage2";

	/*
	 * Usa-se o CONTENT_AUTHORITY para criar a base de todas as URI's que serão usadas para chamar
	 * o content provider
	 */
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

	/*
	 * Possiveis caminhos (adicionados a BASE_CONTENT_URI para possiveis URI's).
	 * Por exemplo, content://com.example.vinicius.myappportfolio/movie/ é um caminho válido para
	 * procurar por dados de um filme. content://com.example.vinicius.myappportfolio/givemeroot/
	 * irá falhar, porque o content provider não tem nenhuma informação sobre o que fazer com "givemeroot"
	 */
	public static final String PATH_MOVIE = "movie";

	/* Classe que define o conteúdo da tabela Movie */
	public static final class MovieEntry implements BaseColumns
	{
		public static final Uri CONTENT_URI =
				  BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

		/* O content type de um diretório de items */
		public static final String CONTENT_TYPE =
				  ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
		/* O content type de um único item */
		public static final String CONTENT_ITEM_TYPE =
				  ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

		public static final String TABLE_NAME = "movie";

		public static final String COLUMN_TITLE = "title";

		/* Função para construir uma URI para encontrar um filme específico por seu id */
		public static Uri buildMovieUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
