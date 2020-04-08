package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DeviceScheme
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/rest/devices"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class DeviceController(
    private val devicePrototypes: DevicePrototypes
) {

    @GetMapping
    fun getAll(): Map<String, DeviceScheme> = devicePrototypes.dictionary
}