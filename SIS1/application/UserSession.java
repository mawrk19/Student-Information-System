package application;

import application.UserSession;

public class UserSession {
	private static UserSession instance;
    private String username;
    private String lastname;
    private String firstname;
    private String password;
    private int id;
    private String type;

    private UserSession() {
        // Private constructor to prevent direct instantiation
    }
    
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;	
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public String getFirstname() {
        return firstname;
    }

    public void clearSession() {
        username = null;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
        return type;
    }
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
}