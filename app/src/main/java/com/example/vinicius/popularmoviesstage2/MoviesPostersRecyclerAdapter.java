package com.example.vinicius.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vinicius on 12/03/17.
 */

public class MoviesPostersRecyclerAdapter extends RecyclerView.Adapter<MoviesPostersRecyclerAdapter.CustomViewHolder>
{
	private List<MovieDTO> moviesList;
	private Context mContext;
	private final ListItemClickListener mOnClickListener;

	public MoviesPostersRecyclerAdapter(Context context, List<MovieDTO> moviesList, ListItemClickListener mOnClickListener)
	{
		this.moviesList = moviesList;
		this.mContext = context;
		this.mOnClickListener = mOnClickListener;
	}

	public interface ListItemClickListener
	{
		void onListItemClick(int clickedItemIndex);
	}

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_recycler_view_layout, parent, false);

		CustomViewHolder viewHolder = new CustomViewHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(CustomViewHolder customViewHolder, int position)
	{
		MovieDTO movieDTO = moviesList.get(position);

		Picasso.with(mContext).load(movieDTO.getPoster()).placeholder(R.drawable.image_placeholder)
				  .into(customViewHolder.thumbnailImage);
	}

	@Override
	public int getItemCount()
	{
		return (null != moviesList ? moviesList.size() : 0);
	}

	class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{
		protected ImageView thumbnailImage;

		public CustomViewHolder(View itemView)
		{
			super(itemView);

			itemView.setOnClickListener(this);

			this.thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail);
		}

		@Override
		public void onClick(View view)
		{
			int position = getAdapterPosition();

			mOnClickListener.onListItemClick(position);
		}
	}
}
