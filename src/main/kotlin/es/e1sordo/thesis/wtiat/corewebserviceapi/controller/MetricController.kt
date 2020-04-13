package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.TimestampMetric
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.MetricService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    value = ["/rest/metrics"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class MetricController(private val service: MetricService) {

    val log: Logger = LoggerFactory.getLogger(MetricController::class.java)

    @PostMapping("/load")
    fun load(
        @RequestBody metrics: List<TimestampMetric>,
        @RequestParam deviceId: String
    ) {
        log.info("Load metrics for device with ID $deviceId. Total: ${metrics.size}")
        log.debug("Load metrics for device with ID $deviceId: $metrics")
        service.storeInDataBase(metrics, deviceId)
    }
}
