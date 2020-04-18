package es.e1sordo.thesis.wtiat.corewebserviceapi.view

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.NativeButton
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.function.SerializableFunction
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import es.e1sordo.thesis.wtiat.corewebserviceapi.configuration.DevicePrototypes
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.DeviceService
import es.e1sordo.thesis.wtiat.corewebserviceapi.util.howLongAgoItWasBeauty
import kotlin.random.Random


@Route("devices")
class DeviceListView(
    private val service: DeviceService,
    private val prototypes: DevicePrototypes
) : AppLayout() {

    private var layout: VerticalLayout = VerticalLayout()
    private var grid: Grid<Device> = Grid<Device>()
    var linkHome: RouterLink = RouterLink("Вернуться на главную", MainView::class.java)

    init {
        addToNavbar(H1("Список существующих устройств"))

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
                val allMetrics = device.metrics
                val size = allMetrics?.size ?: 0
                sp.text = size.toString()
                sp
            })
        ).setHeader("Метрик").isAutoWidth = true

        grid.addColumn(Device::gatheringFrequencyInMillis).setHeader("Сбор каждые (мс)").isAutoWidth = true

        grid.addColumn(Device::batchSendingFrequencyInMillis).setHeader("Отправка каждые (мс)").isAutoWidth = true

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
            service.create(
                Device(
                    name = "sdsdasd",
                    connectionValues = arrayListOf("192.168.1.100", "5050", "0", "2"),
                    connectorName = prototypes.dictionary.entries.elementAt(Random.nextInt((prototypes.dictionary.size))).key
                )
            )
            grid.setItems(service.getAll())
            grid.dataProvider.refreshAll()
        }

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES)
        grid.setSizeFull()

        layout.setSizeFull()
        layout.add(
            linkHome,
            newButton,
            reloadButton,
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
