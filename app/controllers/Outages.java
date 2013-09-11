package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import java.util.*;
import models.*;
import views.html.*;
import views.html.outages.*;

@Security.Authenticated(Secured.class)
public class Outages extends Controller {
	
	/**
	*	Add a new outage through this method.
	*/
	public static Result add() {
	System.out.println(form().bindFromRequest().get("change"));
		Outage newOutage= Outage.create(
			form().bindFromRequest().get("length"),
			form().bindFromRequest().get("description"), form().bindFromRequest().get("system"),
			form().bindFromRequest().get("change")
		);
		System.out.println("Using add");
		return redirect(routes.Outages.viewOutages());
	}	
	
	public static Result addFromModal() {
		Outage newOutage= Outage.create(
			form().bindFromRequest().get("length"),
			form().bindFromRequest().get("description"), form().bindFromRequest().get("system"),
			form().bindFromRequest().get("change")
		);
		System.out.println("Using add");
		return redirect(routes.Outages.viewOutages());
	}	
	
	/**
	*	Defines a form wrapping the Outage class
	*/
	public final static Form<Outage> newOutageForm = form(Outage.class);
	
	/**
     * Display a blank form.
     */ 
    public static Result blank() {
        return ok(form.render(newOutageForm, User.find.byId(request().username())));
    }
	
	/**
     * Handle the form submission.
     *
    public static Result submit() {
        Form<Outage> filledForm = newOutageForm.bindFromRequest();                
        System.out.println(filledForm.toString());
		
		if(filledForm.hasErrors()) {
			return badRequest(form.render(filledForm, User.find.byId(request().username())));
		} else {
			Outage.create(filledForm.get().id, filledForm.get().length, filledForm.get().description, filledForm.get().system, filledForm.get().change);
			return redirect(routes.Outages.viewOutages());
		}
	}*/
	
	/**
	*	View all outages
	*/
	public static Result viewOutages() {
		//return TODO;
		return ok(
			viewoutages.render(
				Outage.find.all(),
				User.find.byId(request().username())
			)
		);
	}
	
	
	/**
	*	Delete an outage through this method.
	*/
	public static Result delete(Long outageId) {
		System.out.println(outageId);
		Outage.find.byId(outageId).delete();
		
		return ok();
    }
	
	
}