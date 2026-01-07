package ru.itmo.hls.showmanager.service

import org.springframework.stereotype.Service
import ru.itmo.hls.showmanager.entity.Performance
import ru.itmo.hls.showmanager.repository.PerformanceRepository

@Service
class PerformanceService(
    private val performanceRepository: PerformanceRepository
) {
    fun save(entity: Performance): Performance = performanceRepository.save(entity)

    fun findById(id: Long): Performance? = performanceRepository.findById(id).orElse(null)
}
