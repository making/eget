package am.ik.eget.downloader;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileWatcher extends Thread {
    protected final File targetFile;
    protected final Thread parent;
    private long waitIntervalSecond = 60;
    private int stopThreshold = 1;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileWatcher.class);

    public FileWatcher(File targetFile, Thread parent) {
        this.targetFile = targetFile;
        this.parent = parent;
    }

    @Override
    public void run() {
        LOGGER.debug("start watching {}", targetFile);
        long lastLength = targetFile.length();
        long t1 = System.currentTimeMillis();
        int zeroCount = 0;
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(waitIntervalSecond);
            } catch (InterruptedException e) {
                break;
            }
            long length = targetFile.length();
            long t2 = System.currentTimeMillis();
            long speed = (length - lastLength) / (t2 - t1);
            LOGGER.debug("{} [MB] {} [KB/s]", length / 1000000, speed);

            if (length == lastLength) {
                zeroCount++;
                if (zeroCount >= stopThreshold) {
                    parent.interrupt();
                    break;
                }
            } else {
                zeroCount = 0;
            }
            lastLength = length;
            t1 = t2;
        }
        LOGGER.debug("stop watching {}", targetFile);
    }

    public long getWaitIntervalSecond() {
        return waitIntervalSecond;
    }

    public void setWaitIntervalSecond(long waitIntervalSecond) {
        this.waitIntervalSecond = waitIntervalSecond;
    }

    public int getStopThreshold() {
        return stopThreshold;
    }

    public void setStopThreshold(int stopThreshold) {
        this.stopThreshold = stopThreshold;
    }
}
