package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository repository;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private AuthService service;

	@Transactional
	public ReviewDTO save(ReviewDTO dto) {

		Review review = new Review();
		review.setText(dto.getText());
		review.setMovie(movieRepository.getOne(dto.getMovieId()));
		review.setUser(service.authenticated());
		repository.save(review);
		return new ReviewDTO(review);
	}
}
