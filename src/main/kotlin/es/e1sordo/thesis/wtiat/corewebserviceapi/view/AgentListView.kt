package es.e1sordo.thesis.wtiat.corewebserviceapi.view

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.HtmlContainer
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.grid.editor.Editor
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.NativeButton
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.TemplateRenderer
import com.vaadin.flow.data.renderer.TextRenderer
import com.vaadin.flow.function.SerializableFunction
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import es.e1sordo.thesis.wtiat.corewebserviceapi.enum.AgentState.*
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Device
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.AgentService
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.DeviceService
import es.e1sordo.thesis.wtiat.corewebserviceapi.util.howLongAgoItWasBeauty
import java.util.*

@Route("agents")
@HtmlImport("frontend://styles/shared-styles.html")
class AgentListView(
    private val service: AgentService,
    private val deviceService: DeviceService
) : AppLayout() {

    private var layout: VerticalLayout = VerticalLayout()
    private var grid: Grid<Agent> = Grid<Agent>()
    var linkHome: RouterLink = RouterLink("Вернуться на главную", MainView::class.java)

    init {
        addToNavbar(H2("Список существующих агентов"))

        grid.setItems(service.getAll())

        //Выведем столбцы в нужном порядке

        grid.addColumn(
            ComponentRenderer(SerializableFunction { agent: Agent ->
                val sp = Span()
                val agentId = agent.id
                sp.text = agentId
                sp.style.set("font-size", "0.8em")
                sp
            })
        ).setHeader("ID").isAutoWidth = true

        grid.addColumn(Agent::name).setHeader("Название").isAutoWidth = true
        grid.addColumn { it.registerDate?.howLongAgoItWasBeauty() }.setHeader("Регистрация").isAutoWidth = true

        grid.addColumn(
            ComponentRenderer(SerializableFunction { agent: Agent ->
                val sp = Span()
                val agentState = agent.state
                sp.text = agentState.toString()
                when (agentState) {
                    NOT_RESPONDED -> sp.style.set("color", "red")
                    BUSY -> sp.style.set("font-weight", "bold")
                    TO_TERMINATE -> sp.style.set("color", "slategray")
                    TERMINATED -> {
                        sp.style.set("color", "slategray")
                        sp.style.set("font-style", "italic")
                    }
                    else -> Unit
                }
                sp
            })
        ).setHeader("Состояние").isAutoWidth = true



        grid.addColumn { it.lastResponseTime?.howLongAgoItWasBeauty() }.setHeader("Последний отклик").isAutoWidth = true
        grid.addColumn(Agent::ip).setHeader("IP").isAutoWidth = true
        grid.addColumn(Agent::pid).setHeader("PID").isAutoWidth = true
        val assignedDeviceColumn: Grid.Column<Agent> =
            grid.addColumn { it.assignedDevice?.name }.setHeader("Устройство")
        assignedDeviceColumn.isAutoWidth = true
        grid.addColumn { it.assignedDate?.howLongAgoItWasBeauty() }.setHeader("Время назначения").isAutoWidth = true

        val binder: Binder<Agent> = Binder(Agent::class.java)
        val editor: Editor<Agent> = grid.editor
        editor.binder = binder
        editor.isBuffered = true

        val assignedDeviceSelect = ComboBox<Device>()
        assignedDeviceSelect.setItems(deviceService.getAll())
        assignedDeviceSelect.setRenderer(TextRenderer(Device::name))
        assignedDeviceSelect.isClearButtonVisible = true
        assignedDeviceSelect.setItemLabelGenerator { it.name }
        binder.bind(assignedDeviceSelect, "assignedDevice")
        assignedDeviceColumn.editorComponent = assignedDeviceSelect

        val editButtons: MutableCollection<Button> = Collections.newSetFromMap(WeakHashMap())

        val editorColumn: Grid.Column<Agent> =
            grid.addComponentColumn { agent: Agent? ->
                val edit = Button(VaadinIcon.EDIT.create())
                edit.addClickListener {
                    editor.editItem(agent)
                    assignedDeviceSelect.value = agent?.assignedDevice
                    assignedDeviceSelect.focus()
                }
                edit.isEnabled = !editor.isOpen
                editButtons.add(edit)
                edit
            }

        editor.addOpenListener {
            editButtons.stream()
                .forEach { button: Button ->
                    button.isEnabled = !editor.isOpen
                }
        }
        editor.addCloseListener {
            editButtons.stream()
                .forEach { button: Button ->
                    button.isEnabled = !editor.isOpen
                }
        }

        val save = Button(VaadinIcon.CHECK_CIRCLE.create(),
            ComponentEventListener { editor.save() }
        )
        save.addClassName("save")

        val cancel = Button(VaadinIcon.CLOSE_CIRCLE.create(),
            ComponentEventListener { editor.cancel() }
        )
        cancel.addClassName("cancel")

        // Add a keypress listener that listens for an escape key up event.
        // Note! some browsers return key as Escape and some as Esc
        grid.element
            .addEventListener("keyup") { editor.cancel() }
            .filter = "event.key === 'Escape' || event.key === 'Esc'"


        val buttons = Div(save, cancel)
        editorColumn.editorComponent = buttons


        //Добавим кнопку удаления и остановки
        grid.addColumn(ComponentRenderer { item: Agent ->
            when (item.state) {
                TERMINATED -> buildButtonWithDialog(item, "удалить", service::delete)
                FREE, NOT_RESPONDED -> buildButtonWithDialog(item, "остановить", service::terminate)
                else -> HtmlContainer("empty")
            }
        }).isAutoWidth = true

        // TODO: for development purpose only!!!
        grid.addColumn(ComponentRenderer { item: Agent ->
            buildButtonWithDialog(item, "del", service::delete)
        }).isAutoWidth = true


        val reloadButton = Button("Обновить")
        reloadButton.addClickListener {
            grid.recalculateColumnWidths()
            grid.setItems(service.getAll())
            grid.recalculateColumnWidths()
        }

        editor.addSaveListener { event ->
            service.assignDevice(event.item.id!!, event.item.assignedDevice)

            UI.getCurrent().page.reload()
        }

        grid.setItemDetailsRenderer(
            TemplateRenderer.of<Agent>(
                "<div style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'>"
                        + "<div>Hi! My name is <b>[[item.name]]!</b></div>"
                        + "<div><img style='height: 80px; width: 80px;' src='[[item.image]]'/></div>"
                        + "</div>"
            )
                .withProperty("name", Agent::name)
                .withProperty("id", Agent::id)
                .withProperty("state", Agent::state)
                .withEventHandler("handleClick") {
                    grid.dataProvider.refreshItem(it)
                })

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES)
        grid.setSizeFull()

        layout.setSizeFull()
        layout.add(
            linkHome,
            reloadButton,
            grid
        )
        content = layout
    }

    private fun buildButtonWithDialog(item: Agent, actionName: String, actionReference: (String) -> Any): NativeButton {
        return NativeButton(actionName.capitalize(),
            ComponentEventListener {
                val dialog = Dialog()
                val confirm = Button(actionName.capitalize())
                val cancel = Button("Отмена")
                dialog.add("Вы уверены что хотите $actionName агента?")
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
