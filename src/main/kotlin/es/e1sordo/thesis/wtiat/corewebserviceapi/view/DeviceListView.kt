package es.e1sordo.thesis.wtiat.corewebserviceapi.view

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.NativeButton
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.TemplateRenderer
import com.vaadin.flow.function.SerializableFunction
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.AgentService
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.DeviceService
import es.e1sordo.thesis.wtiat.corewebserviceapi.util.howLongAgoItWasBeauty
import java.text.NumberFormat

@Route("devices")
class DeviceListView(
    private val service: DeviceService,
    private val agentService: AgentService,
    private val prototypes: DevicePrototypes
) : AppLayout() {

    private var layout: VerticalLayout = VerticalLayout()
    private var grid: Grid<Device> = Grid<Device>()
    var linkHome: RouterLink = RouterLink("Вернуться на главную", MainView::class.java)

    init {
        //Выведем столбцы в нужном порядке

        grid.addColumn(
            ComponentRenderer(SerializableFunction { device: Device ->
                val sp = Span()
                val deviceId = device.id
                sp.text = deviceId
                sp.style.set("font-size", "0.8em")
                sp
            })
        ).setHeader("ID").isAutoWidth = true

        grid.addColumn(Device::name).setHeader("Название").isAutoWidth = true

        grid.addColumn(
            ComponentRenderer(SerializableFunction { device: Device ->
                val sp = Span()
                val connectorName = device.connectorName
                sp.text = connectorName
                sp.style.set("font-size", "0.8em")
                sp
            })
        ).setHeader("Коннектор").isAutoWidth = true

        grid.addColumn(
            ComponentRenderer(SerializableFunction { device: Device ->
                val sp = Span()
                val deviceAgent = agentService.getByAssignedDevice(device)
                if (deviceAgent.isPresent) {
                    sp.text = deviceAgent.get().name
                    sp.style.set("font-weight", "bold")
                } else {
                    sp.text = "Не назначен"
                    sp.style.set("color", "slategray")
                }
                sp
            })
        ).setHeader("Агент").isAutoWidth = true

        grid.addColumn(
            ComponentRenderer(SerializableFunction { device: Device ->
                val sp = Span()
                val allMetrics = device.metrics
                val size = allMetrics?.size ?: 0
                sp.text = size.toString()
                sp
            })
        ).setHeader("Метрик").isAutoWidth = true

        grid.addColumn(
            ComponentRenderer(SerializableFunction { device: Device ->
                val sp = Span()
                val numFormat = NumberFormat.getInstance()
                sp.text = numFormat.format(device.gatheringFrequencyInMillis)
                sp
            })
        ).setHeader("Сбор каждые (мс)").isAutoWidth = true

        grid.addColumn(
            ComponentRenderer(SerializableFunction { device: Device ->
                val sp = Span()
                val numFormat = NumberFormat.getInstance()
                sp.text = numFormat.format(device.batchSendingFrequencyInMillis)
                sp
            })
        ).setHeader("Отправка каждые (мс)").isAutoWidth = true

        grid.addColumn(
            ComponentRenderer(SerializableFunction { device: Device ->
                val a = Anchor()
                a.href = "http://localhost:3000/d/"
                a.setTarget("_blank")
                a.text = "Перейти"
                a
            })
        ).setHeader("Dashboard").isAutoWidth = true

        grid.addColumn { it.registerDate?.howLongAgoItWasBeauty() }.setHeader("Регистрация").isAutoWidth = true


        // TODO: for development purpose only!!!
        grid.addColumn(ComponentRenderer { item: Device ->
            buildButtonWithDialog(item, "del", service::delete)
        }).isAutoWidth = true

        grid.setItems(service.getAll())


        val reloadButton = Button("Обновить таблицу")
        reloadButton.addClickListener {
            grid.recalculateColumnWidths()
            grid.setItems(service.getAll())
            grid.recalculateColumnWidths()
        }


        val newButton = Button("Добавить новое устройство")
        newButton.addClickListener {
            UI.getCurrent().navigate(RegisterDeviceView::class.java)
        }


        grid.setItemDetailsRenderer(
            TemplateRenderer.of<Device>(
                """
<style>
.mdl-data-table {
    position: relative;
    border: 1px solid rgba(0,0,0,.12);
    border-collapse: collapse;
    white-space: nowrap;
    font-size: 13px;
}

.mdl-data-table thead {
    padding-bottom: 3px;
}

.mdl-data-table td:first-of-type, .mdl-data-table th:first-of-type {
    padding-left: 24px;
}

.mdl-data-table th {
    vertical-align: bottom;
    text-overflow: ellipsis;
    font-weight: 700;
    line-height: 24px;
    letter-spacing: 0;
    font-size: 12px;
    padding-bottom: 8px;
}

.mdl-data-table td, .mdl-data-table th {
    position: relative;
    height: 48px;
    box-sizing: border-box;
}

.mdl-data-table td, .mdl-data-table th {
    padding: 0 18px 12px;
    text-align: left;
}

.mdl-data-table td {
    border-top: 1px solid rgba(0,0,0,.12);
    border-bottom: 1px solid rgba(0,0,0,.12);
    padding-top: 12px;
    vertical-align: middle;
}
</style>

<br>
<table id="metrics-table" class="mdl-data-table" style="width:100%">
    <tbody inner-h-t-m-l="[[item.deviceInfo]]"></tbody>
</table>
<br>
<table id="metrics-table" class="mdl-data-table" style="width:100%">
    <tbody inner-h-t-m-l="[[item.connectionInfo]]"></tbody>
</table>
<br>
<table id="metrics-table" class="mdl-data-table" style="width:100%">
    <thead inner-h-t-m-l="[[item.metricHeaders]]"></thead>
    <tbody inner-h-t-m-l="[[item.metricValues]]"></tbody>
</table>
<br>
                        """
            )
                .withProperty("metricHeaders") {
                    val prototype = prototypes.dictionary[it.connectorName]
                    val readParams = prototype?.read?.params
                    var response =
                        "<tr><th>Метрика</th><th>Описание</th><th>Единица измерения</th><th>Тип переменной</th>"
                    if (!readParams.isNullOrEmpty())
                        for (param in readParams)
                            response += "<th>${param}</th>"
                    response
                }
                .withProperty("metricValues") {
                    val metrics = it.metrics!!
                    var response = ""
                    for (metric in metrics) {
                        response += "<tr>"
                        response += "<td>${metric.name}</td><td>${metric.description}</td><td>${metric.unit}</td>"
                        response += "<td>${metric.jvmType}</td>"
                        if (metric.access.isNotEmpty())
                            for (ma in metric.access)
                                response += "<td>${ma}</td>"
                        response += "</tr>"
                    }
                    response
                }
                .withProperty("connectionInfo") {
                    val prototype = prototypes.dictionary[it.connectorName]
                    val connParams = prototype?.connection
                    var response = "<th>Информация о подключении</th>"
                    response += "<td><b>Протокол</b>: ${connParams?.protocol}</td>"
                    if (!connParams?.params.isNullOrEmpty())
                        for ((index, param) in connParams!!.params.withIndex()) {
                            response += "<td><b>${param.toUpperCase()}</b>: ${it!!.connectionValues!![index]}</td>"
                        }
                    response
                }
                .withProperty("deviceInfo") {
                    val prototype = prototypes.dictionary[it.connectorName]
                    var response = "<th>Информация об устройстве</th>"
                    response += "<td><b>Производитель</b>: ${prototype?.manufacturer}</td>"
                    response += "<td><b>Название</b>: ${prototype?.name}</td>"
                    response += "<td><b>Тип</b>: ${prototype?.type}</td>"
                    response
                }
                .withEventHandler("handleClick") {
                    grid.dataProvider.refreshItem(it)
                })

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES)
        grid.setSizeFull()


        // Title block

        val horizontalLayoutManage = HorizontalLayout()
        horizontalLayoutManage.isPadding = true
        horizontalLayoutManage.add(
            reloadButton,
            newButton
        )

        layout.setSizeFull()
        layout.add(
            H2("Зарегистрированные устройства"),
            linkHome,
            horizontalLayoutManage,
            grid
        )
        content = layout
    }

    private fun buildButtonWithDialog(
        item: Device,
        actionName: String,
        actionReference: (String) -> Any
    ): NativeButton {
        return NativeButton(actionName.capitalize(),
            ComponentEventListener {
                val dialog = Dialog()
                val confirm = Button(actionName.capitalize())
                val cancel = Button("Отмена")
                dialog.add("Вы уверены что хотите $actionName устройство?")
                dialog.add(confirm)
                confirm.addClickListener {
                    item.id?.let(actionReference)
                    dialog.close()
                    val notification = Notification("Успешно", 1000)
                    notification.position = Notification.Position.MIDDLE
                    notification.open()
                    grid.setItems(service.getAll())
                }
                dialog.add(cancel)
                cancel.addClickListener { dialog.close() }
                dialog.open()
            }
        )
    }
}
