package com.renldx.filmpal.server.repository;

import com.renldx.filmpal.server.model.UserMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, Integer> {

    Set<UserMovie> findAllByUserId(long userId);

}
