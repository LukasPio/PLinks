package com.lucas.plinks.repository;

import com.lucas.plinks.domain.LinkModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<LinkModel, Long> {
    boolean existsBySlug(String slug);
    Optional<LinkModel> findBySlug(String slug);
}
