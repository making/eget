package am.ik.eget.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import am.ik.eget.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, String> {

}
