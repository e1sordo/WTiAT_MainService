package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    value = ["/rest/metrics"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class MetricController {

    val log: Logger = LoggerFactory.getLogger(MetricController::class.java)

    @PostMapping("/load")
    fun load(@RequestBody stats: List<Map<String, String>>) = log.info("Load statistics: $stats")
}
