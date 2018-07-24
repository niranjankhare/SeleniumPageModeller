/**
 * 
 */
package org.seleniumng.utils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* Mechanism used to locate elements within a document using a series of other lookups.  This class
* will find all DOM elements that matches each of the locators in sequence, e.g.
*
* <pre>
* driver.findElements(new ByChained(by1, by2))
* </pre>
*
* will find all elements that match <var>by2</var> and appear under an element that matches
* <var>by1</var>.
*/
public class ByChild extends By implements Serializable {

private static final long serialVersionUID = 1563769051170172451L;

private By[] bys;

public ByChild(By... bys) {
 this.bys = bys;
}

@Override
public WebElement findElement(SearchContext context) {
 List<WebElement> elements = findElements(context);
 if (elements.isEmpty())
   throw new NoSuchElementException("Cannot locate an element using " + toString());
 return elements.get(0);
}

@Override
public List<WebElement> findElements(SearchContext context) {
 if (bys.length == 0) {
   return new ArrayList<>();
 }

 List<WebElement> elems = null;
 for (By by : bys) {
   List<WebElement> newElems = new ArrayList<>();

   if (elems == null) {
     newElems.addAll(by.findElements(context));
   } else {
     for (WebElement elem : elems) {
       newElems.addAll(elem.findElements(by));
     }
   }
   elems = newElems;
 }

 return elems;
}

@Override
public String toString() {
 StringBuilder stringBuilder = new StringBuilder("By.chained(");
 stringBuilder.append("{");

 boolean first = true;
 for (By by : bys) {
   stringBuilder.append((first ? "" : ",")).append(by);
   first = false;
 }
 stringBuilder.append("})");
 return stringBuilder.toString();
}

}
