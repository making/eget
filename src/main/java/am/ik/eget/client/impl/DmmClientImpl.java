package am.ik.eget.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import am.ik.eget.client.DmmClient;
import am.ik.eget.entity.Actress;
import am.ik.eget.entity.Movie;
import am.ik.eget.exception.EgetException;
import am.ik.eget.util.Util;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DmmClientImpl implements DmmClient {
    public static final Pattern ACTRESS_URL_PAT = Pattern.compile(Pattern
            .quote("/article=actress/id=") + "([0-9]+)" + Pattern.quote("/"));
    public static final Pattern PART_PAT = Pattern.compile("part=([0-9]+)");

    public static final String SESSION_ID_KEY = "INT_SESID";

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DmmClientImpl.class);

    private String sessionId = "";
    private final AtomicBoolean downloding = new AtomicBoolean(false);

    @Value("${dmm.userid}")
    protected String userId;
    @Value("${dmm.password}")
    protected String password;
    @Value("${dmm.channel}")
    protected String channel;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        DmmClient client = Util.getApplicationContext()
                .getBean(DmmClient.class);
        System.out
                .println(client
                        .getMovies("http://www.dmm.co.jp/monthly/s1/-/detail/=/cid=onsd00449/"));
    }

    @Override
    public void generateSessionId() {
        LOGGER.info("login to DMM");
        try {
            Connection.Response res = Jsoup
                    .connect("https://www.dmm.co.jp/my/")
                    .data("login_id", userId).data("password", password)
                    .data("sava_password", "1").data("save_login_id", "1")
                    .data("act", "commit").method(Method.POST).execute();
            String sesId = res.cookie(SESSION_ID_KEY);
            LOGGER.info("sessionId={}", sesId);
            this.sessionId = sesId;
        } catch (SocketTimeoutException e) {
            LOGGER.warn("login failed", e);
            generateSessionIdRetry(3000);
        } catch (SSLHandshakeException e) {
            LOGGER.warn("login failed", e);
            generateSessionIdRetry(3000);
        } catch (IOException e) {
            throw new EgetException("failed to login", e);
        }
    }

    protected void generateSessionIdRetry(long waitMilliseconds) {
        LOGGER.debug("retry to login");
        try {
            TimeUnit.MILLISECONDS.sleep(waitMilliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        generateSessionId();
    }

    @Override
    public List<Movie> getMovies(String url) {
        List<Movie> movies = new ArrayList<Movie>();
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        Document doc = null;
        try {
            doc = Jsoup.connect(url).cookie(SESSION_ID_KEY, sessionId).get();
        } catch (IOException e) {
            throw new EgetException("failed to connect " + url, e);
        }
        // System.out.println(doc);
        // タイトル抽出
        String title = doc.getElementById("title").text().trim();
        String cid = null;
        List<Actress> actresses = new ArrayList<Actress>();

        Elements trs = doc.select("table.mg-b20 tr");

        for (Element tr : trs) {
            Elements tds = tr.select("td");
            // 動画情報抽出
            for (Element td : tds) {
                String text = td.text().trim();
                if ("品番：".equals(text)) {
                    cid = td.nextElementSibling().text().trim();
                } else if ("出演者：".equals(text)) {
                    Elements as = td.nextElementSibling().select("a");
                    for (Element a : as) {
                        String href = a.attr("href");
                        Matcher m = ACTRESS_URL_PAT.matcher(href);
                        if (m.find()) {
                            Long id = Long.valueOf(m.group(1));
                            String name = a.text().trim();
                            Actress actress = new Actress();
                            actress.setId(id);
                            actress.setName(name);
                            actresses.add(actress);
                        }
                    }
                }
            }
        }
        // URL抽出
        int rate = 1000;
        Elements as = doc
                .select("a[href~=/monthly/" + channel + "/-/proxy/=/]");
        for (Element a : as) {
            String href = a.attr("href");
            if (href.contains("/transfer_type=download/rate=" + rate + "/")) {
                // System.out.println(href);
                Movie movie = new Movie();
                movie.setTitle(title);
                movie.setActresses(actresses);
                movie.setCid(cid);
                movie.setUrl("http://www.dmm.co.jp" + href);
                movie.setCreatedAt(tm);
                movie.setUpdatedAt(tm);
                Matcher m = PART_PAT.matcher(href);
                if (m.find()) {
                    int part = Integer.parseInt(m.group(1));
                    movie.setPart(part);
                }
                movies.add(movie);
            }
        }

        return movies;
    }

    @Override
    public void download(String url, OutputStream output) {
        Cookie cookie = new Cookie(".dmm.co.jp", SESSION_ID_KEY, sessionId,
                "/", null, false);
        HttpClient client = new HttpClient();
        client.getState().addCookie(cookie);
        GetMethod method = new GetMethod(url);
        try {
            downloding.set(true);
            client.executeMethod(method);
            InputStream input = method.getResponseBodyAsStream();
            try {
                IOUtils.copyLarge(input, output);
                byte[] buffer = new byte[4096];
                int n = 0;
                while (downloding.get() && -1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                if (!downloding.get()) {
                    LOGGER.warn("interrupted to download " + url);
                }
            } finally {
                IOUtils.closeQuietly(input);
            }
        } catch (IOException e) {
            throw new EgetException("failed to download " + url, e);
        } finally {
            downloding.set(false);
            method.releaseConnection();
        }
    }

    @Override
    public void stopDownload() {
        downloding.set(false);
    }
}
