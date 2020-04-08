package es.e1sordo.thesis.wtiat.corewebserviceapi.service

import es.e1sordo.thesis.wtiat.corewebserviceapi.TestSpecification
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import org.springframework.beans.factory.annotation.Autowired

class DeviceServiceTest extends TestSpecification {

    @Autowired
    DeviceService service

    def 'should create successfully'() {
        given:
        def deviceRequest = new Device(
                name: 'The Device',
                connectorName: 'tcp-siemens-sinamics-g120',
                connectionValues: ['192.168.1.100', '5555', '0', '2']
        )

        when:
        def response = service.create(deviceRequest)

        then:
        deviceRepository.count() == 1
        response.name == deviceRequest.name
        response.connectorName == deviceRequest.connectorName
        response.gatheringFrequencyInMillis == 500
        response.batchSendingFrequencyInMillis == 5000
        response.metrics.forEach { it.access.size() == response.connectionValues.size() }
    }
}