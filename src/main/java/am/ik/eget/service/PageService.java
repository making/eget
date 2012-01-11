package am.ik.eget.service;

import org.springframework.transaction.annotation.Transactional;

import am.ik.eget.crawler.LinkNode;

public interface PageService {
    @Transactional(readOnly = true)
    boolean contains(LinkNode link);
    
    @Transactional
    void add(LinkNode link);
    

    @Transactional
    void remove(LinkNode link);
}
