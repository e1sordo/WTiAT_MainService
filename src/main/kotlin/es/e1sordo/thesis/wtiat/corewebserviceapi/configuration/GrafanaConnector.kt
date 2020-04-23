package es.e1sordo.thesis.wtiat.corewebserviceapi.configuration

import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.DashboardResponse
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.DatasourceRequest
import es.e1sordo.thesis.wtiat.corewebserviceapi.gateways.GrafanaGateway
import org.apache.commons.lang3.text.StrSubstitutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors.joining

@Component
class GrafanaConnector(
    private val gateway: GrafanaGateway,
    private val devicePrototypes: DevicePrototypes
) {

    private val log: Logger = LoggerFactory.getLogger(GrafanaConnector::class.java)

    fun createDatasource(name: String) {
        gateway.createDatasource(
            DatasourceRequest(
                orgId = "1",
                name = name,
                type = "influxdb",
                typeLogoUrl = "public/app/plugins/datasource/influxdb/img/influxdb_logo.svg",
                access = "direct",
                url = "http://localhost:8086",
                password = "",
                user = "admin",
                database = name,
                basicAuth = false,
                isDefault = false,
                jsonData = mapOf("httpMode" to "GET"),
                readOnly = false
            )
        )
    }

    fun removeDatasource(name: String) {
        gateway.removeDatasource(name)
    }

    fun createDashboard(name: String, connectorName: String): DashboardResponse {
        val storageFolder = "dashboards/"

        val jsonBody = getResourceFileAsString("$storageFolder$connectorName.json")

        val values: MutableMap<String, String> = hashMapOf(
            "title" to name,
            "shortInfo" to buildDescription(connectorName),
            "datasource" to name
        )
        val sub = StrSubstitutor(values, "\${", "}")
        val formattedBody = sub.replace(jsonBody)

        return gateway.createDashboard(formattedBody)
    }

    fun removeDashboard(uid: String) {
        gateway.removeDashboard(uid)
    }

    private fun buildDescription(connectorName: String): String {
        val deviceScheme = devicePrototypes.dictionary[connectorName]
        return """\n# ${deviceScheme!!.name} \n\n _Производитель_: ${deviceScheme.manufacturer} \n\n _Тип устройства_: ${deviceScheme.type} \n\n\n"""
    }

    private fun getResourceFileAsString(fileName: String): String? {
        val inputStream = getResourceFileAsInputStream(fileName)
        return if (inputStream != null) {
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.lines().collect(joining(System.lineSeparator()))
        } else {
            null
        }
    }

    private fun getResourceFileAsInputStream(fileName: String): InputStream? {
        val classLoader = GrafanaConnector::class.java.classLoader
        return classLoader.getResourceAsStream(fileName)
    }
}
