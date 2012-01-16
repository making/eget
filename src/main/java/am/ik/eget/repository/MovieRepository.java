package am.ik.eget.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import am.ik.eget.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, String> {
    @Query("SELECT m FROM Movie AS m LEFT JOIN m.actresses WHERE m.saved = ?1 ORDER BY m.createdAt DESC")
    Page<Movie> findBySaved(boolean saved, Pageable pageable);
}
