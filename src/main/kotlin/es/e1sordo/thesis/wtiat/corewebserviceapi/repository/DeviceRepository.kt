package es.e1sordo.thesis.wtiat.corewebserviceapi.repository

import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import org.springframework.data.mongodb.repository.MongoRepository

interface DeviceRepository : MongoRepository<Device, String>