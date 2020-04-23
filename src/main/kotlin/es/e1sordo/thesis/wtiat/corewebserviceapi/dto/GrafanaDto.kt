package es.e1sordo.thesis.wtiat.corewebserviceapi.dto

data class DatasourceResponse(
    val id: Int? = null,
    val message: String? = null,
    val name: String? = null
)

data class DatasourceRequest(
    val orgId: String? = null,
    val name: String? = null,
    val type: String? = null,
    val typeLogoUrl: String? = null,
    val access: String? = null,
    val url: String? = null,
    val password: String? = null,
    val user: String? = null,
    val database: String? = null,
    val basicAuth: Boolean? = null,
    val isDefault: Boolean? = null,
    val jsonData: Map<String, String>? = null,
    val readOnly: Boolean? = null
)

data class DashboardResponse(
    val id: String? = null,
    val slug: String? = null,
    val status: String? = null,
    val uid: String? = null,
    val url: String? = null,
    val version: Int? = null
)
