package io.oferto.application.views.login;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "login")
@PageTitle("Login | Vaadin Training")
@CssImport("./views/login/login-view.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	LoginForm loginForm = new LoginForm();	
	
	public LoginView() {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        
		loginForm.setForgotPasswordButtonVisible(false);	
		loginForm.setAction("login");

		add(new H1("Vaadin Stock Manager"), loginForm);
	}
		
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
        	loginForm.setError(true);
        }
    }
}
