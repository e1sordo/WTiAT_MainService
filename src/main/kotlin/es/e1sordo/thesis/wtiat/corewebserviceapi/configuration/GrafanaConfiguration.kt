package es.e1sordo.thesis.wtiat.corewebserviceapi.configuration

import feign.auth.BasicAuthRequestInterceptor
import org.springframework.context.annotation.Bean

class GrafanaConfiguration {

    @Bean
    fun basicAuthRequestInterceptor(grafanaCredentials: GrafanaCredentials): BasicAuthRequestInterceptor? {
        return BasicAuthRequestInterceptor(grafanaCredentials.username, grafanaCredentials.password)
    }
}
