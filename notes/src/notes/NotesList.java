package notes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="notes")
public class NotesList {
	private List<Note> notes;
	private AtomicInteger noteId;

	public NotesList() {
		notes = new CopyOnWriteArrayList<Note>();
		noteId = new AtomicInteger();
	}

	public List<Note> getNotes() {
		return this.notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();;
		for (Note n : notes)
			sb.append(n.toString());
		return sb.toString();
	}

	public Note find(int id) {
		Note note = null;
		// Search the list -- for now, the list is short enough that
		// a linear search is ok but binary search would be better if the
		// list got to be an order-of-magnitude larger in size.
		for (Note n : notes) {
			if (n.getId() == id) {
				note = n;
				break;
			}
		}
		return note;
	}

	public int add(String body) {
		int id = noteId.incrementAndGet();
		Note n = new Note();
		n.setBody(body);
		n.setId(id);
		notes.add(n);
		return id;
	}
	
	
	public int add(Note partialNote) {
		int id = noteId.incrementAndGet();
		
		// with id, it is complete
		partialNote.setId(id);
		notes.add(partialNote);
		return id;
	}

	public NotesList findSubList(String query) {
		// TODO Auto-generated method stub
		List<Note> subList = new ArrayList<>();
		for(Note n : notes) {
			if(n.containsString(query)) {
				subList.add(n);
			}
		}
		
		if(subList.size() == 0) {
			return null;
		} else {
			NotesList notesSubgrp = new NotesList();
			notesSubgrp.setNotes(subList);
			return notesSubgrp;
		}
	}
}