package taiga.gwt.client;

import java.util.HashMap;
import java.util.Map;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ErrorLog;
import taiga.interfaces.ErrorMessage;
import taiga.interfaces.PathUtils;
import taiga.interfaces.Position;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public abstract class SiteLoader {
	
	private Map<String, String> contents = new HashMap<String, String>();
	private String basePath;
	private int remaining;
	private ErrorLog errorLog;
	
	public SiteLoader(final ErrorLog log, final String base, final String... names) {
		this.basePath = base;
		this.errorLog = log;
		
		for(final String name: names) {
			final String fullPath = PathUtils.concatPath(base, name);
			remaining++;
			try {
				new RequestBuilder(RequestBuilder.GET, fullPath).sendRequest("", new RequestCallback() {
					  @Override
					  public void onResponseReceived(Request req, Response resp) {
					    String text = resp.getText();
					    contents.put(name, text);
					    loaded(name);
					  }

					  @Override
					  public void onError(Request res, Throwable ex) {
					    log.add(new ErrorMessage(ErrorCode.IO_EXCEPTION, Position.UNDEFINED, null, ex instanceof Exception ? (Exception)ex : null, fullPath));
					    loaded(name);
					  }
					});
			} catch (RequestException e) {
				remaining--;
			    loaded(name);
			}
		}
		
	}
	
	protected void loaded(String name) {
		if(--remaining == 0)
			onLoad();
	}

	public String getBasePath() {
		return basePath;
	}
	
	public String getContent(String name) {
		return contents.get(name);
	}

	public ErrorLog getErrorLog() {
		return errorLog;
	}
	
	public abstract void onLoad();
	
}
