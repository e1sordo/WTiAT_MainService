package es.e1sordo.thesis.wtiat.corewebserviceapi.view

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DeviceScheme
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Metric
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.DeviceService

@Route("registerDevice")
class RegisterDeviceView(
    private val service: DeviceService,
    private val devicePrototypes: DevicePrototypes
) : VerticalLayout() {

    var mainForm: FormLayout
    var connectionForm: FormLayout
    var metricsForm: FormLayout
    var name: TextField
    var connector: ComboBox<String>
    var gatheringFrequencyInMillis: NumberField
    var batchSendingFrequencyInMillis: NumberField
    var saveButton: Button

    var connectionFields = mutableListOf<TextField>()
    var metricFields = mutableListOf<TextField>()

    val addMoreButton = Button("Добавить еще")
    val removeLastButton = Button("Удалить последнюю метрику")

    private var prototypes: Map<String, DeviceScheme> = devicePrototypes.dictionary

    init {
        connectionForm = FormLayout()
        metricsForm = FormLayout()

        //Создаем объекты для формы
        mainForm = FormLayout()
        mainForm.setResponsiveSteps(
            ResponsiveStep("25em", 1),
            ResponsiveStep("32em", 2),
            ResponsiveStep("40em", 3)
        )

        name = TextField()
        name.placeholder = "Название"

        gatheringFrequencyInMillis = NumberField()
        gatheringFrequencyInMillis.placeholder = "Частота сбора метрик (в мс)"

        batchSendingFrequencyInMillis = NumberField()
        batchSendingFrequencyInMillis.placeholder = "Частота отправления метрик на сервер (в мс)"

        connector = ComboBox<String>()
        connector.placeholder = "Connector name"
        connector.setItems(prototypes.keys)
        connector.isClearButtonVisible = true
        connector.addValueChangeListener {
            metricFields = mutableListOf()
            connectionForm.removeAll()
            metricsForm.removeAll()
            remove(addMoreButton)
            remove(removeLastButton)

            if (it.value != null) {
                val deviceScheme = prototypes[it.value]

                val connectionParams = deviceScheme!!.connection.params
                connectionForm.setResponsiveSteps(
                    ResponsiveStep("25em", 1),
                    ResponsiveStep("32em", 2),
                    ResponsiveStep("40em", connectionParams.size)
                )
                for (param in connectionParams) {
                    val field = TextField(param)
                    connectionFields.add(field)
                    connectionForm.add(field)
                }

                val defaultMetrics = deviceScheme.defaultMetrics
                metricsForm.setResponsiveSteps(
                    ResponsiveStep("25em", 1),
                    ResponsiveStep("32em", 2),
                    ResponsiveStep("40em", 5)
                )
                for (metric in defaultMetrics) {
                    val metricName = TextField("Название")
                    metricName.value = metric.name
                    metricFields.add(metricName)

                    val metricDescription = TextField("Описание")
                    metricDescription.value = metric.description
                    metricFields.add(metricDescription)

                    val metricUnit = TextField("Единица измерения")
                    metricUnit.value = metric.unit
                    metricFields.add(metricUnit)

                    val metricAccess = TextField("Доступ (${deviceScheme.read.params.joinToString()})")
                    metricAccess.value = metric.access.joinToString()
                    metricFields.add(metricAccess)

                    val metricJvmType = TextField("Тип переменной")
                    metricJvmType.value = metric.jvmType
                    metricFields.add(metricJvmType)
                }
                metricsForm.add(*metricFields.toTypedArray())
                addComponentAtIndex(4, addMoreButton)
                addMoreButton.addClickListener {
                    val candidates = arrayOf(
                        TextField("Название"),
                        TextField("Описание"),
                        TextField("Единица измерения"),
                        TextField("Доступ (${deviceScheme.read.params.joinToString()})"),
                        TextField("Тип переменной")
                    )
                    metricFields.addAll(candidates)
                    metricsForm.add(*candidates)
                    addComponentAtIndex(4, removeLastButton)
                }
                addComponentAtIndex(4, removeLastButton)
                removeLastButton.addClickListener {
                    val removedFields =
                        metricFields.subList(metricFields.size - defaultMetrics.size - 1, metricFields.size)
                    metricFields = metricFields.dropLast(removedFields.size).toMutableList()
                    metricsForm.remove(*removedFields.toTypedArray())
                    if (metricFields.size == 0)
                        remove(removeLastButton)
                }
            }
        }

        mainForm.setColspan(connector, 2)

        saveButton = Button("Сохранить")
        saveButton.addClickListener {
            //Создадим объект контакта получив значения с формы
            val device = Device()
            device.name = name.value
            device.connectorName = connector.value
            device.gatheringFrequencyInMillis = gatheringFrequencyInMillis.value.toInt()
            device.batchSendingFrequencyInMillis = batchSendingFrequencyInMillis.value.toInt()
            device.connectionValues = connectionFields.map { it.value }

            val deviceMetrics = mutableListOf<Metric>()
            val metricsNumber = metricFields.size - 1
            for (i in 0..metricsNumber step 5) {
                deviceMetrics.add(
                    Metric(
                        name = metricFields[i].value,
                        description = metricFields[i + 1].value,
                        unit = metricFields[i + 2].value,
                        access = metricFields[i + 3].value.split(", "),
                        jvmType = metricFields[i + 4].value
                    )
                )
            }

            device.metrics = deviceMetrics

            service.create(device)

            //Выведем уведомление пользователю и переведем его к списку контактов
            val notification = Notification("Устройство успешно зарегистрировано", 1700)
            notification.position = Notification.Position.MIDDLE
            notification.addDetachListener {
                UI.getCurrent().navigate(DeviceListView::class.java)
            }
            mainForm.isEnabled = false
            notification.open()
        }

        //Добавим все элементы на форму
        mainForm.add(
            name,
            gatheringFrequencyInMillis,
            batchSendingFrequencyInMillis,
            connector
        )
        add(
            H2("Регистрация нового устройства"),
            mainForm,
            connectionForm,
            metricsForm,
            saveButton
        )
    }
}
