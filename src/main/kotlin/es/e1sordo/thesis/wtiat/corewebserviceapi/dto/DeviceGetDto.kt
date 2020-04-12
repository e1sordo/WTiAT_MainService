package es.e1sordo.thesis.wtiat.corewebserviceapi.dto

data class DeviceGetDto(
    var id: String? = null,
    var name: String? = null,
    var connectorName: String? = null,
    var gatheringFrequencyInMillis: Int? = null,
    var batchSendingFrequencyInMillis: Int? = null,
    var connectionValues: List<String>? = null,
    var metrics: List<List<String>>? = null,
    var metricTypes: List<String>? = null
)