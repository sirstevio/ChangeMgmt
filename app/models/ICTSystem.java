package models;

import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class ICTSystem extends Model {

    @Id
    public Long id;
    public String name;
    public User iao;

    public ICTSystem(String name, User iao){
        this.name = name;
        this.iao = iao;
    }

    public static Model.Finder<Long,ICTSystem> find = new Model.Finder(Long.class, ICTSystem.class);

    public static ICTSystem create(String name, String iao) {
        ICTSystem system= new ICTSystem(name, User.find.ref(iao));
        system.save();
        return system;
    }

    public String getName(){
        return this.name;
    }
}
