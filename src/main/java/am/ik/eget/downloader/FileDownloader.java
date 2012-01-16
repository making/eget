package am.ik.eget.downloader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import am.ik.eget.client.DmmClient;

public class FileDownloader extends Thread {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileDownloader.class);

    private final String url;
    private final File targetFile;
    private final DmmClient dmmClient;
    private final Thread parent;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public FileDownloader(String url, File targetFile, DmmClient dmmClient,
            Thread parent) {
        this.url = url;
        this.targetFile = targetFile;
        this.dmmClient = dmmClient;
        this.parent = parent;
    }

    public void stopDownload() {
        dmmClient.stopDownload();
    }

    @Override
    public void run() {
        running.set(true);
        OutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(targetFile));
            LOGGER.info("start to download to {}",
                    targetFile.getCanonicalFile());
            dmmClient.download(url, output);
        } catch (Exception e) {
            LOGGER.warn("failed to downlod" + targetFile, e);
            IOUtils.closeQuietly(output);
            if (!FileUtils.deleteQuietly(targetFile)) {
                LOGGER.warn("could not delete " + targetFile);
            }
            parent.interrupt();
        } finally {
            IOUtils.closeQuietly(output);
        }
    }
}
