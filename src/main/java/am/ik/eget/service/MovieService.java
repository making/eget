package am.ik.eget.service;

import org.springframework.transaction.annotation.Transactional;

import am.ik.eget.entity.Movie;
import am.ik.eget.entity.Page;

public interface MovieService {
    @Transactional
    void registerMovie(Page page);
    
    @Transactional
    void downloadMovie(Movie movie);
}
