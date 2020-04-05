package es.e1sordo.thesis.wtiat.corewebserviceapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Device(
    @Id val id: String? = null

)