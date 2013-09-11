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
		//Form<Change> filledForm = newChangeForm.bindFromRequest();
		System.out.println(form().bindFromRequest().get("builderCheckbox"));
		//System.out.println(filledForm);
		boolean bool = false;
		if (form().bindFromRequest().get("builderCheckbox") != null) {
				System.out.println("set bool to true");
				bool = true;
			} else {
				System.out.println("set bool to false");
				bool = false;
			}
		Change newChange= Change.create(
			form().bindFromRequest().get("summary"),
			form().bindFromRequest().get("description"), User.find.byId(request().username()), 
			form().bindFromRequest().get("system"),
			form().bindFromRequest().get("type"),
			form().bindFromRequest().get("environment"),
			bool
		);
		System.out.println("Using add");
		return redirect(routes.Changes.viewChanges());
	}
	
	/**
	*	View all changes
	*/
	public static Result viewChanges() {
		return ok(
			viewchanges.render(
				Change.find.all(),
				User.find.byId(request().username())
			)
		);
	}
	
	/**
	*	View all changes at the build stage
	*/
	public static Result viewBuild() {
		return ok(
			buildchange.render(
				Helper.filteredChangeList(Change.Status.ASSESSMENT),
				User.find.byId(request().username()),
				form(Change.class)
			)
		);
	}
	
	/**
	*	Submit assessment of a change
	*/
	public static Result changeAssess(Long changeId){
		System.out.println(form().bindFromRequest().get("businessImpact"));
		System.out.println(form().bindFromRequest().get("acceptanceCriteria"));
		Change change = Change.find.byId(changeId);
		change.businessImpact = form().bindFromRequest().get("businessImpact");
		change.acceptanceCriteria = form().bindFromRequest().get("acceptanceCriteria");
		change.nextStatus();
		change.update();
		return redirect(routes.Changes.viewBuild());
	}
	
	/**
	*	View all changes at the assessment stage
	
	public static Result viewApprovals() {
		return viewChangesAtStage(Change.Status.ASSESSMENT);
	}*/
	
	/**
	*	View all changes at the initial approval stage
	*/
	public static Result viewInitialApproval() {
		return ok(
			initapprovalchange.render(
				Helper.filteredChangeList(Change.Status.INITIALAPPROVAL),
				User.find.byId(request().username()),
				form(Change.class)
			)
		);
	}
	
	public static Result approve(Long changeId) {
		System.out.println(changeId);
		Change change = Change.find.byId(changeId);
		System.out.println(change.id);
		change.approveAsIao();
		change.approveAsSystemOwner();
		change.approveAsTestManager();
		change.nextStatus();
		change.update();
		return viewInitialApproval();
	}
	
	/**
	*	View all changes at the analysis stage
	*/
	public static Result viewAnalysis() {
		return ok(
			analysechange.render(
				Helper.filteredChangeList(Change.Status.ANALYSIS),
				User.find.byId(request().username()),
				form(Change.class),
				Outage.find.all()
			)
		);
	}
	
	public static Result submitAnalyse(Long changeId) {
		System.out.println(form().bindFromRequest().get("testDescription"));
		System.out.println(form().bindFromRequest().get("testPlan"));
		System.out.println(form().bindFromRequest().get("rollBackPlan"));
		Change change = Change.find.byId(changeId);
		change.testDescription = form().bindFromRequest().get("testDescription");
		change.testPlan = form().bindFromRequest().get("testPlan");
		change.rollBackPlan = form().bindFromRequest().get("rollBackPlan");
		change.nextStatus();
		change.update();
		return viewAnalysis();
	}
	
	/**
	*	View all changes at the specified stage
	*/
	public static Result viewChangesAtStage(Change.Status status) {
		return ok(
			viewchanges.render(
				Change.find.where().eq("status", status).findList(),
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
	public static Result delete(Long changeId) {
    if(Secured.isInitiatorOf(changeId)) {
			Change.find.ref(changeId).delete();
			return ok();
		} else {
			return forbidden();
		}
	}
	
	public static Result view(Long changeId) {
		return ok(item.render(Change.find.ref(changeId),
							User.find.byId(request().username()))
				);
	}
	
}