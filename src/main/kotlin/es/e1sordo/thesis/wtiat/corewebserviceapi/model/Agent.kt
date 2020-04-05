package es.e1sordo.thesis.wtiat.corewebserviceapi.model

import es.e1sordo.thesis.wtiat.corewebserviceapi.enum.AgentState
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Agent(
    @Id var id: String? = null,
    var registerDate: LocalDateTime? = null,
    var name: String? = null,
    var state: AgentState? = null,
    var lastResponseTime: LocalDateTime? = null,
    var ip: String? = null,
    var pid: Int? = null,
    @DBRef var assignedDevice: Device? = null,
    var assignedDate: LocalDateTime? = null
)
