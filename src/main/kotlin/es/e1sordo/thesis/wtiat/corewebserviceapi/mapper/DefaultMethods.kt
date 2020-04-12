package es.e1sordo.thesis.wtiat.corewebserviceapi.mapper

import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Metric
import org.mapstruct.Mapper
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
open class DefaultMethods {

    @Named("getAccessListFromMetric")
    fun getAccessListFromMetric(metrics: List<Metric>?): List<List<String>>? {
        return metrics?.map { it.access }
    }

    @Named("getTypesListFromMetric")
    fun getTypesListFromMetric(metrics: List<Metric>?): List<String>? {
        return metrics?.map { it.jvmType }
    }
}
