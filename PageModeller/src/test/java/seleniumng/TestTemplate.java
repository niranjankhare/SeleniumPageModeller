package seleniumng;

import java.net.URL;

import org.testng.annotations.Test;

// All tests should use the TestDataProvider class
@Test(dataProviderClass = TestDataProvider.class)
public class TestTemplate implements TestDataProvider {

	@Override
	public URL getDataResource(String resourceName) {
		Class<?> c = this.getClass();
		return c.getResource(c.getSimpleName() + "/" + resourceName + ".csv");

	}

}
