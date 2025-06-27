package com.lucas.plinks;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkModel, Long> {
    boolean existsBySlug(String slug);
}
