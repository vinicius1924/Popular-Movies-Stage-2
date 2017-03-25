package com.example.vinicius.popularmoviesstage2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vinicius on 17/03/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper
{
	/* Se o banco de dados for mudado então esse número deve ser incrementado */
	private static final int DATABASE_VERSION = 1;

	/* Nome do banco de dados */
	static final String DATABASE_NAME = "movie.db";

	public MovieDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
				  MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
				  MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL " +
				  " );";

		sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);

		onCreate(sqLiteDatabase);
	}
}
