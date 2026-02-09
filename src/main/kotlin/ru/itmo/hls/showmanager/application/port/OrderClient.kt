package ru.itmo.hls.showmanager.application.port

interface OrderClient {
    fun getOccupiedSeats(showId: Long): List<Long>
}
