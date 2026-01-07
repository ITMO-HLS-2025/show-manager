package ru.itmo.hls.showmanager.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "performance")
class Performance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var title: String,
    var description: String? = null,
    var durationMinutes: Int? = null,

    @OneToMany(mappedBy = "performance")
    var shows: MutableList<Show> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "theatre_performance",
        joinColumns = [JoinColumn(name = "performance_id")]
    )
    @Column(name = "theatre_id")
    var theatreIds: MutableSet<Long> = mutableSetOf()
) {
    constructor() : this(0, "", null, null)
}
