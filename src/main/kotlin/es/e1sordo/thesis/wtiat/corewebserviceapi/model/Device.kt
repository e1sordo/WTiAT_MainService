package es.e1sordo.thesis.wtiat.corewebserviceapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Device(
    @Id var id: String? = null,
    var name: String? = null,
    var connectorName: String? = null,
    var registerDate: LocalDateTime? = null,
    var lastResponseTime: LocalDateTime? = null,
    var gatheringFrequencyInMillis: Int? = null,
    var batchSendingFrequencyInMillis: Int? = null,
    var connectionValues: List<String>? = null,
    var metrics: List<Metric>? = null,
    var dashboardUid: String? = null,
    var dashboardUrl: String? = null
)

data class Metric(
    var name: String? = null,
    var description: String? = null,
    var unit: String? = null,
    var access: List<String>,
    var jvmType: String
)
