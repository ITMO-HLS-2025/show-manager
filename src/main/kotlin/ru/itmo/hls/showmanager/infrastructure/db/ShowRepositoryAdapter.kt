package ru.itmo.hls.showmanager.infrastructure.db

import org.springframework.stereotype.Repository
import ru.itmo.hls.showmanager.domain.model.Show
import ru.itmo.hls.showmanager.domain.port.ShowRepository
import java.time.LocalDateTime

@Repository
class ShowRepositoryAdapter(
    private val showJpaRepository: ShowJpaRepository
) : ShowRepository {

    override fun findShowByIdFetch(id: Long): Show? = showJpaRepository.findShowByIdFetch(id)

    override fun findAllByShowTimeBetween(start: LocalDateTime, end: LocalDateTime): List<Show> =
        showJpaRepository.findAllByShowTimeBetween(start, end)

    override fun save(show: Show): Show = showJpaRepository.save(show)
}
