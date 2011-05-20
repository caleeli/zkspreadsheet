/* FilterMouseEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		May 20, 2011 11:45:18 AM, Created by henrichen
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zss.ui.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zss.model.Worksheet;

/**
 * A class from handle event which about mouse event on a filter.
 * @author henrichen
 */
public class FilterMouseEvent extends CellMouseEvent {
	private static final long serialVersionUID = 20110520114618L;
	private final int _field;
	public FilterMouseEvent(String name, Component target, int x,int y, int keys,Worksheet sheet, int row ,int col,int clientx,int clienty, int field) {
		super(name, target, x, y, keys, sheet, row, col, clientx, clienty);
		_field = field;
	}
	/**
	 * get Filter field; see @link{Range#autoFilter()}.
	 * @return sheet the related sheet 
	 */
	public int getField(){
		return _field;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("[").append("field:").append(getField())
			.append("]");
		return sb.toString();
	}
}
