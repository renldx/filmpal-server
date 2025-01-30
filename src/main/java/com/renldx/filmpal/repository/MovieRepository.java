package com.renldx.filmpal.repository;

import com.renldx.filmpal.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findByTitleAndRelease(String title, Date release);

}
