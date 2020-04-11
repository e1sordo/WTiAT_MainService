package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DeviceScheme
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.DeviceGetDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.mapper.DtoMapper
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.DeviceService
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/rest/devices"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class DeviceController(
    private val service: DeviceService,
    private val devicePrototypes: DevicePrototypes,
    private val dtoMapper: DtoMapper
) {

    val log: Logger = getLogger(DeviceController::class.java)

    @GetMapping
    fun getAll(): Map<String, DeviceScheme> = devicePrototypes.dictionary

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): DeviceGetDto = dtoMapper.toDeviceGetDto(service.getById(id))

    @GetMapping("/ping")
    fun ping() = log.info("Ping now!!")
}
