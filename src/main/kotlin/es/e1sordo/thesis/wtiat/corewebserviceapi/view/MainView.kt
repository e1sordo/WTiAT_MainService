package es.e1sordo.thesis.wtiat.corewebserviceapi.view

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.editor.Editor
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.validator.StringLengthValidator
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.theme.lumo.Lumo
import es.e1sordo.thesis.wtiat.corewebserviceapi.model.Agent
import es.e1sordo.thesis.wtiat.corewebserviceapi.service.AgentService
import java.util.*

@Route
class MainView(
    private val service: AgentService
) : VerticalLayout() {

    private val grid = Grid(Agent::class.java)
    private val button: Button = Button("test")
    var linkAgents: RouterLink = RouterLink("Список агентов", AgentListView::class.java)
    var linkDevices: RouterLink = RouterLink("Список устройств", DeviceListView::class.java)

    init {

        val toggleButton = Button("Toggle dark theme",
            ComponentEventListener {
                val themeList = UI.getCurrent().element.themeList
                if (themeList.contains(Lumo.DARK)) {
                    themeList.remove(Lumo.DARK)
                } else {
                    themeList.add(Lumo.DARK)
                }
            }
        )


        val grid: Grid<Agent> = Grid<Agent>()
        val persons: List<Agent> = service.getAll()
        grid.setItems(persons)

        val firstNameColumn: Grid.Column<Agent> = grid.addColumn(Agent::name).setHeader("Agent Name")
        val ageColumn: Grid.Column<Agent> = grid.addColumn(Agent::ip).setHeader("Age")

        val binder: Binder<Agent> = Binder(Agent::class.java)
        val editor: Editor<Agent> = grid.editor
        editor.binder = binder
        editor.isBuffered = true

        val validationStatus = Div()
        validationStatus.setId("validation")

        val firstNameField = TextField()
        binder.forField(firstNameField)
            .withValidator(StringLengthValidator("First name length must be between 3 and 50.", 3, 50))
            .withStatusLabel(validationStatus).bind("name")
        firstNameColumn.editorComponent = firstNameField

        val ageField = TextField()
        binder.forField(ageField)
            .withStatusLabel(validationStatus).bind("ip")
        ageColumn.editorComponent = ageField

        val editButtons: MutableCollection<Button> = Collections.newSetFromMap(WeakHashMap())

        val editorColumn: Grid.Column<Agent> =
            grid.addComponentColumn { person: Agent? ->
                val edit = Button("Edit")
                edit.addClassName("edit")
                edit.addClickListener {
                    editor.editItem(person)
                    firstNameField.focus()
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

        val save = Button("Save",
            ComponentEventListener { editor.save() }
        )
        save.addClassName("save")

        val cancel = Button("Cancel",
            ComponentEventListener { editor.cancel() }
        )
        cancel.addClassName("cancel")

// Add a keypress listener that listens for an escape key up event.
// Note! some browsers return key as Escape and some as Esc

// Add a keypress listener that listens for an escape key up event.
// Note! some browsers return key as Escape and some as Esc
        grid.element.addEventListener("keyup") { editor.cancel() }
            .filter = "event.key === 'Escape' || event.key === 'Esc'"


        val buttons = Div(save, cancel)
        editorColumn.editorComponent = buttons




        add(
            toggleButton,
            linkAgents,
            linkDevices,
            grid,
            button
        )

        button.addClickListener {
            service.create(Agent())
            grid.dataProvider.refreshAll()
        }
//        listCustomers()
    }


    private fun listCustomers() {
        grid.setItems(service.getAll())
//        grid.
    }
}