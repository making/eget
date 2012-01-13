package am.ik.eget.service;

import org.springframework.transaction.annotation.Transactional;

public interface DownloadService {
    //@Transactional
    void startDownload();
}
