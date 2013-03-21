package taiga.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorLog {
	
	private List<ErrorMessage> messages;
	private boolean stopOnError;

	public ErrorLog() {
		this(true);
	}

	public ErrorLog(boolean stopOnError) {
		this.stopOnError =  stopOnError;
		messages = new ArrayList<ErrorMessage>();
	}

	public ErrorLog addAll(ErrorLog el) {
		if(this != el)
			for(ErrorMessage em: el.getMessages())
				add(em);
		return this;
	}
	
	/** Returns true if parsing should stop */
	public boolean add(ErrorMessage msg) {
		if(!messages.contains(msg)) {
			messages.add(msg);
			return stopOnError;
		} else {
			return false;
		}
	}

	public void addAndThrow(ErrorMessage msg) throws TaigaException {
		if(add(msg))
			throw new TaigaException(msg);
	}

	public boolean isStopOnError() {
		return stopOnError;
	}
	
	public boolean isEmpty() {
		return messages.isEmpty();
	}
	
	public List<ErrorMessage> getMessages() {
		return Collections.unmodifiableList(messages);
	}
	
}
