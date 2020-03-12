package remote.ct.ssh;

public class Key {
	// User login
	private final String login;
	// User password
	private final String password;

	public Key(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "[login=" + login + ", password=" + password + "]";
	}

}
