package es.e1sordo.thesis.wtiat.corewebserviceapi.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AgentPostDto(
    var id: String? = null,
    var name: String? = null,
    var ip: String? = null,
    var pid: Int? = null
)