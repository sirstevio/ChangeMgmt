package controllers;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import models.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class ChangesTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }
	
	@Test
	public void newChange() {
		Result result = callAction(
			controllers.routes.ref.Changes.add(),
			fakeRequest().withSession("email", "bob@example.com")
				.withFormUrlEncodedBody(ImmutableMap.of("system", "100"))
		);
		assertEquals(200, status(result));
		Change change = Change.find.where()
			.eq("summary", "New change").findUnique();
		
		///Project project = Project.find.where().eq("description", "Some Group").findUnique();
		assertNotNull(change);
		assertEquals("New change", change.summary);
		assertEquals("bob@example.com", change.initiator.userid);
	}
	
	@Test
	public void changeSummary() {
		long id = Change.find.where()
			.eq("summary", "Small change").findUnique().id;
		Result result = callAction(
			controllers.routes.ref.Changes.changeSummary(id),
			fakeRequest().withSession("email", "bob@example.com")
				.withFormUrlEncodedBody(ImmutableMap.of("summary", "New Summary"))
		);
		assertEquals(200, status(result));
		assertEquals("New Summary", Change.find.byId(id).summary);
	}
	
	@Test
	public void changeSummaryForbidden() {
		long id = Change.find.where()
			.eq("summary", "Small change").findUnique().id;
		Result result = callAction(
			controllers.routes.ref.Changes.changeSummary(id),
			fakeRequest().withSession("email", "jeff@example.com")
				.withFormUrlEncodedBody(ImmutableMap.of("summary", "Another New Summary"))
		);
		assertEquals(403, status(result));
		assertEquals("Small change", Change.find.byId(id).summary);
	}
	
	
	@Test
	public void deleteChange() {
		long id = Change.find.where()
			.eq("summary", "Small change").findUnique().id;
		Result result = callAction(
			controllers.routes.ref.Changes.delete(id),
			fakeRequest().withSession("email", "bob@example.com")
		);
		assertEquals(200, status(result));
	}
	
	@Test
	public void deleteChangeForbidden() {
		long id = Change.find.where()
			.eq("summary", "Small change").findUnique().id;
		Result result = callAction(
			controllers.routes.ref.Changes.delete(id),
			fakeRequest().withSession("email", "jeff@example.com")
		);
		assertEquals(403, status(result));
		assertNull(Change.find.byId(id));
	}
}