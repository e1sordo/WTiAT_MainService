package es.e1sordo.thesis.wtiat.corewebserviceapi.configuration

import org.influxdb.InfluxDB
import org.influxdb.InfluxDBException
import org.influxdb.InfluxDBFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class InfluxConnector {

    private val log: Logger = LoggerFactory.getLogger(InfluxConnector::class.java)

    val influxDb: InfluxDB = InfluxDBFactory.connect("http://localhost:8086")
    val defaultRetentionPolicy = "defaultPolicy"

    private val defaultDuration = "100d"
    private val defaultReplicationFactor = 1

    init {
        val response = influxDb.ping()
        if (response.version.equals("unknown", ignoreCase = true)) {
            log.error("Failed to establish connection with InfluxDB")
        }
    }

    fun createDataBase(name: String) {
        if (influxDb.databaseExists(name)) {
            throw InfluxDBException("A database with the same name has already been created")
        } else {
            influxDb.createDatabase(name)
            influxDb.createRetentionPolicy(
                defaultRetentionPolicy,
                name,
                defaultDuration,
                defaultReplicationFactor,
                true
            )
        }
    }

    fun dropDataBase(name: String) {
        if (influxDb.databaseExists(name)) {
            influxDb.deleteDatabase(name)
            influxDb.dropRetentionPolicy(defaultRetentionPolicy, name)
        }
    }
}
