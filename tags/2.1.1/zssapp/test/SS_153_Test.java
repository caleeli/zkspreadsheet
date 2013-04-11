/* order_test_1Test.java

	Purpose:
		
	Description:
		
	History:
		Sep, 7, 2010 17:30:59 PM

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

This program is distributed under Apache License Version 2.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/

//right click "Align Text Right" : B13
public class SS_153_Test extends SSAbstractTestCase {
	@Override
	protected void executeTest() {
		rightClickCell(1,12);
		click(jq("$styleCtrlBottomPanel @dropdownbutton$halignBtn div.z-dpbutton-arrow"));
		waitResponse();
		click(jq(" @menupopup @menuitem[label=\"Align Text Right\"] a.z-menu-item-cnt:eq(2)"));
		waitResponse();
		rightClickCell(1,13);

		//verify
		String style = getCellStyle(1, 12);
		System.out.println(">>>>>:"+style);
		verifyTrue(containsIgnoreCase(style, "text-align: right"));		
	}
}

