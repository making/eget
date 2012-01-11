package am.ik.eget.crawler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import am.ik.eget.crawler.LinkNode.LinkType;
import am.ik.eget.entity.Page;
import am.ik.eget.exception.EgetException;
import am.ik.eget.repository.PageRepository;
import am.ik.eget.service.MovieService;
import am.ik.eget.util.Util;

public class MovieExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieExtractor.class);
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = Util.getApplicationContext();
        try {
            PageRepository pageRepository = ctx.getBean(PageRepository.class);
            MovieService movieService = ctx.getBean(MovieService.class);
            int page = 0;
            int size = 200;
            while (true) {
                Pageable pageable = new PageRequest(page++, size);
                org.springframework.data.domain.Page<Page> ret = pageRepository
                        .findByType(LinkType.DETAIL.name(), pageable);
                List<Page> pages = ret.getContent();

                for (Page p : pages) {
                    if (p.getUrl().contains("ch_navi")) {
                        continue;
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                    try {
                        movieService.registerMovie(p);
                    } catch (EgetException e) {
                        LOGGER.warn("exception!", e);
                    }
                }

                if (ret.getNumberOfElements() != size) {
                    break;
                }
            }
        } finally {
            ctx.close();
        }

    }

}
