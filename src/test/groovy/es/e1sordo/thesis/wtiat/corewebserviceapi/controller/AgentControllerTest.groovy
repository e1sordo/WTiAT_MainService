package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import es.e1sordo.thesis.wtiat.corewebserviceapi.TestSpecification
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.AgentPostDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import org.springframework.beans.factory.annotation.Autowired

class AgentControllerTest extends TestSpecification {

    @Autowired
    AgentController agentController

    def 'should return full agent dto if some device was assigned to it'() {
        given:
        def agent = agentService.create(new Agent(name: "Agent1"))
        def device = deviceService.create(new Device(
                name: 'Device1',
                connectorName: 'tcp-siemens-sinamics-g120',
                connectionValues: ['192.168.1.100', '5555', '0', '2']
        ))

        def agentPostDto = new AgentPostDto(id: agent.id, name: agent.name)

        and:
        agentService.assignDevice(agent.id, device)

        when:
        def response = agentController.exchange(agentPostDto)

        then:
        response.assignedDevice != null
    }
}
