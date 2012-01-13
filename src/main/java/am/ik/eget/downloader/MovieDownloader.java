package am.ik.eget.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import am.ik.eget.service.DownloadService;
import am.ik.eget.util.Util;

public class MovieDownloader {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MovieDownloader.class);

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = Util.getApplicationContext();
        DownloadService downloadService = ctx.getBean(DownloadService.class);
        try {
            LOGGER.info("start donwloader");
            downloadService.startDownload();
        } finally {
            ctx.close();
        }

    }

}
