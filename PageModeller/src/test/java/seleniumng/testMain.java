/*******************************************************************************
 * Copyright 2018 Niranjan Khare
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
