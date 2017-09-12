package notes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "note")
@XmlType(propOrder = {"id", "body"})
public class Note implements Comparable<Note> {
	
	
	@XmlElement(required = true)
	private int id; // identifier used as lookup-key
	
	@XmlElement(required = true)
	private String body; //

	public Note() {
	}

	@Override
	public String toString() {
		return String.format("%2d: ", id) + body + "\n";
	}



	public void setBody(String what) {
		this.body = what;
	}

	@XmlElement
	public String getBody() {
		return this.body;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	public int getId() {
		return this.id;
	}

	// implementation of Comparable interface
	public int compareTo(Note other) {
		return this.id - other.id;
	}

	public boolean containsString(String query) {
		// TODO Auto-generated method stub
		return body.contains(query);
	}
}