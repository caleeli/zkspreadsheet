/* order_test_1Test.java

	Purpose:
		
	Description:
		
	History:
		Sep, 7, 2010 17:30:59 PM

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

This program is distributed under Apache License Version 2.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/

import org.junit.Test;
import org.zkoss.ztl.Element;
import org.zkoss.ztl.JQuery;
import org.zkoss.ztl.Tags;
import org.zkoss.ztl.Widget;
import org.zkoss.ztl.ZK;
import org.zkoss.ztl.ZKClientTestCase;

import com.thoughtworks.selenium.Selenium;


public class SS_098_Test extends ZKClientTestCase {
	
	public SS_098_Test() {
		target = "http://zktest/zssdemos/index.zul";
		browsers = getBrowsers("chrome");
		_timeout = 60000;
	}
		
	@Test(expected = AssertionError.class)
	public void testClick() {
		for (Selenium browser : browsers) {
			try {
				start(browser);
				windowFocus();
				windowMaximize();
				
				contextMenu(jq("@tab[label=\"Market\"] span.z-tab-text"));				
				waitResponse();
				click(jq("$deleteSheet a.z-menu-item-cnt"));
				waitResponse();

				//how to verify???
			} finally {
				stop();	
			}
		}
	}
}


