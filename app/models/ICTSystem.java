package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class ICTSystem extends Model {

    @Id
    public Long id;
    public String name;
	@ManyToOne
    public User iao;

    public ICTSystem(String name, Long id){
        this.id = id;
		this.name = name;
        this.iao = User.find.ref("jeff@example.com");
		System.out.println("Created new System");
    }

    public static Model.Finder<Long,ICTSystem> find = new Model.Finder(Long.class, ICTSystem.class);

    public static ICTSystem create(String name, Long id) {
        ICTSystem system= new ICTSystem(name, id);
        system.save();
        return system;
    }

	
	public void setIao(User newiao){
		this.iao = newiao;
	}
	
	public static List<String> systems(List<ICTSystem> oldList) {
		Iterator itr = oldList.iterator();
		List<String> systemList = new ArrayList<String>();
		while (itr.hasNext()) {
			systemList.add(itr.next().toString());
		}		
		return systemList;
	}
	
	public String toString() {
		return this.name;
	}
}