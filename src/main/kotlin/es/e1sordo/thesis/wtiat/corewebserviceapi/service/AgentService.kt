package es.e1sordo.thesis.wtiat.corewebserviceapi.service

import es.e1sordo.thesis.wtiat.corewebserviceapi.enum.AgentState.*
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import es.e1sordo.thesis.wtiat.corewebserviceapi.repository.AgentRepository
import es.e1sordo.thesis.wtiat.corewebserviceapi.util.getCurrentTime
import org.springframework.stereotype.Service
import java.util.*

@Service
class AgentService(private val repository: AgentRepository) {

    fun create(agent: Agent): Agent {
        agent.id = null
        agent.registerDate = getCurrentTime()
        agent.lastResponseTime = agent.registerDate
        agent.state = FREE
        return repository.save(agent)
    }

    fun getAll(): MutableList<Agent> = repository.findAll()

    fun getAllFreeAgents(): MutableList<Agent> = repository.findAllByAssignedDeviceNull()

    fun getAllBusyAgents(): MutableList<Agent> = repository.findAllByAssignedDeviceNotNull()

    fun getById(id: String): Optional<Agent> = repository.findById(id)

    fun getByAssignedDevice(device: Device): Optional<Agent> = repository.findAgentByAssignedDevice(device)

    fun delete(id: String) = repository.deleteById(id)

    fun update(request: Agent, fromAgent: Boolean = false): Agent {
        val agent = getById(request.id!!).get()

        if (agent == request)
            return agent

        agent.name = request.name
        agent.ip = request.ip
        agent.pid = request.pid

        if (fromAgent) {
            agent.lastResponseTime = getCurrentTime()

            if (agent.state == TO_TERMINATE) {
                agent.state = TERMINATED
            } else {
                agent.state = if (agent.assignedDevice != null) BUSY else FREE
            }
        } else {
            agent.state = request.state
        }

        return repository.save(agent)
    }

    fun assignDevice(id: String, device: Device?) {
        val agent = getById(id).get()

        if (agent.assignedDevice != device) {
            agent.assignedDevice = device

            agent.assignedDate = if (agent.assignedDevice != null) getCurrentTime() else null

            repository.save(agent)
        }
    }

    fun terminate(id: String) {
        val agent = getById(id).get()
        agent.state = TO_TERMINATE
        repository.save(agent)
    }
}