package ru.itmo.hls.showmanager.application.usecase

import org.springframework.stereotype.Service
import ru.itmo.hls.showmanager.domain.model.Performance
import ru.itmo.hls.showmanager.domain.port.PerformanceRepository

@Service
class PerformanceService(
    private val performanceRepository: PerformanceRepository
) {
    fun save(entity: Performance): Performance = performanceRepository.save(entity)

    fun findById(id: Long): Performance? = performanceRepository.findById(id)
}
