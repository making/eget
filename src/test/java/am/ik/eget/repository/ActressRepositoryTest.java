package am.ik.eget.repository;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import am.ik.eget.entity.Actress;
import am.ik.eget.entity.Movie;

@ContextConfiguration(locations = "classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ActressRepositoryTest {
    @Inject
    ActressRepository actressRepository;
    @Inject
    MovieRepository movieRepository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Rollback(false)
    public void test() {
        Actress actress = new Actress();
        Movie movie = new Movie();
        
        actress.setId(100L);
        actress.setName("名無し");
        actress.setMovies(Arrays.asList(movie));
        actressRepository.save(actress);
        
        movie.setCid("xxx");
        movie.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        movie.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        movie.setTitle("タイトル");
        movie.setActresses(Arrays.asList(actress));
        movieRepository.save(movie);

        actressRepository.flush();
        movieRepository.flush();
    }

    @Test
    @Rollback(false)
    public void foo() throws Exception {
        Movie m = movieRepository.findOne("xxx");
        System.out.println(m);
        movieRepository.delete(m);
        actressRepository.delete(m.getActresses());
        movieRepository.flush();
        actressRepository.flush();
    }
}
