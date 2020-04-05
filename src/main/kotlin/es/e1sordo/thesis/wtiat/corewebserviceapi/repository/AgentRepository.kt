package es.e1sordo.thesis.wtiat.corewebserviceapi.repository

import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import org.springframework.data.mongodb.repository.MongoRepository

interface AgentRepository: MongoRepository<Agent, String>