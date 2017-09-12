package notes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
public class NotesRS {

	private static NotesList nlist; // set in populate()

	// should at least read from some file
	// but to let it work with the simple command line publisher
	// I simplify it to read from an array
	private final static String[] exampleNotes = { "This is first note!", "Today, it is very hot....",
			"I hope it becomes cooler tomorrow", "Line 4 contains milk", "Go to school, go to work, then go to club",
			"Irma is not Irmer", "Line 7 is about milk too", "What else to include here?",
			"The is the last note of the example ones", "Line 10: milk is good for your body" };

	public NotesRS() {
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getNotes(@QueryParam("query") String query) {
		checkContext();
		if (query == null) {
			return Response.ok(toJson(nlist), "application/json").build();
		} else {
			NotesList subList = nlist.findSubList(query);
			if (subList == null) {
				String msg = "There are no notes having the key word " + query + ".\n";
				return Response.ok().entity(msg).type(MediaType.TEXT_PLAIN).build();
			} else {
				return Response.ok(toJson(subList), "application/json").build();

			}
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id: \\d+}")
	public Response getNote(@PathParam("id") int id) {
		checkContext();
		return toRequestedType(id, "application/json");
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response create(String jsonBody) {
		checkContext();
		String msg = null;
		// Require both properties to create.
		if (jsonBody == null) {
			msg = "There is no body.\n";
			return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
		}
		// Otherwise, create the Prediction and add it to the collection.
		try {
			Note toAdd = new ObjectMapper().readValue(jsonBody, Note.class);
			int id = addNote(toAdd);
			toAdd.setId(id);
			msg = "Prediction " + id + " created: (body = " + toAdd.getBody() + ").\n";

			return Response.ok(toJson(toAdd), "application/json").build();

		} catch (JsonParseException e) {
			msg = "Unable to parse the message.\n";
		} catch (JsonMappingException e) {
			msg = "Cannot map this to a Note class.\n";
		} catch (IOException e) {
			msg = "IOException occured.\n";
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
	}

	private int addNote(Note toAdd) {
		// TODO Auto-generated method stub
		return nlist.add(toAdd);

	}

	// ** utilities
	private void checkContext() {
		if (nlist == null)
			populate();
	}

	private void populate() {
		nlist = new NotesList();
		for (String record : exampleNotes) {
			addNote(record);
		}
	}

	// Add a new prediction to the list.
	private int addNote(String body) {
		int id = nlist.add(body);
		return id;
	}

	// Prediction --> JSON document
	private String toJson(Note note) {
		String json = "If you see this, there's a problem.";
		try {
			json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(note);
		} catch (Exception e) {
		}
		return json + "\n";
	}

	// PredictionsList --> JSON document
	private String toJson(NotesList nlist) {
		String json = "If you see this, there's a problem.";
		try {
			json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(nlist.getNotes());
		} catch (Exception e) {
		}

		// need to remove the top level, which is {"notes": []}
		// only need everything in []
		// don't know a better way so far

		return json + "\n";
	}

	// Generate an HTTP error response or typed OK response.
	private Response toRequestedType(int id, String type) {
		Note note = nlist.find(id);
		if (note == null) {
			String msg = id + " is a bad ID.\n";
			return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
		} else if (type.contains("json"))
			return Response.ok(toJson(note), type).build();
		else
			return Response.ok(note, type).build(); // this is toXml, which is
													// automatic
	}
}
