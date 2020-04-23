package es.e1sordo.thesis.wtiat.corewebserviceapi.configuration

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Data
@Validated
@ConstructorBinding
@ConfigurationProperties("grafana")
data class GrafanaCredentials(
    val username: @NotNull String? = null,
    val password: @NotNull String? = null
)
