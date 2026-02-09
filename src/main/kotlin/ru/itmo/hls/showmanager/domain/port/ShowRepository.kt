package ru.itmo.hls.showmanager.domain.port

import ru.itmo.hls.showmanager.domain.model.Show
import java.time.LocalDateTime

interface ShowRepository {
    fun findShowByIdFetch(id: Long): Show?
    fun findAllByShowTimeBetween(start: LocalDateTime, end: LocalDateTime): List<Show>
    fun save(show: Show): Show
}
