package seleniumng;


import static myAut.PageLibraries.PageRepository.*;
import myAut.PageLibraries.PageRepository;

public class AppTest {

	public static void main(String[] args) {
		new PageRepository();
		
//		pageDashboard.userRole.select ("WHere are you");
		pageLogin.Login("NKH", "somepass");
//		System.out.println(pageLogin.menuConfig.friendlyName);
//		System.out.println(pageLogin.menuConfig.locValue);
//		System.out.println(pageLogin.selLanguage.friendlyName);
//		System.out.println(pageLogin.footerStuff.friendlyName);
//		System.out.println(pageDashboard.footerStuff.friendlyName);
//		System.out.println(pageLogin.controlAtTheBase.friendlyName);

	}

}
