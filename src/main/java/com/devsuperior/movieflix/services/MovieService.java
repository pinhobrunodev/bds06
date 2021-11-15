package com.devsuperior.movieflix.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private ReviewRepository reviewRepository;

	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findMoviesPaged(Long genreId, Pageable pageable) {
		List<Genre> genres = (genreId == 0) ? null : Arrays.asList(genreRepository.getOne(genreId));
		Page<Movie> page = repository.find(genres, pageable);
		return page.map(MovieCardDTO::new);
	}

	@Transactional(readOnly = true)
	public MovieDetailsDTO findById(Long id) {
		return repository.findById(id).map(MovieDetailsDTO::new)
				.orElseThrow(() -> new ResourceNotFoundException("Id not found"));
	}

	@Transactional(readOnly = true)
	public List<ReviewDTO> findReviewsByMovieId(Long id) {
		try {
			Movie movie = repository.getOne(id);
			List<ReviewDTO> reviews = reviewRepository.findReviewsByMovie(movie);
			return reviews;
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Entity not found");
		}
	}

}
