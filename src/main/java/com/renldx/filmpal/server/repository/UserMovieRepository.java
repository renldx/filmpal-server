package com.renldx.filmpal.server.repository;

import com.renldx.filmpal.server.model.UserMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, Integer> {

    List<UserMovie> findAllByUserId(long userId);

    @Query(value = "select x from UserMovie x where x.userId=:userId and x.movie.title=:title and x.movie.release=:release")
    Optional<UserMovie> findByTitleAndRelease(long userId, String title, Year release);

}
