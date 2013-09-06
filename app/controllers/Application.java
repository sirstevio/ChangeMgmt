package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import java.util.List;

import models.*;
import views.html.*;

public class Application extends Controller {
  
	@Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(
			index.render(
				Change.find.all(),
				User.find.byId(request().username())
			)
		);
	}	
	
	@Security.Authenticated(Secured.class)
    public static Result outagesOverChanges() {
        return ok(
			outagesoverchanges.render(
				Change.find.all(),
				Outage.find.all(),
				User.find.byId(request().username())
			)
		);
	}	
	
	public static Result login() {
		return ok(
			login.render(form(Login.class))
		);
	}
	
	public static Result authenticate() {
		System.out.println("Entering Application.authenticate method");
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		System.out.println(loginForm.get());
		if (loginForm.hasErrors()) {
			System.out.println("hasErrors is true");
			return badRequest(login.render(loginForm));
		} 
		else {
			System.out.println("hasErrors is false");
			session().clear();
			session("email", loginForm.get().email);
			return redirect(
				routes.Application.index()
			);
		}
		
	}
	
	public static Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(
			routes.Application.login()
		);
	}
	
	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(
			Routes.javascriptRouter("jsRoutes",
				controllers.routes.javascript.Changes.add(),
				controllers.routes.javascript.Changes.delete(),
				controllers.routes.javascript.Changes.changeSummary()
				//controllers.routes.javascript.Changes.addChange()
			)
		);
	}
	
	
	//Inner class for login - leave at end of class
	public static class Login {
		public String email;
		public String password;
		
		public String validate() {
			if(User.authenticate(email, password) == null) {
				System.out.println("Failing test at inner class validate");
				return "Invalid user or password";
			}
			return null;
		}
	}
	

}
