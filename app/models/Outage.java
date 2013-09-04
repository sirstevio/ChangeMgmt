package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class Outage extends Model {

    @Id
    public Long id;
	@ManyToOne
    public ICTSystem system;
    public int length;
    public String description;
    @ManyToOne
    public Change change;

    public Outage(ICTSystem system, int length, String desc, Change change) {
        this.system = system;
        this.length = length;
        this.description = desc;
	this.change = change;
    }

    public static Model.Finder<Long,Outage> find = new Model.Finder(Long.class, Outage.class);

    public static Outage create(Long system, int length, String desc, Long change) {
        Outage outage = new Outage(ICTSystem.find.ref(system), length, desc, Change.find.ref(change));
        outage.save();
        return outage;
    }

    public static List<Outage> findAffecting(String system) {
        return find.where()
            .eq("ICTSystem.name", system)
            .findList();
    }

    public String getDescription()
    {
        return this.description;
    }

}
