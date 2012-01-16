package am.ik.eget.crawler;

import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import am.ik.eget.crawler.LinkNode.LinkType;
import am.ik.eget.service.PageService;
import am.ik.eget.util.Util;

public class MovieLinkCrawler {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MovieLinkCrawler.class);

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = Util.getApplicationContext();
        try {
            String channel = ctx.getBean("dmmChannel", String.class);
            String target = "/monthly/" + channel + "/";
            String rootUrl = "http://www.dmm.co.jp" + target;
            LinkNode root = new LinkNode(rootUrl, channel);
            LinkedList<LinkNode> opened = new LinkedList<LinkNode>();

            PageService closed = ctx.getBean(PageService.class);
            opened.add(root);
            int count = 0;
            closed.remove(root);
            while (!opened.isEmpty()) {
                if (count > 10000) {
                    break;
                }
                LinkNode page = opened.poll();
                if (!closed.contains(page)) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    LOGGER.debug("page={}", page);
                    try {
                        Document pageDoc = Jsoup.connect(page.getPath()).get();
                        Elements anchors = pageDoc.select("a[href^=/monthly/"
                                + channel + "/]");
                        for (Element e : anchors) {
                            String url = rootUrl
                                    + e.attr("href").replace(target, "");
                            LinkNode next = new LinkNode(url, channel);
                            opened.add(next);
                        }
                        closed.add(page);
                        if (page.getType() == LinkType.DETAIL) {
                            count++;
                        }
                    } catch (SocketTimeoutException e) {
                        LOGGER.warn("retry page=" + page, e);
                        TimeUnit.MILLISECONDS.sleep(1000);
                        opened.add(page);
                    } catch (Exception e) {
                        LOGGER.warn("cannot open page=" + page, e);
                    }
                }
            }

            System.out.println(count + " pages were added");
        } finally {
            ctx.close();
        }
    }
}
