package com.example.vinicius.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vinicius on 19/03/17.
 */

public class FavoritesRecyclerAdapter extends RecyclerView.Adapter<FavoritesRecyclerAdapter.CustomViewHolder>
{
	private List<String> favoritesList;
	private Context mContext;

	public FavoritesRecyclerAdapter(Context mContext, List<String> moviesList)
	{
		this.mContext = mContext;
		this.favoritesList = moviesList;
	}

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

		CustomViewHolder viewHolder = new CustomViewHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(CustomViewHolder customViewHolder, int position)
	{
		String movieId = favoritesList.get(position);

		customViewHolder.movieId.setText(movieId);
	}

	@Override
	public int getItemCount()
	{
		return (null != favoritesList ? favoritesList.size() : 0);
	}

	class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		protected TextView movieId;

		public CustomViewHolder(View itemView)
		{
			super(itemView);

			itemView.setOnClickListener(this);

			this.movieId = (TextView) itemView.findViewById(android.R.id.text1);
		}

		@Override
		public void onClick(View view)
		{
		}
	}
}
