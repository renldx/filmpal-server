package com.renldx.filmpal.repository;

import com.renldx.filmpal.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Optional<Movie> findByTitleAndRelease(String title, Date release);

}
