package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;
import controllers.Helper;

@Entity
public class Outage extends Model {

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="outage_seq_gen")
	@SequenceGenerator(name="outage_seq_gen", sequenceName="OUTAGE_SEQ", allocationSize=1, initialValue=1)
    public Long id;
	@ManyToOne
    public ICTSystem system;
    public int length;
    public String description;
    @ManyToOne
    public Change change;

    public Outage(int length, String desc, ICTSystem system, Change change) {
        //this.id = id;
		this.length = length;
        this.description = desc;
		this.system = system;
		this.change = change;
    }

    public static Model.Finder<Long,Outage> find = new Model.Finder(Long.class, Outage.class);

    /**public static Outage create(Long system, int length, String desc, Long change) {
        Outage outage = new Outage(ICTSystem.find.ref(system), length, desc, Change.find.ref(change));
        outage.save();
        return outage;
    }*/
	
	public static Outage create(String length, String desc, String system, String change) {
        //Long newId = Helper.stringLongConverter(id);
		int i = Integer.parseInt(length);
		ICTSystem newSystem = ICTSystem.find.where().eq("name", system).findUnique();
		Change newChange = Change.find.where().eq("summary", change).findUnique();
		Outage outage = new Outage(i, desc, newSystem, newChange);									
        outage.save();
        return outage;
    }
	
    public static List<Outage> findAffecting(String system) {
        return find.where()
            .eq("ICTSystem.name", system)
            .findList();
    }
	
	public static List<String> outageListAsStrings(List<Outage> oldList) {
		return Helper.listAsStrings(oldList);
	}
}
