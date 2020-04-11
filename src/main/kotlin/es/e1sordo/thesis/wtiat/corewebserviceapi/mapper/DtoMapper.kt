package es.e1sordo.thesis.wtiat.corewebserviceapi.mapper

import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.AgentGetDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.AgentPostDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.DeviceGetDto
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = [DefaultMethods::class]
)
interface DtoMapper {

    @Mappings(
        value = [
            Mapping(target = "shouldTerminate", expression = "java(source.getState().getShouldTerminate())"),
            Mapping(
                target = "assignedDeviceId",
                source = "assignedDevice",
                qualifiedByName = ["getAssignedDeviceIdIfExists"]
            )
        ]
    )
    fun toAgentGetDto(source: Agent): AgentGetDto

    fun fromAgentPostDto(source: AgentPostDto): Agent

    @Mapping(target = "metrics", qualifiedByName = ["getAccessListFromMetric"])
    fun toDeviceGetDto(source: Device): DeviceGetDto
}
