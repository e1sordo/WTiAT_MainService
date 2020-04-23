package es.e1sordo.thesis.wtiat.corewebserviceapi

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.GrafanaCredentials
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(value = [DevicePrototypes::class, GrafanaCredentials::class])
class CoreWebServiceApplication

fun main(args: Array<String>) {
	runApplication<CoreWebServiceApplication>(*args)
}
