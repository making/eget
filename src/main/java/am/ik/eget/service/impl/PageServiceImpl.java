package am.ik.eget.service.impl;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import am.ik.eget.crawler.LinkNode;
import am.ik.eget.entity.Page;
import am.ik.eget.repository.PageRepository;
import am.ik.eget.service.PageService;

@Service
public class PageServiceImpl implements PageService {
    @Inject
    protected PageRepository pageRepository;
    
    
    @Override
    public boolean contains(LinkNode link) {
        if (link == null) {
            return false;
        }
        
        Page page = link.toEntity();
        if (StringUtils.isEmpty(page.getUrl())) {
            return false;
        }
        return pageRepository.exists(page.getUrl());
    }

    @Override
    public void add(LinkNode link) {
        if (link == null) {
            return;
        }

        Page page = link.toEntity();
        if (StringUtils.isEmpty(page.getUrl())) {
            return;
        }
        
        pageRepository.save(page);
    }
    
    @Override
    public void remove(LinkNode link) {
        if (link == null) {
            return;
        }

        Page page = link.toEntity();
        if (StringUtils.isEmpty(page.getUrl())) {
            return;
        }
        pageRepository.delete(page);
    }
}
