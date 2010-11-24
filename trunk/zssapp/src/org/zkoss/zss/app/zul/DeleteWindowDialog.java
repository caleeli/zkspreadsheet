/* DeleteDialog.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 12, 2010 11:00:04 AM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 3.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zss.app.zul;

import static org.zkoss.zss.app.base.Preconditions.checkNotNull;

import org.zkoss.poi.ss.usermodel.Sheet;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zss.app.cell.CellHelper;
import org.zkoss.zss.ui.Rect;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Button;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;
/**
 * @author Sam
 *
 */
public class DeleteWindowDialog extends Window implements ZssappComponent {
		
	private final static String URI = "~./zssapp/html/deleteDialog.zul";
	
	private Radiogroup deleteOption;
	private Radio shiftCellLeft;
	private Radio shiftCellUp;
	private Radio entireRow;
	private Radio entireColumn;
	private Button okBtn;
	
	private Spreadsheet ss;
	
	public DeleteWindowDialog() {
		Executions.createComponents(URI, this, null);
		Components.wireVariables(this, this, '$', true, true);
		Components.addForwards(this, this, '$');
		
		deleteOption.setSelectedItem(shiftCellUp);
		
		setWidth("200px");
		setBorder("normal");
		setClosable(true);
		//TODO: use I18n
		setTitle("Delete");
	}

	public void onEcho(Event evt) {
		Radio seld = deleteOption.getSelectedItem();
		Sheet sheet = ss.getSelectedSheet();
		Rect rect = ss.getSelection();
		int tRow = rect.getTop();
		int lCol = rect.getLeft();
		
		/**
		 * For entire row up and entire column left
		 * Note. if there are too many column to up, need to process separately 
		 * to avoid client side freeze java script.
		 * 
		 * TODO: fine-tune javascript to process more efficiently, avoid browser freeze
		 */
		if (seld == shiftCellLeft) {
			CellHelper.shiftCellLeft(sheet, tRow, lCol);
		} else if (seld == shiftCellUp) {
			CellHelper.shiftCellUp(sheet, tRow, lCol);
		} else if (seld == entireRow) {
			CellHelper.shiftEntireRowUp(sheet, tRow, lCol);
		} else if (seld == entireColumn) {
			Integer rowStart = (Integer)evt.getData();
			int rowIdx = CellHelper.shiftEntireColumnLeft(sheet, 
					rowStart != null ? rowStart : tRow, 
					lCol,
					15); //shift 15 columns at once. experiment with FF36, IE8
			if (rowIdx >= 0) {
				Events.echoEvent("onEcho", this, Integer.valueOf(rowIdx));
				return;
			}
		}
		Clients.clearBusy();
		detach();
	}
	
	public void onClick$okBtn() {
		setVisible(false);
		//TODO: use I18n
		Clients.showBusy("Execute... ");
		Events.echoEvent("onEcho", this, null);
	}

	@Override
	public Spreadsheet getSpreadsheet() {
		return ss;
	}

	@Override
	public void setSpreadsheet(Spreadsheet spreadsheet) {
		ss = checkNotNull(spreadsheet, "Spreadsheet is null");
	}
}