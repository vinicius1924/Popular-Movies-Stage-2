package com.example.vinicius.popularmoviesstage2.model.api;


import com.example.vinicius.popularmoviesstage2.DTO.MovieDTO;

import java.util.List;

/**
 * Created by vinicius on 12/03/17.
 */

public class GetMoviesResponse
{
	private List<MovieDTO> results;

	public List<MovieDTO> getData()
	{
		return results;
	}
}
