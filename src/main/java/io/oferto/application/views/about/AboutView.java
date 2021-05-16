package io.oferto.application.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import io.oferto.application.views.main.MainView;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@CssImport("./views/about/about-view.css")
public class AboutView extends HorizontalLayout {

    public AboutView() {
        addClassName("about-view");
        
        setSizeFull();
        setPadding(true);
        
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        
        add(new Text("Hello Stock Manager!"));
    }

}
