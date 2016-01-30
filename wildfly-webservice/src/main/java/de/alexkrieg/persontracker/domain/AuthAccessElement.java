package de.alexkrieg.persontracker.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthAccessElement implements Serializable {

    public static final String PARAM_AUTH_ID = "auth-id";
    public static final String PARAM_AUTH_TOKEN = "auth-token";

    private String authId;
    private String authToken;
    private String authPermission;
    private String message;

    public AuthAccessElement() {
    	this(null,null,null,null);
    }

    public AuthAccessElement(String authId, String authToken, String authPermission) {
        this(authId,authToken,authPermission,"OK");
    }
    
    public AuthAccessElement(String authId, String authToken, String authPermission, String message) {
        this.authId = authId;
        this.authToken = authToken;
        this.authPermission = authPermission;
        this.message = message;
    }

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getAuthPermission() {
		return authPermission;
	}

	public void setAuthPermission(String authPermission) {
		this.authPermission = authPermission;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}