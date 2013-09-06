package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import java.util.*;
import models.*;
import views.html.*;
import views.html.changes.*;

@Security.Authenticated(Secured.class)
public class Changes extends Controller {
	
	/**
	*	Add a new change through this method.
	*/
	public static Result add() {
		Change newChange= Change.create(
			form().bindFromRequest().get("summary"),
			form().bindFromRequest().get("description"), User.find.byId(request().username()), 
			form().bindFromRequest().get("system")
		);
		System.out.println("Using add");
		return redirect(routes.Changes.viewChanges());
	}
	
	public static Result viewChanges() {
		return ok(
			viewchanges.render(
				Change.find.all(),
				User.find.byId(request().username())
			)
		);
	}
	
	/**
	*	Defines a form wrapping the Change class
	*/
	final static Form<Change> newChangeForm = form(Change.class);
	
	/**
     * Display a blank form.
     */ 
    public static Result blank() {
        return ok(form.render(newChangeForm, User.find.byId(request().username())));
    }
	/**	
	public static Result submit() {
		Form<Change> filledForm = newChangeForm.bindFromRequest();
		//debug
				System.out.println(filledForm.get().id);
				System.out.println(filledForm.get().summary);
				System.out.println(filledForm.get().description);
				System.out.println(User.find.byId(request().username()));
				System.out.println(filledForm.get().system);
				System.out.println(filledForm.toString());
		
		if(filledForm.hasErrors()) {
				return badRequest(form.render(filledForm, User.find.byId(request().username())));
			} else {
				Change.create(filledForm.get().id, filledForm.get().summary,  filledForm.get().description, User.find.byId(request().username()),  filledForm.get().system);
				//Change created = filledForm.get();
				System.out.println("Using submit");
				return redirect(routes.Changes.viewChanges());
			}
	}*/
		
		
	/**
	*	Change the summary of a change through this method.
	*/
	public static Result changeSummary(Long change) {
		if (Secured.isInitiatorOf(change)) {
			return ok(
				Change.changeSummary(
					change,
					form().bindFromRequest().get("summary")
				)
			);
		} else {
			return forbidden();
		}
	}
	
	/**
	*	Delete a change through this method.
	*/
	public static Result delete(Long change) {
    if(Secured.isInitiatorOf(change)) {
			Change.find.ref(change).delete();
			return ok();
		} else {
			return forbidden();
		}
	}
	
}