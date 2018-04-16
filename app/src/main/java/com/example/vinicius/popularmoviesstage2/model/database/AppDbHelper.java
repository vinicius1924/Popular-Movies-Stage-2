package com.example.vinicius.popularmoviesstage2.model.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.database.MovieContract;
import com.example.vinicius.popularmoviesstage2.model.database.base.DbHelper;
import com.example.vinicius.popularmoviesstage2.services.DownloadIntentService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by vinicius on 11/09/17.
 */

@Singleton
public class AppDbHelper implements DbHelper
{
	private final ContentResolver contentResolver;
	private final @Named("ApplicationContext")Context context;
	private final ContentValues contentValues;

	@Inject
	public AppDbHelper(@Named("ApplicationContext")Context context, ContentValues contentValues)
	{
		this.contentResolver = context.getContentResolver();
		this.context = context;
		this.contentValues = contentValues;
	}

	@Override
	public void insertMovie(MovieDTO movieDTO)
	{
		contentValues.put(MovieContract.MovieEntry._ID, movieDTO.getId());
		/*
		 * O método "getFilesDir()" retorna o mesmo caminho que o método "openFileOutput()" utilizado no service
		 * DownloadIntentService para salvar a imagem
		 */
		contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, context.getFilesDir() + "/" +
				  movieDTO.getPoster(context.getResources().getBoolean(R.bool.smallestWidth600),
							 context.getResources().getBoolean(R.bool.smallestWidth720)).substring(movieDTO.
							 getPoster(context.getResources().getBoolean(R.bool.smallestWidth600),
										context.getResources().getBoolean(R.bool.smallestWidth720)).lastIndexOf("/") + 1));
		contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieDTO.getOriginalTitle());
		contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieDTO.getReleaseDate());
		contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, String.valueOf(movieDTO.getVoteAverage()));
		contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movieDTO.getOverview());

		DownloadIntentService.startActionDownload(context, movieDTO.getPoster(context.getResources().getBoolean(R.bool.smallestWidth600),
				  context.getResources().getBoolean(R.bool.smallestWidth720)),
				  movieDTO.getPoster(context.getResources().getBoolean(R.bool.smallestWidth600),
							 context.getResources().getBoolean(R.bool.smallestWidth720)).substring(movieDTO.
							 getPoster(context.getResources().getBoolean(R.bool.smallestWidth600),
										context.getResources().getBoolean(R.bool.smallestWidth720)).lastIndexOf("/") + 1));

		Uri insertedUri = contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
	}

	@Override
	public void deleteMovie(long id, String posterPath, MovieDTO movieDTO)
	{
		String poster = posterPath;

		ContentResolver resolver = context.getContentResolver();

		if(posterPath.startsWith("http"))
		{
			/*
			 * O método "getFilesDir()" retorna o mesmo caminho que o método "openFileOutput()" utilizado no service
			 * DownloadIntentService para salvar a imagem
			 */
			poster = context.getFilesDir() + "/" + movieDTO.getPoster(context.getResources().getBoolean(R.bool.smallestWidth600),
					  context.getResources().getBoolean(R.bool.smallestWidth720)).substring(movieDTO.getPoster(
					  context.getResources().getBoolean(R.bool.smallestWidth600),
					  context.getResources().getBoolean(R.bool.smallestWidth720)).lastIndexOf("/") + 1);
		}

		DownloadIntentService.startActionDelete(context, poster);

		resolver.delete(Uri.withAppendedPath(MovieContract.MovieEntry.CONTENT_URI,
				  String.valueOf(id)), null, null);
	}

	@Override
	public Cursor queryMovieById(long id)
	{
		return context.getContentResolver().query(Uri.withAppendedPath(MovieContract.MovieEntry.CONTENT_URI,
				  String.valueOf(id)), new String[]{MovieContract.MovieEntry._ID}, null, null, null);
	}
}
