package es.e1sordo.thesis.wtiat.corewebserviceapi.service

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.GrafanaConnector
import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.InfluxConnector
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import es.e1sordo.thesis.wtiat.corewebserviceapi.repository.DeviceRepository
import es.e1sordo.thesis.wtiat.corewebserviceapi.util.getCurrentTime
import org.springframework.stereotype.Service

@Service
class DeviceService(
    private val agentService: AgentService,
    private val repository: DeviceRepository,
    private val influxConnector: InfluxConnector,
    private val grafanaConnector: GrafanaConnector
) {

    fun create(request: Device): Device {
        val device = Device()

        device.id = null
        device.name = request.name
        device.connectorName = request.connectorName
        device.registerDate = getCurrentTime()
        device.gatheringFrequencyInMillis = request.gatheringFrequencyInMillis
        device.batchSendingFrequencyInMillis = request.batchSendingFrequencyInMillis
        device.metrics = request.metrics
        device.connectionValues = request.connectionValues

        val name = device.name!!
        try {
            influxConnector.createDataBase(name)
            grafanaConnector.createDatasource(name)
            val dashboard = grafanaConnector.createDashboard(name, device.connectorName!!)

            device.dashboardUid = dashboard.uid
            device.dashboardUrl = dashboard.url

            return repository.save(device)
        } catch (e: Exception) {
            grafanaConnector.removeDatasource(name)
            influxConnector.dropDataBase(name)
            throw e
        }
    }

    fun getAll(): MutableList<Device> = repository.findAll()

    fun getAllFreeDevices(): List<Device> = repository.findAll().filterNot {
        agentService.getAllBusyAgents().map(Agent::assignedDevice).contains(it)
    }

    fun getById(id: String): Device = repository.findById(id).orElseThrow(::RuntimeException)

    fun getByName(name: String): Device = repository.findByName(name).orElseThrow(::RuntimeException)

    fun delete(id: String) {
        val device = getById(id)
        val name = device.name!!

        grafanaConnector.removeDashboard(device.dashboardUid!!)
        grafanaConnector.removeDatasource(name)
        influxConnector.dropDataBase(name)

        repository.deleteById(id)
    }
}