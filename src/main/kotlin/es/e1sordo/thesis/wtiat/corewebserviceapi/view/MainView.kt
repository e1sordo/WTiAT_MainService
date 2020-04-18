package es.e1sordo.thesis.wtiat.corewebserviceapi.view

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.theme.lumo.Lumo

@Route
class MainView : VerticalLayout() {

    private var linkAgents: RouterLink = RouterLink("Список агентов", AgentListView::class.java)
    private var linkDevices: RouterLink = RouterLink("Список устройств", DeviceListView::class.java)

    init {
        val toggleButton = Button("Переключить цветовую схему интерфейса",
            ComponentEventListener {
                val themeList = UI.getCurrent().element.themeList
                if (themeList.contains(Lumo.DARK)) {
                    themeList.remove(Lumo.DARK)
                } else {
                    themeList.add(Lumo.DARK)
                }
            }
        )

        add(
            H1("Главная страница"),
            toggleButton,
            linkAgents,
            linkDevices
        )
    }
}