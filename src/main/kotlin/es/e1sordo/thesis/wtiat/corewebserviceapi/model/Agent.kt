package es.e1sordo.thesis.wtiat.corewebserviceapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Agent(
    @Id val id: String? = null,
    val registerDate: LocalDateTime? = null,
    var ip: String? = null,
    var pid: Long? = null,
    @DBRef var assignedDevice: Device? = null,
    var assignedDate: LocalDateTime? = null
)
