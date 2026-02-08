package ru.itmo.hls.showmanager.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.itmo.hls.showmanager.entity.Show
import java.time.LocalDateTime

interface ShowRepository : JpaRepository<Show, Long> {
    @Query(
        """
        select s from Show s
        join fetch s.performance p
        where s.id = :id
        """
    )
    fun findShowByIdFetch(@Param("id") id: Long): Show?

    @Query(
        """
        select distinct s from Show s
        join fetch s.performance p
        where s.showTime between :start and :end
        """
    )
    fun findAllByShowTimeBetween(
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Show>

    @EntityGraph(attributePaths = ["performance"])
    fun findAllByShowTimeBetween(
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<Show>

    @Query(
        """
        select s from Show s
        where s.hallId = :theatreId
          and s.showTime between :start and :end
        """
    )
    fun findAllByTheatreId(
        @Param("theatreId") theatreId: Long,
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime
    ): List<Show>
}
