package ru.itmo.hls.showmanager.infrastructure.db

import org.springframework.stereotype.Repository
import ru.itmo.hls.showmanager.domain.model.Performance
import ru.itmo.hls.showmanager.domain.port.PerformanceRepository

@Repository
class PerformanceRepositoryAdapter(
    private val performanceJpaRepository: PerformanceJpaRepository
) : PerformanceRepository {

    override fun findById(id: Long): Performance? = performanceJpaRepository.findById(id).orElse(null)

    override fun save(performance: Performance): Performance = performanceJpaRepository.save(performance)
}
