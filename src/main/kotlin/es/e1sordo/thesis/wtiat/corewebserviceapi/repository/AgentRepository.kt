package es.e1sordo.thesis.wtiat.corewebserviceapi.repository

import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface AgentRepository : MongoRepository<Agent, String> {
    fun findAllByAssignedDeviceNull(): MutableList<Agent>

    fun findAgentByAssignedDevice(device: Device): Optional<Agent>
}