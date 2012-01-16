package am.ik.eget.client;

import java.io.OutputStream;
import java.util.List;
import am.ik.eget.entity.Movie;

public interface DmmClient {
    void generateSessionId();

    void download(String url, OutputStream output);
    
    void stopDownload();

    List<Movie> getMovies(String url);
}
