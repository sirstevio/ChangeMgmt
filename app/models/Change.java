package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

import controllers.Helper;

@Entity
public class Change extends Model {

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="change_seq_gen")
	@SequenceGenerator(name="change_seq_gen", sequenceName="CHANGE_SEQ", allocationSize=1, initialValue=1)
    public Long id;
    public Date initiated;
    public String summary;
    public String description;
	public String businessImpact;
	public String acceptanceCriteria;
	public String testDescription;
	public String testPlan;
	public String rollBackPlan;
	//booleans (approvals)
    public boolean iaoApproved;
	public boolean sysOwnApproved;
    public boolean testApproved;
    public boolean infAssurApproved;
	//enums
	public Status status;
	public Environment environment;
	public ChangeType changeType;
	//lookup attributes
	@ManyToOne
    public ICTSystem system;	
    @ManyToOne
    public User initiator;
    @ManyToOne
    public User builder;

//    @OneToMany(cascade = CascadeType.PERSIST)
//    public List<Outage> outages = new ArrayList<Outage>();

	public Change(String summary, String description, User initiator, ICTSystem system, String type, String env) {
		//this.id = id;
		this.summary = summary;
        this.description = description;
        this.initiator = initiator;
        this.system = system;
		System.out.println("Using Z");
		this.iaoApproved = false;
		this.sysOwnApproved = false;
		this.testApproved = false;
		this.infAssurApproved = false;
		this.status = Status.NEW;
		this.changeType = ChangeType.valueOf(type);
		this.environment = Environment.valueOf(Environment.toEnum(env));
	}
	
    public static Model.Finder<Long,Change> find = new Model.Finder(Long.class, Change.class);
	

	public static Change create(String summary, String description, User initiator, String system, String type, String env, Boolean bool) {		
		//debug
		System.out.println("Using C");
		System.out.println(summary);
		System.out.println(description);
		System.out.println(initiator);
		System.out.println(system);
		System.out.println(type);
		System.out.println(env);
		
		Change change = new Change(summary, description, initiator, 
									ICTSystem.find.where().eq("name", system).findUnique(), type, env
									);
		if (bool == true){
			change.builder = change.initiator;
			change.nextStatus();
			System.out.println("Setting builder to initiator");			
		};
		change.save();
		return change;
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
	
	//Approvals
	public void approveAsIao() {
		this.iaoApproved = true;
		this.update();
	}
	
	public void approveAsSystemOwner() {
		this.sysOwnApproved = true;
		this.update();
	}
	
	public void approveAsTestManager() {
		this.testApproved = true;
		this.update();
	}
	
	public String initialApprovals() {
		if (this.iaoApproved && this.sysOwnApproved && this.testApproved)
		{
			return "true";//"All initial approvals are completed";
		} else {
			return "Initial approvals not yet complete";
		}
	}
	
	public String toString() {
		return this.summary + " " + this.description;
	}
	
	public void nextStatus() {
		System.out.println(this.status);
		System.out.println(this.status.ordinal());
		
		switch (this.status.ordinal()) {
			case 0: this.status = Status.ASSESSMENT;break;
			case 1: this.status = Status.INITIALAPPROVAL;break;
			case 2: this.status = Status.ANALYSIS;break;
			case 3: this.status = Status.SIA;break;
			case 4: this.status = Status.CABAUTHORISATION;break;
			case 5: this.status = Status.IMPLEMENTATION;break;
			case 6: this.status = Status.POSTIMPLEMENTATION;break;
			case 7: this.status = Status.COMPLETE;break;
			case 8: break;
			default: break;
		}
		System.out.println(this.status);
	}
	/**Old code not needed now
	
    public Change(String summary, String description, User initiator, User builder, ICTSystem system) {
        this.summary = summary;
        this.description = description;
        this.initiator = initiator;
        this.builder = builder;
        this.system = system;
    }
	
		public Change(String id, String summary, String description, User initiator, ICTSystem system) {
		this.id = stringLongConverter(id);
		this.summary = summary;
        this.description = description;
        this.initiator = initiator;
        this.system = system;
		System.out.println("Using X");
	}
	//temporary overload
	public Change(Long id, String summary, String description, User initiator, ICTSystem system) {
		this.id = id;
		this.summary = summary;
        this.description = description;
        this.initiator = initiator;
        this.system = system;
		System.out.println("Using Y");
	}
	//temporary overload
		/**
	*	Create a new change request
	*
    public static Change create (String summary, String description, String initiator, String builder, Long system) {
        Change change = new Change(summary, description, User.find.ref(initiator),User.find.ref(builder),ICTSystem.find.ref(system));
        change.save();
        return change;
    }	
	public static Change create(String summary, String description, String initiator, String system) {		
		
		Change change = new Change(summary, description, User.find.ref(initiator),
				ICTSystem.find.ref(stringLongConverter(system)));
		System.out.println("Using A");
		change.save();
		return change;
	}
	//temporary overload
	public static Change create(Long id, String summary, String description, User initiator, ICTSystem system) {		
		//debug
		System.out.println("Using B");
		System.out.println(id);
		System.out.println(summary);
		System.out.println(description);
		System.out.println(initiator);
		System.out.println(system);
		
		Change change = new Change(id, summary, description, initiator, system);
		
		change.save();
		return change;
	}
	//temporary overload
	
    public static List<Project> findInvolving(String user) {
        return find.where().eq("members.email", user).findList();
    } .find.ref(stringLongConverter(system))
	
	*/
		/**
	* 	Helper method to turn a list of Objects into a list of Strings using 
	*	the objects toString method
	*/
	public static List<String> changeListAsStrings(List<Change> oldList) {
		return Helper.listAsStrings(oldList);
	}
	
	public static List<String> environmentList() {
				List<Environment> list = Arrays.asList(Environment.values());
				return Helper.listAsStrings(list);
			}
	
	public enum Status {
			NEW, 
			ASSESSMENT,
			INITIALAPPROVAL,
			ANALYSIS,
			SIA,
			CABAUTHORISATION,
			IMPLEMENTATION,
			POSTIMPLEMENTATION,
			COMPLETE
			;
			
			public String toString() {
				switch (this) {
					case NEW: return "New";
					case ASSESSMENT: return "Assessment";
					case INITIALAPPROVAL: return "Initial Approval";
					case ANALYSIS: return "Analysis";
					case SIA: return "Security Impact Assessment";
					case CABAUTHORISATION: return "CAB Authorisation";
					case IMPLEMENTATION: return "Implementation";
					case POSTIMPLEMENTATION: return "Post-Implementation";
					case COMPLETE: return "Complete";
					default: return name();
				}
			};
	}
	
	private enum Environment {
			CORPORATENETWORK,
			SECURENETWORK,
			BLUELIGHTNETWORK
			;
			public String toString() {
				switch (this) {
					case CORPORATENETWORK: return "Corporate Network";
					case SECURENETWORK: return "Secure Network";
					case BLUELIGHTNETWORK: return "Blue Light Network";
					default: return name();
				}
			};
			
			public static String toEnum(String aString) {
				switch (aString) {
					case "Corporate Network": return "CORPORATENETWORK";
					case "Secure Network": return "SECURENETWORK";
					case "Blue Light Network": return "BLUELIGHTNETWORK";
					default: return aString;
				}
			}
	}
	
	private enum ChangeType {
			STANDARD,
			NONSTANDARD,
			EMERGENCY
			;
			public String toString() {
				switch (this) {
					case STANDARD: return "Standard";
					case NONSTANDARD: return "Non-Standard";
					case EMERGENCY: return "Emergency";
					default: return name();
				}
			};
	}
}
