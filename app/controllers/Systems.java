package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import java.util.*;
import models.*;
import views.html.*;
import views.html.systems.*;

@Security.Authenticated(Secured.class)
public class Systems extends Controller {
	
	/**
	*	Add a new system through this method.
	*/
	public static Result add() {
	System.out.println("Trying to add");
	System.out.println(form().bindFromRequest().get("name"));
	System.out.println(form().bindFromRequest().get("iao"));
	System.out.println(form().bindFromRequest().get("systemOwner"));
		ICTSystem newSystem= ICTSystem.create(
			form().bindFromRequest().get("name"),
			form().bindFromRequest().get("iao"),
			form().bindFromRequest().get("systemOwner")
		);
		System.out.println("Using add");
		return redirect(routes.Systems.viewSystems());
	}
	
	/**
	*	Defines a form wrapping the ICTSystem class
	*/
	final static Form<ICTSystem> newSystemForm = form(ICTSystem.class);
	
	/**
     * Display a blank form.
     */ 
    public static Result blank() {
        return ok(form.render(newSystemForm, User.find.byId(request().username())));
    }
	
	/**
     * Handle the form submission.
     */
    /**public static Result submit() {
        Form<ICTSystem> filledForm = newSystemForm.bindFromRequest();                
        System.out.println(filledForm.toString());
		
		/**
        // Check if the username is valid
        if(!filledForm.hasErrors()) {
            if(filledForm.get().username.equals("admin") || filledForm.get().username.equals("guest")) {
                filledForm.reject("username", "This username is already taken");
            }
			}
		
			if(filledForm.hasErrors()) {
				return badRequest(form.render(filledForm, User.find.byId(request().username())));
			} else {
				ICTSystem.create(filledForm.get().name, filledForm.get().iao, filledForm.get().systemOwner);
				return redirect(routes.Systems.viewSystems());
			}
        }*/
	
	/**
	*	View all systems
	*/
	public static Result viewSystems() {
		return ok(
			viewsystems.render(
				ICTSystem.find.all(),
				User.find.byId(request().username())
			)
		);
	}
	
	
	/**
	*	Delete a system through this method.
	*/
	public static Result delete(Long systemId) {
		return TODO;
    }
	
	
}