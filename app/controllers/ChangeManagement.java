package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import java.util.List;

import models.*;
import views.html.*;

@Security.Authenticated(Secured.class)
public class ChangeManagement extends Controller {
	
	/**
	*	View all changes at the build stage
	*/
	public static Result assignBuild() {
		Change.Status status = Change.Status.NEW;
		List<Change> list = Change.find.where().eq("status", status).eq("builder", null).findList();
	
		return ok(
			assignbuilder.render(
				Change.find.where().eq("status", status).eq("builder", null).findList(),
				User.find.byId(request().username()),
				form(Change.class)
			)
		);
	}
	
	public static Result assignBuilder(Long changeId) {
		System.out.println(form().bindFromRequest().get("builder"));
		String builder = form().bindFromRequest().get("builder");
		User newBuilder = User.find.where().eq("userid", builder).findUnique();
		System.out.println(newBuilder.name);
		Change change = Change.find.byId(changeId);
		System.out.println(change.summary);
		change.builder = newBuilder;
		change.nextStatus();
		change.save();
		System.out.println(change.builder.name);
		System.out.println("Using assignBuilder");
		return redirect(routes.ChangeManagement.assignBuild());
	}
}