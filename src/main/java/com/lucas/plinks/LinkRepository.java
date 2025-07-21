package com.lucas.plinks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    boolean existsBySlug(String slug);

    Optional<Link> findBySlug(String slug);
}
