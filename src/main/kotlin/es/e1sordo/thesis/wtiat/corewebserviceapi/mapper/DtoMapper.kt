package es.e1sordo.thesis.wtiat.corewebserviceapi.mapper

import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.AgentGetDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.AgentPostDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface DtoMapper {

    @Mapping(target = "shouldTerminate", expression = "java(source.getState().getShouldTerminate())")
    fun toAgentGetDto(source: Agent): AgentGetDto

    fun fromAgentPostDto(source: AgentPostDto): Agent
}