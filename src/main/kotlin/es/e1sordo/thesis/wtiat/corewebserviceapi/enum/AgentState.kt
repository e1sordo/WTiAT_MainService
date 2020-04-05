package es.e1sordo.thesis.wtiat.corewebserviceapi.enum

enum class AgentState(val shouldTerminate: Boolean) {
    FREE(false), // агент запущен, периодически отлликается, но устройство не назначено
    NOT_RESPONDED(false), // в рамках установленного времени опроса не отвечал
    BUSY(false), // на агента назначено устройство
    TO_TERMINATE(true), // агент помечен на удаление, при очередном отклике агента будет получено поле shouldTerminate = true, и агент удалится
    TERMINATED(true); // процесс агента остановлен на целевой машине и сам агент может быть удален из базы
}