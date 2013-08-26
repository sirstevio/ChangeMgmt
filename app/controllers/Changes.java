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
			"New change", form().bindFromRequest().get("description"), request().username(),
			form().bindFromRequest().get("system")
			
		);
		return ok(item.render(newChange));
	}
	
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