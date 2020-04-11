package es.e1sordo.thesis.wtiat.corewebserviceapi.service

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import es.e1sordo.thesis.wtiat.corewebserviceapi.repository.DeviceRepository
import es.e1sordo.thesis.wtiat.corewebserviceapi.util.getCurrentTime
import org.springframework.stereotype.Service

@Service
class DeviceService(
    private val repository: DeviceRepository,
    private val prototypes: DevicePrototypes
) {

    fun create(request: Device): Device {
        val device = Device()

        device.id = null
        device.name = request.name
        device.connectorName = request.connectorName
        device.registerDate = getCurrentTime()
        device.gatheringFrequencyInMillis = 500
        device.batchSendingFrequencyInMillis = 5000
        device.connectionValues = request.connectionValues
        device.metrics = prototypes.dictionary[device.connectorName]?.defaultMetrics

        return repository.save(device)
    }

    fun getAll(): MutableList<Device> = repository.findAll()

    fun getById(id: String): Device = repository.findById(id).orElseThrow(::RuntimeException)

    fun delete(id: String) = repository.deleteById(id)
}