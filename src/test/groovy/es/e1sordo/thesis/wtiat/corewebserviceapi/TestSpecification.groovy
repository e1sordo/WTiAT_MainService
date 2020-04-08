package es.e1sordo.thesis.wtiat.corewebserviceapi

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.repository.DeviceRepository
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
    DeviceRepository deviceRepository

    @Autowired
    DevicePrototypes devicePrototypes

    def void setup() {
    }

    def void cleanup() {
    }
}
