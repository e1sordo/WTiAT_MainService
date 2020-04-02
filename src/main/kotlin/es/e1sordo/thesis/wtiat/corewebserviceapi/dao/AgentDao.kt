package es.e1sordo.thesis.wtiat.corewebserviceapi.dao

import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import org.springframework.data.mongodb.repository.MongoRepository

interface AgentDao: MongoRepository<Agent, String>