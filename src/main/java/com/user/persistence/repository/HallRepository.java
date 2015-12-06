package com.user.persistence.repository;

import com.user.persistence.entity.Hall;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HallRepository extends MongoRepository<Hall, String> {
    Optional<Hall> findOneByUri(String uri);
}
