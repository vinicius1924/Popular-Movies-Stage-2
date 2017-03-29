package com.example.vinicius.popularmoviesstage2.DTO;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by vinicius on 12/03/17.
 */

public class MovieDTO implements Parcelable, Cloneable
{
	private String poster_path;
	private String overview;
	private String release_date;
	private long id;
	private String original_title;
	private double vote_average;
	public static final String PARCELABLE_KEY = "movie";


	public MovieDTO()
	{
	}

	public MovieDTO(Parcel in)
	{
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in)
	{
		id = in.readLong();
		original_title = in.readString();
		release_date = in.readString();
		poster_path = in.readString();
		vote_average = in.readDouble();
		overview = in.readString();
	}

	public String getPoster()
	{
		if(poster_path.startsWith("/data/data"))
		{
			return poster_path;
		}
		else
		{
			return "http://image.tmdb.org/t/p/w185/" + poster_path;
		}
	}

	public void setPosterPath(String poster_path)
	{
		this.poster_path = poster_path;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getOverview()
	{
		return overview;
	}

	public void setOverview(String overview)
	{
		this.overview = overview;
	}

	public String getReleaseDate()
	{
		return release_date;
	}

	public void setReleaseDate(String release_date)
	{
		this.release_date = release_date;
	}

	public String getOriginalTitle()
	{
		return original_title;
	}

	public void setOriginalTitle(String original_title)
	{
		this.original_title = original_title;
	}

	public double getVoteAverage()
	{
		return vote_average;
	}

	public void setVoteAverage(double vote_average)
	{
		this.vote_average = vote_average;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeLong(id);
		parcel.writeString(original_title);
		parcel.writeString(release_date);
		parcel.writeString(poster_path);
		parcel.writeDouble(vote_average);
		parcel.writeString(overview);
	}

	public static final Creator<MovieDTO> CREATOR = new Creator<MovieDTO>()
	{
		public MovieDTO createFromParcel(Parcel in)
		{
			return new MovieDTO(in);
		}

		public MovieDTO[] newArray(int size)
		{
			return new MovieDTO[size];
		}
	};

	public MovieDTO clone()
	{
		MovieDTO clone = null;

		try
		{
			clone = (MovieDTO) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			Log.e(this.getClass().toString(), this.getClass().toString() + " does not implement Cloneable");
		}

		return clone;
	}
}
