package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping(
    value = ["/rest/connectors"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
// Temporally solution. In a right way this gonna be a separate microservice that handle file loading
// For now all available connector jar files were placed in a resource folder
class ConnectorController {

    @GetMapping("/{connectorName}")
    @Throws(IOException::class)
    fun download(@PathVariable connectorName: String): ResponseEntity<Resource?>? {

        val nameWithExtension = if (connectorName.endsWith(".jar")) connectorName else "$connectorName.jar"
        val storageFolder = "connectors/"

        val file = ResourceUtils.getFile("classpath:$storageFolder$nameWithExtension")
        val path = Paths.get(file.absolutePath)
        val resource = ByteArrayResource(Files.readAllBytes(path))

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$nameWithExtension")

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource)
    }
}