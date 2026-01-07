package ru.itmo.hls.showmanager.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.hls.showmanager.entity.Performance

interface PerformanceRepository : JpaRepository<Performance, Long>
