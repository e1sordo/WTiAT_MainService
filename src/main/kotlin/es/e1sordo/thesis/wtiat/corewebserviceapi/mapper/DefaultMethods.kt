package es.e1sordo.thesis.wtiat.corewebserviceapi.mapper

import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import org.mapstruct.Mapper
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
open class DefaultMethods {

    @Named("getAssignedDeviceNameIfExists")
    fun getAssignedDeviceNameIfExists(device: Device?): String? {
        return device?.name
    }
}