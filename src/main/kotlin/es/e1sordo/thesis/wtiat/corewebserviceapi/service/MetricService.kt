package es.e1sordo.thesis.wtiat.corewebserviceapi.service

import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.InfluxConnector
import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.TimestampMetric
import org.influxdb.dto.BatchPoints
import org.influxdb.dto.Point
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class MetricService(
    private val deviceService: DeviceService,
    private val influxConnector: InfluxConnector
) {

    private val log: Logger = LoggerFactory.getLogger(MetricService::class.java)

    val valueField = "value"

    fun storeInDataBase(batchMetrics: List<List<TimestampMetric>>, deviceId: String) {
        val device = deviceService.getById(deviceId)

        log.info("Load metrics for device ${device.name}. Total: ${batchMetrics.size}")
        log.debug("Load metrics for device \${device.name: $batchMetrics")

        val batchPoints = BatchPoints
            .database(device.name)
            .retentionPolicy(influxConnector.defaultRetentionPolicy)
            .build()

        for (singleMetricList in batchMetrics) {
            for ((index, metricTemplate) in device.metrics!!.withIndex()) {
                val timestampMetric = singleMetricList[index]

                val pointBuilder: Point.Builder = Point.measurement(metricTemplate.name)
                    .time(timestampMetric.timestamp, TimeUnit.MILLISECONDS)

                val metricValue = timestampMetric.value
                when (metricTemplate.jvmType) {
                    "boolean" -> pointBuilder.addField(valueField, metricValue as Boolean)
                    "int", "long" -> pointBuilder.addField(valueField, metricValue as Long)
                    "float", "double" -> pointBuilder.addField(valueField, metricValue as Double)
                    "short", "byte" -> pointBuilder.addField(valueField, metricValue as Number)
                    else -> pointBuilder.addField(valueField, metricValue.toString())
                }

                batchPoints.point(pointBuilder.build())
            }
        }

        influxConnector.influxDb.write(batchPoints)
    }
}
