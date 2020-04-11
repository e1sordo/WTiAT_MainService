package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.AgentGetDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.AgentPostDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.mapper.DtoMapper
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.AgentService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    value = ["/rest/agents"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class AgentController(
    private val service: AgentService,
    private val deviceService: AgentService,
    private val dtoMapper: DtoMapper
) {

    @GetMapping
    fun getAll(): List<AgentGetDto> = service.getAll().map(dtoMapper::toAgentGetDto)

    @PostMapping
    fun exchange(@RequestBody request: AgentPostDto): AgentGetDto {
        val agentRequest = dtoMapper.fromAgentPostDto(request)

        val agentId = agentRequest.id
        val createdAgent = if (agentId != null && (service.getById(agentId).isPresent)) {
            service.update(agentRequest, fromAgent = true)
        } else {
            service.create(agentRequest)
        }

        return dtoMapper.toAgentGetDto(createdAgent)
    }
}