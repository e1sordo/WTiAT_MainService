package es.e1sordo.thesis.wtiat.corewebserviceapi

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [DevicePrototypes::class])
class CoreWebServiceApplication

fun main(args: Array<String>) {
	runApplication<CoreWebServiceApplication>(*args)
}
