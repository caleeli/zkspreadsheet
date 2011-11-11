/* HSSFWorkbookHelper.java

	Purpose:
		
	Description:
		
	History:
		May 31, 2010 5:53:58 PM, Created by henrichen

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/

package org.zkoss.poi.hssf.usermodel;

import org.zkoss.poi.ddf.EscherBSERecord;
import org.zkoss.poi.hssf.model.InternalWorkbook;

/**
 * A helper class to make HSSFWorkbook package method visible.
 * @author henrichen
 *
 */
public class HSSFWorkbookHelper {
	final HSSFWorkbook _book;
	public HSSFWorkbookHelper(HSSFWorkbook book) {
		_book = book;
	}
	public HSSFWorkbook getWorkbook() {
		return _book;
	}
	public InternalWorkbook getInternalWorkbook() {
		return _book.getWorkbook();
	}
	public HSSFPictureData newHSSFPictureData(EscherBSERecord bse) {
		return new HSSFPictureData(bse);
	}
}
