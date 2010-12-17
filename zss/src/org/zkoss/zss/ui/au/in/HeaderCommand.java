/* HeaderCommand.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Dec 18, 2007 12:10:40 PM     2007, Created by Dennis.Chen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under Lesser GPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zss.ui.au.in;


import java.util.Map;

import org.zkoss.poi.ss.usermodel.Sheet;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
//import org.zkoss.zss.model.Sheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.HeaderEvent;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zss.ui.sys.SpreadsheetInCtrl;


/**
 * A Command (client to server) for handling event about a header(top header or left header) 
 * @author Dennis.Chen
 *
 */
public class HeaderCommand implements Command {
	final static String Command = "onZSSHeaderModif";


	public void process(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
		final Map data = (Map) request.getData();
		
		String type = (String) data.get("type");
		if ("top".equals(type)) {
			processTopHeader((Spreadsheet) comp, data);
		} else if ("left".equals(type)) {
			processLeftHeader((Spreadsheet) comp, data);
		} else {
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] {
					"Type:" + type, this });
		}
	}
	
	private void processTopHeader(Spreadsheet spreadsheet, Map data){
		String sheetId = (String) data.get("sheetId");
		Sheet sheet = spreadsheet.getSelectedSheet();
		if(!Utils.getSheetUuid(sheet).equals(sheetId)) {
			return;
		}
		String event = (String) data.get("event");
		int index = (Integer) data.get("index");
		
		if("size".equals(event)){
			int newsize = (Integer) data.get("newsize");
			int id = (Integer) data.get("id");
			boolean hidden = (Boolean) data.get("hidden");
			((SpreadsheetInCtrl)spreadsheet.getExtraCtrl()).setColumnSize(sheetId, index, newsize,id, hidden);
			
			if (Events.isListened(spreadsheet, org.zkoss.zss.ui.event.Events.ON_HEADER_SIZE, true)){
				HeaderEvent he = new HeaderEvent(org.zkoss.zss.ui.event.Events.ON_HEADER_SIZE, spreadsheet, sheet, HeaderEvent.TOP_HEADER,index, Integer.valueOf(newsize), hidden);
				Events.postEvent(he);
			}
		}else{
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] {"Event:"+event, this});
		}
		
	}
	
	private void processLeftHeader(Spreadsheet spreadsheet, Map data){
		String sheetId = (String) data.get("sheetId");
		Sheet sheet = spreadsheet.getSelectedSheet();
		if(!Utils.getSheetUuid(sheet).equals(sheetId)) {
			return;
		}
		String event = (String) data.get("event");
		int index = (Integer) data.get("index");
		if("size".equals(event)){
			int newsize = (Integer) data.get("newsize");
			int id = (Integer) data.get("id");
			boolean hidden = (Boolean) data.get("hidden");
			((SpreadsheetInCtrl)spreadsheet.getExtraCtrl()).setRowSize(sheetId, index, newsize,id, hidden);
			if (Events.isListened(spreadsheet,org.zkoss.zss.ui.event.Events.ON_HEADER_SIZE, true)){
				HeaderEvent he = new HeaderEvent(org.zkoss.zss.ui.event.Events.ON_HEADER_SIZE, spreadsheet, sheet, HeaderEvent.LEFT_HEADER,index, Integer.valueOf(newsize), hidden);
				Events.postEvent(he);
			}
		} else {
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] {"Event:"+event, this});
		}
	}

	public String getCommand() {
		return Command;
	}
}