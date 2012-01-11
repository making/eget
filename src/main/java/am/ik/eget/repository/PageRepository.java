package am.ik.eget.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import am.ik.eget.entity.Page;

public interface PageRepository extends JpaRepository<Page, String> {
    org.springframework.data.domain.Page<Page> findByType(String type, Pageable pageable);
}
