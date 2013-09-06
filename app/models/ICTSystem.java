package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;
import controllers.Helper;

@Entity
public class ICTSystem extends Model {

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="ictsystem_seq_gen")
	@SequenceGenerator(name="ictsystem_seq_gen", sequenceName="ICTSYSTEM_SEQ", allocationSize=1, initialValue=1)
    public Long id;
    public String name;
	@ManyToOne
    public User iao;
	@ManyToOne
    public User systemOwner;
	

    public ICTSystem(String name, User iao, User systemOwner){
        //this.id = id;
		this.name = name;
        this.iao = iao;
		this.systemOwner = systemOwner;
		System.out.println("Created new System");
    }

    public static Model.Finder<Long,ICTSystem> find = new Model.Finder(Long.class, ICTSystem.class);

    public static ICTSystem create(String name, String iao, String systemOwner) {
        User newIao = User.find.where().eq("userid", iao).findUnique();
		User newSystemOwner = User.find.where().eq("userid", systemOwner).findUnique();
		ICTSystem system= new ICTSystem(name, newIao, newSystemOwner);
        system.save();
        return system;
    }

	
	public void setIao(User newiao){
		this.iao = newiao;
	}
	
	public static List<String> systemListAsStrings(List<ICTSystem> oldList) {
		return Helper.listAsStrings(oldList);
	}
	
	public String toString() {
		return this.name;
	}
}