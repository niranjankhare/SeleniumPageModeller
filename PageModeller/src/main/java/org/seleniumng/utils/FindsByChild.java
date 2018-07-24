package org.seleniumng.utils;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface FindsByChild {
	  WebElement findElementByChild(String using);

	  List<WebElement> findElementsByChild(String using);
}
