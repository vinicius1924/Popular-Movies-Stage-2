package com.example.vinicius.popularmoviesstage2.DTO;

/**
 * Created by vinicius on 16/03/17.
 */

public class MovieVideoDTO
{
	private String key;

	public String getKey()
	{
		return key;
	}

	public String getVideoUrl()
	{
		return "https://www.youtube.com/watch?v=" + key;
	}
}
