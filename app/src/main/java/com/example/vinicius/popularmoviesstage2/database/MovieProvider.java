package com.example.vinicius.popularmoviesstage2.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by vinicius on 17/03/17.
 */

public class MovieProvider extends ContentProvider
{
	/*
	 * Aqui precisa ser definido um identificador inteiro para cada URI que pretendemos escrever.
	 * Neste caso teremos dois identificadores para cada query. Uma URI para todas as linhas
	 * e uma URI para uma linha individual
	 */
	private static final int MOVIE = 200;
	private static final int MOVIE_ID = 201;

	private static final UriMatcher sUriMatcher = buildUriMatcher();
	/* Variavel usada para acessar o banco de dados */
	private MovieDbHelper mOpenHelper;

	@Override
	public boolean onCreate()
	{
		mOpenHelper = new MovieDbHelper(getContext());
		return true;
	}

	/*
	 * Os parâmetros deste metodo são:
	 *
	 * uri - a URI que deve ser consultada
	 * projection - um array de strings de colunas que serão retornadas no conjunto de resultados
	 * selection - especifica o critério para selecionar as linhas (equivalente ao WHERE col = value em SQL)
	 * selectionArgs - lista de argumentos que substituem os ? no parametro selection
	 * sortOrder - especifica a ordem na qual as linhas irão aparecer no Cursor retornado
	 */
	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
							  @Nullable String[] selectionArgs, @Nullable String sortOrder)
	{
		final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cursor;
		long _id;

		/*
		 * Se houver um match da URI passada para cosulta então será retornado o inteiro (terceiro parametro
		 * que foi adicionado dentro do método "matcher.addURI()") que foi
		 * definido para essa URI dentro do método buildUriMatcher()
		 */
		switch(sUriMatcher.match(uri))
		{
			case MOVIE:
				cursor = db.query(
						  MovieContract.MovieEntry.TABLE_NAME,
						  projection,
						  selection,
						  selectionArgs,
						  null,
						  null,
						  sortOrder
				);

				break;

			case MOVIE_ID:
				_id = ContentUris.parseId(uri);
				cursor = db.query(
						  MovieContract.MovieEntry.TABLE_NAME,
						  projection,
						  MovieContract.MovieEntry._ID + " = ?",
						  new String[]{String.valueOf(_id)},
						  null,
						  null,
						  sortOrder
				);

				break;

			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);

		}

		/* notifica o listener do content resolver do chamador desta função sobre mudanças na uri */
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;

	}

	/*
	 * Este método é usado para retornar o content type de cada URI que foi definida dentro do método
	 * buildUriMatcher()
	 */
	@Nullable
	@Override
	public String getType(@NonNull Uri uri)
	{
		/*
		 * Se houver um match da URI passada para cosulta então será retornado o inteiro (terceiro parametro
		 * que foi adicionado dentro do método "matcher.addURI()") que foi
		 * definido para essa URI dentro do método buildUriMatcher()
		 */
		switch(sUriMatcher.match(uri))
		{
			case MOVIE:
				return MovieContract.MovieEntry.CONTENT_TYPE;

			case MOVIE_ID:
				return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues)
	{
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long _id;
		Uri returnUri;

		/*
		 * Se houver um match da URI passada para cosulta então será retornado o inteiro (terceiro parametro
		 * que foi adicionado dentro do método "matcher.addURI()") que foi
		 * definido para essa URI dentro do método buildUriMatcher()
		 */
		switch(sUriMatcher.match(uri))
		{
			case MOVIE:
				_id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);

				if(_id > 0)
				{
					returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
				}
				else
				{
					throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
				}

				break;

			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		/* Este método é usado para notificar os listeners que aconteceu uma mudança na uri */
		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
	{
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int rows;
		long _id;

		switch(sUriMatcher.match(uri))
		{
			case MOVIE:
				rows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
				break;

			case MOVIE_ID:
				_id = ContentUris.parseId(uri);
				rows = db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry._ID + " = ?",
						  new String[]{String.valueOf(_id)});
				break;

			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		if(selection == null || rows != 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rows;

	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
							@Nullable String[] selectionArgs)
	{
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int rows;

		switch(sUriMatcher.match(uri))
		{
			case MOVIE:
				rows = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		if(rows != 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rows;

	}

	/* Constói uma UriMatcher que é usada para determinar qual requisição ao banco de dados está sendo feita */
	public static UriMatcher buildUriMatcher(){
		String content = MovieContract.CONTENT_AUTHORITY;

		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(content, MovieContract.PATH_MOVIE, MOVIE);
		/*
		 * O /# é usado como placeholder para um valor numérico. Neste caso usaríamos
		 *	content://com.example.vinicius.myappportfolio/movie/1 para o filme com id 1
		 */
		matcher.addURI(content, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);

		return matcher;
	}
}
