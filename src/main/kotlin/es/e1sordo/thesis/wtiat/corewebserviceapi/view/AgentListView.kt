package es.e1sordo.thesis.wtiat.corewebserviceapi.view

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.HtmlContainer
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.NativeButton
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ClickableRenderer.ItemClickListener
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.NativeButtonRenderer
import com.vaadin.flow.data.renderer.TemplateRenderer
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import es.e1sordo.thesis.wtiat.corewebserviceapi.enum.AgentState
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.AgentService
import es.e1sordo.thesis.wtiat.corewebserviceapi.util.howLongAgoItWasBeauty

@Route("agents")
class AgentListView(private val service: AgentService) : AppLayout() {

    private var layout: VerticalLayout = VerticalLayout()
    private var grid: Grid<Agent> = Grid<Agent>()
    var linkHome: RouterLink = RouterLink("Вернуться на главную", MainView::class.java)

    init {
        addToNavbar(H2("Список существующих агентов"))

        //Выведем столбцы в нужном порядке
        grid.addColumn(Agent::id).setHeader("ID").isAutoWidth = true
        grid.addColumn(Agent::name).setHeader("Название").isAutoWidth = true
        grid.addColumn { it.registerDate?.howLongAgoItWasBeauty() }.setHeader("Регистрация").isAutoWidth = true
        grid.addColumn(Agent::state).setHeader("Состояние").isAutoWidth = true
        grid.addColumn { it.lastResponseTime?.howLongAgoItWasBeauty() }.setHeader("Последний отклик").isAutoWidth = true
        grid.addColumn(Agent::ip).setHeader("IP").isAutoWidth = true
        grid.addColumn(Agent::pid).setHeader("PID").isAutoWidth = true
        grid.addColumn(Agent::assignedDevice).setHeader("Устройство").isAutoWidth = true
        grid.addColumn(Agent::assignedDate).setHeader("Время назначения").isAutoWidth = true

        //Добавим кнопку удаления и редактирования
        grid.addColumn(
            NativeButtonRenderer("Изменить",
                ItemClickListener { agent: Agent ->
                    Unit
                    //                        UI.getCurrent().navigate(ManageAgentView::class.java, agent.id)
                }
            )
        ).isAutoWidth = true

        grid.addColumn(ComponentRenderer { item: Agent ->
            when (item.state) {
                AgentState.TERMINATED -> buildButtonWithDialog(item, "удалить", service::delete)
                AgentState.FREE, AgentState.NOT_RESPONDED -> buildButtonWithDialog(
                    item,
                    "остановить",
                    service::terminate
                )
                else -> HtmlContainer("empty")
            }
        }).isAutoWidth = true

        // TODO: for development purpose only!!!
        grid.addColumn(ComponentRenderer { item: Agent ->
            buildButtonWithDialog(item, "del", service::delete)
        }).isAutoWidth = true

        grid.setItems(service.getAll())


        val reloadButton = Button("Обновить")
        reloadButton.addClickListener {
            grid.recalculateColumnWidths()
            grid.setItems(service.getAll())
            grid.recalculateColumnWidths()
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
