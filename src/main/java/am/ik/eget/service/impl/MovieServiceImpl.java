package am.ik.eget.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import am.ik.eget.client.DmmClient;
import am.ik.eget.downloader.FileDownloader;
import am.ik.eget.downloader.FileWatcher;
import am.ik.eget.entity.Actress;
import am.ik.eget.entity.Movie;
import am.ik.eget.entity.Page;
import am.ik.eget.exception.EgetException;
import am.ik.eget.repository.ActressRepository;
import am.ik.eget.repository.MovieRepository;
import am.ik.eget.repository.PageRepository;
import am.ik.eget.service.MovieService;
import am.ik.eget.util.Util;

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
    @Value("${dmm.channel}")
    protected String channel;

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

    @Override
    public void downloadMovie(final Movie movie) {
        if (!movie.isSaved()) {
            dmmClient.generateSessionId();
            final String url = movie.getUrl();
            try {
                final File targetFile = getTargetFile(movie);
                long start = System.currentTimeMillis();
                FileDownloader dl = new FileDownloader(url, targetFile, dmmClient,
                        Thread.currentThread());
                FileWatcher watch = new FileWatcher(targetFile,
                        Thread.currentThread());
                try {
                    LOGGER.debug("download {}", movie);
                    dl.start();
                    watch.start();
                    dl.join();
                } catch (InterruptedException e) {
                    LOGGER.warn("interupted!", e);
                    FileUtils.deleteQuietly(targetFile);
                    dl.stopDownload();
                    throw new EgetException(e);
                } finally {
                    watch.interrupt();
                }
                long end = System.currentTimeMillis();
                LOGGER.info("end to download. {} min",
                        TimeUnit.MILLISECONDS.toMinutes(end - start));
                movie.setSaved(true);
                movie.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                movieRepository.save(movie);
            } catch (Exception e) {
                throw Util.convertEgetException(e);
            }
        }
    }

    protected File getTargetFile(Movie movie) throws IOException {
        File baseDir = new File("/Volumes/WDC_HDD_2TB_01-2/dmm/" + channel);
        List<Actress> actresses = movie.getActresses();
        Actress actress = actresses.get(0);
        File targetDir = new File(baseDir, actress.getName());
        FileUtils.forceMkdir(targetDir);
        String fileName = movie.getCid();
        if (movie.getPart() > 0) {
            fileName = fileName + "_" + movie.getPart();
        }
        File targetFile = new File(targetDir, fileName + ".wmv");
        return targetFile;
    }
}
