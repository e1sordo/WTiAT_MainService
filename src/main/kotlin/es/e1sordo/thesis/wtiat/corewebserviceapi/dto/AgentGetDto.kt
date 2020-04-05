package es.e1sordo.thesis.wtiat.corewebserviceapi.dto

data class AgentGetDto(
    var id: String? = null,
    var name: String? = null,
    var registerDate: String? = null,
    var lastResponseTime: String? = null,
    var ip: String? = null,
    var pid: Int? = null,
    var assignedDeviceName: String? = null,
    var assignedDate: String? = null,
    var shouldTerminate: Boolean? = null
)
