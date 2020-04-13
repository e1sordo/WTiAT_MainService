package es.e1sordo.thesis.wtiat.corewebserviceapi.repository

import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface DeviceRepository : MongoRepository<Device, String> {
    fun findByName(name: String): Optional<Device>
}