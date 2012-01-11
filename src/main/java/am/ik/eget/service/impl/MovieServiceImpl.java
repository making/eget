package am.ik.eget.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import am.ik.eget.client.DmmClient;
import am.ik.eget.entity.Actress;
import am.ik.eget.entity.Movie;
import am.ik.eget.entity.Page;
import am.ik.eget.repository.ActressRepository;
import am.ik.eget.repository.MovieRepository;
import am.ik.eget.repository.PageRepository;
import am.ik.eget.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {
    @Inject
    protected PageRepository pageRepository;
    @Inject
    protected MovieRepository movieRepository;
    @Inject
    protected ActressRepository actressRepository;
    @Inject
    protected DmmClient dmmClient;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MovieServiceImpl.class);

    @Override
    public void registerMovie(Page page) {
        if (page.isExtracted()) {
            return;
        }
        String url = page.getUrl();
        List<Movie> movies = dmmClient.getMovies(url);
        LOGGER.debug("register {}", movies);
        if (!movies.isEmpty()) {
            List<Actress> actresses = movies.get(0).getActresses();
            actressRepository.save(actresses);
            movieRepository.save(movies);
            actressRepository.flush();
            movieRepository.flush();
        }
        page.setExtracted(true);
        pageRepository.saveAndFlush(page);
    }

}
