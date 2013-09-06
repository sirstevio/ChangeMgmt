package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import java.util.*;

import models.*;
import views.html.*;

public abstract class Helper {
  
	/**
	*	Helper method to convert String to Long. (dirty)
	*/
	public static Long stringLongConverter(String value) {
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
	
	public static List<String> listAsStrings(List<?> oldList) {
		Iterator itr = oldList.iterator();
		List<String> newList = new ArrayList<String>();
		while (itr.hasNext()) {
			newList.add(itr.next().toString());
		}		
		return newList;
	}

}
