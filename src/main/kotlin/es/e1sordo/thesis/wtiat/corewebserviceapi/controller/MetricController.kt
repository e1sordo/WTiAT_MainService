package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.TimestampMetric
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.MetricService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    value = ["/rest/metrics"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class MetricController(private val service: MetricService) {

    @PostMapping("/load")
    fun load(
        @RequestBody metrics: List<List<TimestampMetric>>,
        @RequestParam deviceId: String
    ) {
        service.storeInDataBase(metrics, deviceId)
    }
}
