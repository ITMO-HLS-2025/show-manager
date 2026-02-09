package ru.itmo.hls.showmanager.infrastructure.db

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.hls.showmanager.domain.model.Performance

interface PerformanceJpaRepository : JpaRepository<Performance, Long>
