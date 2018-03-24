package seleniumng;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigResolveOptions;

public class testMain {

	public static void main(String[] args) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		ConfigParseOptions parseOptions = ConfigParseOptions.defaults();
		ConfigResolveOptions resolveOptions = ConfigResolveOptions.defaults().setAllowUnresolved(true);
		Config application =ConfigFactory.load(ConfigFactory.defaultApplication(), resolveOptions);
		Config reference =ConfigFactory.load(ConfigFactory.defaultReference(), resolveOptions);
//		Config f = ConfigFactory.load("resolver", parseOptions, resolveOptions );
		Config ready = ConfigFactory.load(/*loader,resolveOptions*/);
		Config fin = ConfigFactory.parseResourcesAnySyntax("resolver");
//f.resolveWith(c);
		System.out.println (ready.root().render(ConfigRenderOptions.concise()));
		fin = ready.withFallback(fin).resolveWith(ready);
		fin = ready.withFallback(fin).resolve();
		
		//fin = fin.resolveWith(ready);
		System.out.println (fin.getString("langwithCountry"));

	}

}
