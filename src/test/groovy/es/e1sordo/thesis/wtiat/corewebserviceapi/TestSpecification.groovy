package es.e1sordo.thesis.wtiat.corewebserviceapi

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.repository.AgentRepository
import es.e1sordo.thesis.wtiat.corewebserviceapi.repository.DeviceRepository
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.AgentService
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = [CoreWebServiceApplication.class]
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class TestSpecification extends Specification {

    @Autowired
    AgentService agentService

    @Autowired
    AgentRepository agentRepository

    @Autowired
    DeviceService deviceService

    @Autowired
    DeviceRepository deviceRepository

    @Autowired
    DevicePrototypes devicePrototypes

    def void setup() {
    }

    def void cleanup() {
    }
}
