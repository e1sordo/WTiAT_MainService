package es.e1sordo.thesis.wtiat.corewebserviceapi.configuration

import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Metric
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

@Validated
@ConstructorBinding
@ConfigurationProperties("application.devices")
data class DevicePrototypes(val dictionary: Map<String, DeviceScheme>)

data class DeviceScheme(
    val name: String,
    val manufacturer: String,
    val type: String,
    val connection: DeviceConnection,
    val read: DeviceRead,
    val defaultMetrics: List<Metric>
)

data class DeviceConnection(
    val protocol: String,
    val params: List<String>
)

data class DeviceRead(
    val params: List<String>
)
