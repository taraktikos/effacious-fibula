package com.user.service;

import com.user.persistence.entity.Hall;
import com.user.persistence.repository.HallRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class HallService {

    @Value("${upload.path}")
    String uploadPath;
    @Autowired
    HallRepository hallRepository;

    public Hall save(Hall hall) {
        return hallRepository.save(hall);
    }

    public Page<Hall> findAll(Pageable pageable) {
        return hallRepository.findAll(pageable);
    }

    public Optional<Hall> findOne(String id) {
        return Optional.ofNullable(hallRepository.findOne(id));
    }

    public void delete(Hall hall) {
        hallRepository.delete(hall);
        log.debug("Hall {} was deleted", hall.getId());
    }

}
