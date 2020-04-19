package es.e1sordo.thesis.wtiat.corewebserviceapi.view

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.DeviceService

@Route("manageDevice")
class ManageDeviceView(
    private val service: DeviceService
) : AppLayout(), HasUrlParameter<String> {

    var id2: String
    var mainForm: FormLayout
    var name: TextField
    var deviceId: TextField

    override fun setParameter(beforeEvent: BeforeEvent, contactId: String) {
        id2 = contactId
        if (id2 != "0") {
            val device = service.getById(id2)
            addToNavbar(H3("Редактирование устройства ${device.name}"))
        }
        fillForm()
    }

    private fun fillForm() {
        if (id2 != "0") {
            val device = service.getById(id2)
            name.value = device.name
            deviceId.value = device.id
        }
    }

    init {
        //Создаем объекты для формы
        mainForm = FormLayout()
        id2 = ""
        name = TextField("Название")
        deviceId = TextField("ID")

        //Добавим все элементы на форму
        mainForm.add(
            name,
            deviceId
        )
        content = mainForm
    }
}
