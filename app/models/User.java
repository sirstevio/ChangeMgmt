package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

import java.util.*;
import controllers.Helper;

@Entity
public class User extends Model {

    @Id
    public String userid;
    public String name;
    public String password;
    
    public User(String userid, String name, String password) {
      this.userid = userid;
      this.name = name;
      this.password = password;
    }

    public static Model.Finder<String,User> find = new Finder<String,User>(String.class, User.class); 

    public static User authenticate(String userid, String password) 
    {
		System.out.println("Entering User.authenticate method");
		System.out.println(userid);
		System.out.println(password);
		System.out.println(find.where().eq("userid", userid).eq("password", password).findUnique());
		
        return find.where().eq("userid", userid).eq("password", password).findUnique();
    }
	
	public String toString() {
		return this.userid;
	} 
	
	public static List<String> userListAsStrings(List<User> oldList) {
		return Helper.listAsStrings(oldList);
	}
}