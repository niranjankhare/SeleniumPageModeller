
package ExampleApp.PageLibraries;

import ExampleApp.webPages._PageLogin;

public class PageLogin
    extends _PageLogin
{

	public void login(String username, String password) {
		iTxtUsername.setText(username);
		iTxtPassword.setText(password);
	}


}
