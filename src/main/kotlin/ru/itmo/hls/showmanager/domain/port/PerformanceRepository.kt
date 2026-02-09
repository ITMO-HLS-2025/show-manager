package ru.itmo.hls.showmanager.domain.port

import ru.itmo.hls.showmanager.domain.model.Performance

interface PerformanceRepository {
    fun findById(id: Long): Performance?
    fun save(performance: Performance): Performance
}
