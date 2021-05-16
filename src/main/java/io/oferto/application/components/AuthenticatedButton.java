package io.oferto.application.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

import io.oferto.application.security.SecurityConfiguration;

public class AuthenticatedButton extends Button {
	public AuthenticatedButton() {
		super();
				
		if (!SecurityConfiguration.isAdmin())
        	this.setVisible(false);
    }

    /**
     * Creates a button with a text inside.
     *
     * @param text
     *            the text inside the button
     * @see #setText(String)
     */
    public AuthenticatedButton(String text) {
        super(text);
        
        if (!SecurityConfiguration.isAdmin())
        	this.setVisible(false);
    }

    /**
     * Creates a button with an icon inside.
     *
     * @param icon
     *            the icon inside the button
     * @see #setIcon(Component)
     */
    public AuthenticatedButton(Component icon) {
    	super(icon);
    	
    	if (!SecurityConfiguration.isAdmin())
        	this.setVisible(false);
    }

    /**
     * Creates a button with a text and an icon inside.
     * <p>
     * Use {@link #setIconAfterText(boolean)} to change the order of the text
     * and the icon.
     *
     * @param text
     *            the text inside the button
     * @param icon
     *            the icon inside the button
     * @see #setText(String)
     * @see #setIcon(Component)
     */
    public AuthenticatedButton(String text, Component icon) {
        super(text, icon);
        
        if (!SecurityConfiguration.isAdmin())
        	this.setVisible(false);
    }

    /**
     * Creates a button with a text and a listener for click events.
     *
     * @param text
     *            the text inside the button
     * @param clickListener
     *            the event listener for click events
     * @see #setText(String)
     * @see #addClickListener(ComponentEventListener)
     */
    public AuthenticatedButton(String text,
            ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, clickListener);
        
        if (!SecurityConfiguration.isAdmin())
        	this.setVisible(false);
    }

    /**
     * Creates a button with an icon and a listener for click events.
     *
     * @param icon
     *            the icon inside the button
     * @param clickListener
     *            the event listener for click events
     * @see #setIcon(Component)
     * @see #addClickListener(ComponentEventListener)
     */
    public AuthenticatedButton(Component icon,
            ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(icon, clickListener);
        
        if (!SecurityConfiguration.isAdmin())
        	this.setVisible(false);
    }

    /**
     * Create a button with a text, an icon and a listener for click events.
     *
     * @param text
     *            the text inside the button
     * @param icon
     *            the icon inside the button
     * @param clickListener
     *            the event listener for click events
     * @see #setText(String)
     * @see #setIcon(Component)
     * @see #addClickListener(ComponentEventListener)
     */
    public AuthenticatedButton(String text, Component icon,
            ComponentEventListener<ClickEvent<Button>> clickListener) {
        super(text, icon, clickListener);
        
        if (!SecurityConfiguration.isAdmin())
        	this.setVisible(false);
    }
}
