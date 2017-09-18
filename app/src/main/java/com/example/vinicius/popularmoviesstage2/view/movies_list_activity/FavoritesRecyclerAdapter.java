package com.example.vinicius.popularmoviesstage2.view.movies_list_activity;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vinicius.popularmoviesstage2.CursorRecyclerAdapter;
import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.example.vinicius.popularmoviesstage2.R;
import com.example.vinicius.popularmoviesstage2.database.MovieContract;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by vinicius on 19/03/17.
 */

public class FavoritesRecyclerAdapter extends CursorRecyclerAdapter<FavoritesRecyclerAdapter.CustomViewHolder>
{
	private Context context;
	private FavoritesListItemClickListener mOnClickListener;

	public FavoritesRecyclerAdapter(Context context, Cursor c, int flags, FavoritesListItemClickListener mOnClickListener)
	{
		super(context, c, flags);

		this.context = context;
		this.mOnClickListener = mOnClickListener;
	}

	public interface FavoritesListItemClickListener
	{
		void onFavoriteListItemClick(MovieDTO movieDTO);
	}

	@Override
	public void bindViewHolder(CustomViewHolder customViewHolder, Context context, Cursor cursor)
	{
		MovieDTO movieDTO = new MovieDTO();
		movieDTO.setId(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
		movieDTO.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
		movieDTO.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
		movieDTO.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
		movieDTO.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING)));
		movieDTO.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS)));

		String poster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));

		customViewHolder.bindMovie(movieDTO);

		Picasso.with(context).load(new File(poster)).placeholder(R.drawable.image_placeholder)
				  .into(customViewHolder.thumbnailImage);
	}

	@Override
	public FavoritesRecyclerAdapter.CustomViewHolder createViewHolder(Context context, ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_recycler_view_layout, parent, false);

		CustomViewHolder viewHolder = new CustomViewHolder(view);

		return viewHolder;
	}

	class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		protected ImageView thumbnailImage;
		protected MovieDTO movie;

		public CustomViewHolder(View itemView)
		{
			super(itemView);

			itemView.setOnClickListener(this);

			this.thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail);
		}

		public void bindMovie(MovieDTO movie) {
			this.movie = movie;
		}

		@Override
		public void onClick(View view)
		{
			mOnClickListener.onFavoriteListItemClick(movie);
		}
	}
}
