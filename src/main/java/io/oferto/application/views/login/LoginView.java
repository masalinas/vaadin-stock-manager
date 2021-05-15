package io.oferto.application.views.login;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

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
		
		/*loginForm.addLoginListener(event -> {
		    boolean isAuthenticated = authenticate(event);
		    
		    if (isAuthenticated) {
		    	getUI().ifPresent(ui ->
		           ui.navigate("dashboard"));
		    } else {
		    	loginForm.setError(true);
		    }
		});*/
		
		add(new H1("Vaadin Stock Manager"), loginForm);
	}
	
	/*private boolean authenticate(LoginEvent e) {
		return true;
	}*/
	
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
