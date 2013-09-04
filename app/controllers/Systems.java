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
		return TODO;
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
    public static Result submit() {
        Form<ICTSystem> filledForm = newSystemForm.bindFromRequest();                
        System.out.println(filledForm.toString());
		
		/**
        // Check if the username is valid
        if(!filledForm.hasErrors()) {
            if(filledForm.get().username.equals("admin") || filledForm.get().username.equals("guest")) {
                filledForm.reject("username", "This username is already taken");
            }
			}
		*/
			if(filledForm.hasErrors()) {
				return badRequest(form.render(filledForm, User.find.byId(request().username())));
			} else {
				ICTSystem.create(filledForm.get().name, filledForm.get().id);
				return redirect(routes.Systems.viewSystems());
			}
        }
	
	/**
	*	View all systems
	*/
	public static Result viewSystems() {
		//return TODO;
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
	public static Result delete(Long change) {
		return TODO;
    }
	
	
}