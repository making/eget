package am.ik.eget.repository;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import am.ik.eget.crawler.LinkNode.LinkType;
import am.ik.eget.entity.Page;
import am.ik.eget.service.MovieService;

@ContextConfiguration(locations = "classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PageRepositoryTest {
    @Inject
    PageRepository pageRepository;
    @Inject
    MovieService movieService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        int page = 0;
        int size = 10;
        int count = 0;
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
                movieService.registerMovie(p);
                if (count++ > 3) {
                    return;
                }
            }
            
            if (ret.getNumberOfElements() != size) {
                break;
            }
        }
    }

}
