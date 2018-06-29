package seleniumng;

import org.seleniumng.codegen.PageObjectCodegen;
import org.testng.annotations.Test;

public class CodeGenTests {
	@Test
	public void codegenTest(){
		PageObjectCodegen.main("");
	}
}
