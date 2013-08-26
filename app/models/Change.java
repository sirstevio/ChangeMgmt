package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class Change extends Model {

    @Id
    public Long id;
    public Date initiated;
    public String summary;
    public String description;
    public ICTSystem system;
    public boolean iaoApproved;
    public boolean testApproved;

    @ManyToOne
    public User initiator;

    @ManyToOne
    public User builder;

//    @OneToMany(cascade = CascadeType.PERSIST)
//    public List<Outage> outages = new ArrayList<Outage>();

	/**
    public Change(String summary, String description, User initiator, User builder, ICTSystem system) {
        this.summary = summary;
        this.description = description;
        this.initiator = initiator;
        this.builder = builder;
        this.system = system;
    }
	*/
	
	
	public Change(String summary, String description, User initiator, ICTSystem system) {
		this.summary = summary;
        this.description = description;
        this.initiator = initiator;
        this.system = system;
	}
	
    public static Model.Finder<Long,Change> find = new Model.Finder(Long.class, Change.class);
	
	/**
	*	Create a new change request
	*
    public static Change create (String summary, String description, String initiator, String builder, Long system) {
        Change change = new Change(summary, description, User.find.ref(initiator),User.find.ref(builder),ICTSystem.find.ref(system));
        change.save();
        return change;
    }
	*/
	
	public static Change create (String summary, String description, String initiator, String system) {		
		
		Change change = new Change(summary, description, User.find.ref(initiator),
				ICTSystem.find.ref(stringLongConverter(system)));
		change.save();
		return change;
	}

//    public static List<Project> findInvolving(String user) {
//        return find.where().eq("members.email", user).findList();
//    }
    public String getSummary(){
        return this.summary;
    }
	
	
	/**
	* 	Checks the user is the initiator of the change.
	*/
	public static boolean isInitiator(Long changeRef, String user) {
		return find.where()
			.eq("initiator.userid", user)
			.eq("id", changeRef)
			.findRowCount() > 0;
	}
	
	/**
	*	Changes the summary of the change to the value of argument newSummary.
	*/
	public static String changeSummary(Long changeRef, String newSummary) {
		Change change= find.ref(changeRef);
		change.summary = newSummary;
		change.update();
		return newSummary;
	}
	
	
	/**
	*	Helper method to convert String to Long. (dirty)
	*/
	private static Long stringLongConverter(String value) {
		long returnMe;
		returnMe = 0;
		try 
		{
			returnMe = Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			System.out.println("NumberFormatException: " + nfe.getMessage());
		}
		return returnMe;
	}
}
