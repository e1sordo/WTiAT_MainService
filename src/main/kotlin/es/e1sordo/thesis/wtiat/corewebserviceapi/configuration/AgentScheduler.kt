package es.e1sordo.thesis.wtiat.corewebserviceapi.configuration

import es.e1sordo.thesis.wtiat.corewebserviceapi.enum.AgentState.*
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.AgentService
import es.e1sordo.thesis.wtiat.corewebserviceapi.util.howLongAgoItWasInMillis
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Slf4j
@Configuration
@EnableScheduling
class AgentScheduler(private val service: AgentService) {

    @Scheduled(fixedRate = 1 * 60 * 1000) // every minute
    fun scheduleFixedRateTask() {
        for (agent in service.getAll()) {
            val lastResponseTime = if (agent.lastResponseTime != null) agent.lastResponseTime else agent.registerDate
            if (agent.state !in arrayOf(
                    TO_TERMINATE,
                    TERMINATED
                ) && lastResponseTime!!.howLongAgoItWasInMillis() > 1 * 60 * 1000
            ) {
                agent.state = NOT_RESPONDED
                service.update(agent)
            }
        }
    }
}
