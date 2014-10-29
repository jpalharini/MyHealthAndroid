package android.palharini.myhealth.daos;

public class ConectaWS {

	private static final String URL = "http://192.168.240.151:8080/MyHealthWS/services/";
	private static final String NAMESPACE = "http://dao.ws.myhealth.palharini.android";
	private static final int TIMEOUT = 50000000;
	
	public String getURL() {
		return URL;
	}

	public String getNamespace() {
		return NAMESPACE;
	}

	public int getTimeout() {
		return TIMEOUT;
	}
	
}
