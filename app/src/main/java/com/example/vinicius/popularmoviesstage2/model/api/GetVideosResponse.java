package com.example.vinicius.popularmoviesstage2.model.api;

import com.example.vinicius.popularmoviesstage2.DTO.MovieVideoDTO;

import java.util.List;

/**
 * Created by vinicius on 13/03/17.
 */

public class GetVideosResponse
{
	private List<MovieVideoDTO> results;

	public List<MovieVideoDTO> getData()
	{
		return results;
	}
}
