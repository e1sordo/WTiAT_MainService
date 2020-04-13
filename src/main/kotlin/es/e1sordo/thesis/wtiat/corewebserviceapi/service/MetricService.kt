package es.e1sordo.thesis.wtiat.corewebserviceapi.service

import es.e1sordo.thesis.wtiat.corewebserviceapi.dto.TimestampMetric
import org.influxdb.dto.BatchPoints
import org.influxdb.dto.Point
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class MetricService(private val deviceService: DeviceService) {

    val retentionPolicy = "defaultPolicy"
    val metricsDataBase = "metrics"
    val measurementName = "devices"

    val deviceFieldName = "device"

    fun storeInDataBase(metrics: List<TimestampMetric>, deviceId: String) {
        val device = deviceService.getById(deviceId)

//        val influxDB = InfluxDBFactory.connect("http://localhost:8086")
//        val response = influxDB.ping()
//        if (response.version.equals("unknown", ignoreCase = true)) {
//            println("Error")
//            return
//        }
//        influxDB.createDatabase(metricsDataBase)
//        influxDB.createRetentionPolicy(retentionPolicy, metricsDataBase, "100d", 1, true)

        val batchPoints = BatchPoints
            .database(metricsDataBase)
            .retentionPolicy(retentionPolicy)
            .build()

        for (singleMetric in metrics) {
            val pointBuilder: Point.Builder = Point.measurement(measurementName)
                .time(singleMetric.timestamp, TimeUnit.MILLISECONDS)
                .tag(deviceFieldName, device.name)

            val metricList = singleMetric.metrics
            for ((index, metricTemplate) in device.metrics!!.withIndex()) {
                val metricName = metricTemplate.name
                val metricValue = metricList[index]
                when (metricTemplate.jvmType) {
                    "boolean" -> pointBuilder.addField(metricName, metricValue as Boolean)
                    "int", "long" -> pointBuilder.addField(metricName, metricValue as Long)
                    "float", "double" -> pointBuilder.addField(metricName, metricValue as Double)
                    "short", "byte" -> pointBuilder.addField(metricName, metricValue as Number)
                    else -> pointBuilder.addField(metricName, metricValue.toString())
                }
            }
            batchPoints.point(pointBuilder.build())
        }

        println(batchPoints)
    }
}