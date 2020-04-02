package es.e1sordo.thesis.wtiat.corewebserviceapi.controller

import es.e1sordo.thesis.wtiat.corewebserviceapi.dao.AgentDao
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/rest/agents"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class AgentController(private val agentDao: AgentDao) {

    @GetMapping
    fun getAll(): MutableList<Agent> = agentDao.findAll()

    @PostMapping
    fun register(): Agent = agentDao.save(Agent())
}