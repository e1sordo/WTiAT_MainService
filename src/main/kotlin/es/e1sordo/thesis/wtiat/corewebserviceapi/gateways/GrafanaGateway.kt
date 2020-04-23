package es.e1sordo.thesis.wtiat.corewebserviceapi.gateways

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.GrafanaConfiguration
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.DashboardResponse
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.DatasourceRequest
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.DatasourceResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "grafana-http-api",
    url = "\${grafana.api.url}",
    path = "\${grafana.api.path}",
    configuration = [GrafanaConfiguration::class]
)
interface GrafanaGateway {

    companion object {
        const val DATASOURCES_PATH = "/datasources"
        const val DASHBOARDS_PATH = "/dashboards"
    }

    @PostMapping(value = [DATASOURCES_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createDatasource(@RequestBody request: DatasourceRequest): DatasourceResponse

    @DeleteMapping(value = ["$DATASOURCES_PATH/name/{datasourceName}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun removeDatasource(@PathVariable datasourceName: String): DatasourceResponse

    @PostMapping(
        value = ["$DASHBOARDS_PATH/db"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createDashboard(@RequestBody request: String): DashboardResponse

    @DeleteMapping(value = ["$DASHBOARDS_PATH/uid/{uid}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun removeDashboard(@PathVariable uid: String)
}
