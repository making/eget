package am.ik.eget.service.impl;

import javax.inject.Inject;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import am.ik.eget.entity.Movie;
import am.ik.eget.repository.MovieRepository;
import am.ik.eget.service.DownloadService;
import am.ik.eget.service.MovieService;

@Service
public class DownloadServiceImpl implements DownloadService {
    @Inject
    protected MovieRepository movieRepository;
    @Inject
    protected MovieService movieService;

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DownloadServiceImpl.class);

    @Override
    public void startDownload() {
        PageRequest pageable = new PageRequest(0, 300);
        Page<Movie> page = movieRepository.findBySaved(false, pageable);
        for (Movie movie : page) {
            try {
                movieService.downloadMovie(movie);
            } catch (Exception e) {
                LOGGER.warn("failed to donwload " + movie, e);
            }
        }
    }

}
