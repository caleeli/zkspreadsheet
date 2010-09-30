/* RangeImpl.java

	Purpose:
		
	Description:
		
	History:
		Mar 10, 2010 2:54:55 PM, Created by henrichen

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/

package org.zkoss.zss.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zss.engine.Ref;
import org.zkoss.zss.engine.RefBook;
import org.zkoss.zss.engine.RefSheet;
import org.zkoss.zss.engine.event.SSDataEvent;
import org.zkoss.zss.engine.impl.AreaRefImpl;
import org.zkoss.zss.engine.impl.CellRefImpl;
import org.zkoss.zss.engine.impl.ChangeInfo;
import org.zkoss.zss.engine.impl.MergeChange;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.FormatText;
import org.zkoss.zss.model.Range;

/**
 * Implementation of {@link Range} which plays a facade to operate on the spreadsheet data models
 * and maintain the reference dependency. 
 * @author henrichen
 *
 */
public class RangeImpl implements Range {
	private final Sheet _sheet;
	private final Sheet _lastSheet;
	private int _left = Integer.MAX_VALUE;
	private int _top = Integer.MAX_VALUE;
	private int _right = Integer.MIN_VALUE;
	private int _bottom = Integer.MIN_VALUE;
	private Set<Ref> _refs;
	
	public RangeImpl(int row, int col, Sheet sheet, Sheet lastSheet) {
		_sheet = sheet;
		_lastSheet = lastSheet;
		initCellRef(row, col, sheet, lastSheet);
	}
	
	private void initCellRef(int row, int col, Sheet sheet, Sheet lastSheet) {
		final Book book  = (Book) sheet.getWorkbook();
		final int s1 = book.getSheetIndex(sheet);
		final int s2 = book.getSheetIndex(lastSheet);
		final int sb = Math.min(s1,s2);
		final int se = Math.max(s1,s2);
		for (int s = sb; s <= se; ++s) {
			final Sheet sht = book.getSheetAt(s);
			final RefSheet refSht = BookHelper.getOrCreateRefBook(book).getOrCreateRefSheet(sht.getSheetName());
			addRef(new CellRefImpl(row, col, refSht));
		}
	}
	
	public RangeImpl(int tRow, int lCol, int bRow, int rCol, Sheet sheet, Sheet lastSheet) {
		_sheet = sheet;
		_lastSheet = lastSheet;
		if (tRow == bRow && lCol == rCol) 
			initCellRef(tRow, lCol, sheet, lastSheet);
		else
			initAreaRef(tRow, lCol, bRow, rCol, sheet, lastSheet);
	}
	
	/*package*/ RangeImpl(Ref ref, Sheet sheet) {
		_sheet = sheet;
		_lastSheet = sheet;
		addRef(ref);
	}
	
	private void initAreaRef(int tRow, int lCol, int bRow, int rCol, Sheet sheet, Sheet lastSheet) {
		final Book book  = (Book) sheet.getWorkbook();
		final int s1 = book.getSheetIndex(sheet);
		final int s2 = book.getSheetIndex(lastSheet);
		final int sb = Math.min(s1,s2);
		final int se = Math.max(s1,s2);
		for (int s = sb; s <= se; ++s) {
			final Sheet sht = book.getSheetAt(s);
			final RefSheet refSht = BookHelper.getOrCreateRefBook(book).getOrCreateRefSheet(sht.getSheetName());
			addRef(new AreaRefImpl(tRow, lCol, bRow, rCol, refSht));
		}
	}
	
	@Override
	public Collection<Ref> getRefs() {
		if (_refs == null) {
			_refs = new HashSet<Ref>(3);
		}
		return _refs;
	}
	
	@Override
	public Sheet getFirstSheet() {
		return _sheet;
	}

	@Override
	public Sheet getLastSheet() {
		return _lastSheet;
	}
	
	@Override
	public Hyperlink getHyperlink() {
		Ref ref = _refs != null && !_refs.isEmpty() ? _refs.iterator().next() : null;
		if (ref != null) {
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final RefSheet refSheet = ref.getOwnerSheet();
			final Cell cell = getCell(tRow, lCol, refSheet);
			if (cell != null)
				return BookHelper.getHyperlink(cell);
		}
		return null;
	}
	public void setHyperlink(int linkType, String address, String display) {
		Set<Ref>[] refs = null;
		
		refs = new HyperlinkSetter().setValue(new HyperlinkContext(linkType, address, display));
			
		reevaluateAndNotify(refs);
	}

	private class HyperlinkSetter extends ValueSetter {
		
		protected Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value) {
			HyperlinkContext context = (HyperlinkContext)value;
			Cell cell = BookHelper.getOrCreateCell(_sheet, row, col);
			return BookHelper.setCellHyperlink(cell, context.getLinktype(), context.getAddress(), context.getDisplay());
		}
		
	}
	
	private class HyperlinkContext {
		private int _linkType;
		private String _address;
		private String _display;
		
		HyperlinkContext(int linkType, String address, String display) {
			_linkType = linkType;
			_address = address;
			_display = display;
		}
		
		public int getLinktype() {
			return _linkType;
		}
		
		public String getAddress() {
			return _address;
		}
		
		public String getDisplay() {
			return _display;
		}
	}
	@Override
	public RichTextString getText() {
		Ref ref = _refs != null && !_refs.isEmpty() ? _refs.iterator().next() : null;
		if (ref != null) {
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final RefSheet refSheet = ref.getOwnerSheet();
			final Cell cell = getCell(tRow, lCol, refSheet);
			if (cell != null)
				return BookHelper.getText(cell);
		}
		return null;
	}
	@Override
	public FormatText getFormatText() {
		Ref ref = _refs != null && !_refs.isEmpty() ? _refs.iterator().next() : null;
		if (ref != null) {
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final RefSheet refSheet = ref.getOwnerSheet();
			final Cell cell = getCell(tRow, lCol, refSheet);
			if (cell != null)
				return BookHelper.getFormatText(cell);
		}
		return null;
	}
	
	@Override
	public RichTextString getRichEditText() {
		Ref ref = _refs != null && !_refs.isEmpty() ? _refs.iterator().next() : null;
		if (ref != null) {
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final RefSheet refSheet = ref.getOwnerSheet();
			final Cell cell = getCell(tRow, lCol, refSheet);
			if (cell != null) {
				return BookHelper.getRichEditText(cell);
			}
		}
		return null;
	}
	
	@Override
	public void setRichEditText(RichTextString rstr) {
		setValue(rstr);
	}
	
	@Override
	public String getEditText() {
		Ref ref = _refs != null && !_refs.isEmpty() ? _refs.iterator().next() : null;
		if (ref != null) {
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final RefSheet refSheet = ref.getOwnerSheet();
			final Cell cell = getCell(tRow, lCol, refSheet);
			if (cell != null) {
				return BookHelper.getEditText(cell);
			}
		}
		return null;
	}
	
	@Override
	public void setEditText(String txt) {
		Set<Ref>[] refs = null;
		final Object[] values = BookHelper.editTextToValue(txt);
		
		if (values != null) {
			switch(((Integer)values[0]).intValue()) {
			case Cell.CELL_TYPE_FORMULA:
				refs = setFormula((String)values[1]); //Formula
				break;
			case Cell.CELL_TYPE_STRING:
				refs = setValue((String)values[1]); //String
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				refs = setValue((Boolean)values[1]); //boolean
				break;
			case Cell.CELL_TYPE_NUMERIC:
				final Object val = values[1];
				refs = val instanceof Number ?
					setValue((Number)val): //number
					setValue((Date)val); //date
				break;
			case Cell.CELL_TYPE_ERROR:
				refs = setValue((Byte)values[1]);
				break;
			}
		} else {
			refs = setValue((String) null); 
		}
		
		reevaluateAndNotify(refs);
	}
	
	/*package*/ void reevaluateAndNotify(Set<Ref>[] refs) { //[0]: last, [1]: all
		if (refs != null) {
			final Book book  = (Book) _sheet.getWorkbook();
			BookHelper.reevaluateAndNotify(book, refs[0], refs[1]);
		}
	}
	
	/*package*/ Set<Ref>[] setValue(String value) {
		return new StringValueSetter().setValue(value);
	}
	
	/*package*/ Set<Ref>[] setValue(Number value) {
		return new NumberValueSetter().setValue(value);
	}
	
	/*package*/ Set<Ref>[] setValue(Boolean value) {
		return new BooleanValueSetter().setValue(value);
	}
	
	/*package*/ Set<Ref>[] setValue(Date value) {
		return new DateValueSetter().setValue(value);
	}
	
	/*package*/ Set<Ref>[] setValue(RichTextString value) {
		return new RichTextStringValueSetter().setValue(value);
	}
	
	/*package*/ Set<Ref>[] setValue(Byte error) {
		return new ErrorValueSetter().setValue(error);
	}

	/*package*/ Set<Ref>[] setFormula(String value) {
		return new FormulaValueSetter().setValue(value);
	}
	
	private abstract class ValueSetter {
		private Set<Ref> _last;
		private Set<Ref> _all;
		private ValueSetter() {
			_last = new HashSet<Ref>();
			_all = new HashSet<Ref>();
		}
		/**
		 * Set specified value into cells specified by this Range.  
		 * @param value the value to be set into the cell
		 * @Return Ref set array: [0] is the last formula cells to be re-evaluated, [1] is the cells that has changed
		 */
		@SuppressWarnings("unchecked")
		public Set<Ref>[] setValue(Object value) {
			for(Ref ref : _refs) {
				final int tRow = ref.getTopRow();
				final int lCol = ref.getLeftCol();
				final int bRow = ref.getBottomRow();
				final int rCol = ref.getRightCol();
				final RefSheet refSheet = ref.getOwnerSheet();
				
				for(int row = tRow; row <= bRow; ++row) {
					for (int col = lCol; col <= rCol; ++col) {
						Set<Ref>[] refs = setCellValue(row, col, refSheet, value);
						if (refs != null) {
							_last.addAll(refs[0]);
							_all.addAll(refs[1]);
						}
					}
				}
				_all.add(ref); //this reference shall be reloaded, too.
			}
			return (Set<Ref>[]) new Set[] {_last, _all};
		}
		
		/**
		 * Set specified value into cell at the specified row and column.  
		 * @param rowIndex row index
		 * @param colIndex column index
		 * @param value the value to be set into the cell
		 * @Return Ref set arrary: [0] are the cells to be re-evaluated, [1] are the cells that were consequently updated 
		 */
		protected abstract Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value);
	}

	private class NumberValueSetter extends ValueSetter {
		@Override
		protected Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value) {
			return setCellValue(row, col, refSheet, ((Number)value).doubleValue());
		}
		
		private Set<Ref>[] setCellValue(int rowIndex, int colIndex, RefSheet refSheet, double value) {
			//locate the cell of the refSheet
			final Cell cell = getOrCreateCell(rowIndex, colIndex, refSheet, Cell.CELL_TYPE_NUMERIC);
			return BookHelper.setCellValue(cell, value);
		}
	}
	
	private class BooleanValueSetter extends ValueSetter {
		@Override
		protected Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value) {
			return setCellValue(row, col, refSheet, ((Boolean)value).booleanValue());
		}
		
		private Set<Ref>[] setCellValue(int rowIndex, int colIndex, RefSheet refSheet, boolean value) {
			//locate the cell of the refSheet
			final Cell cell = getOrCreateCell(rowIndex, colIndex, refSheet, Cell.CELL_TYPE_BOOLEAN);
			return BookHelper.setCellValue(cell, value);
		}
	}
	
	private class ErrorValueSetter extends ValueSetter {
		@Override
		protected Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value) {
			return setCellValue(row, col, refSheet, ((Byte)value).byteValue());
		}
		
		private Set<Ref>[] setCellValue(int rowIndex, int colIndex, RefSheet refSheet, byte value) {
			//locate the cell of the refSheet
			final Cell cell = getOrCreateCell(rowIndex, colIndex, refSheet, Cell.CELL_TYPE_ERROR);
			return BookHelper.setCellErrorValue(cell, value);
		}
	}
	
	private class DateValueSetter extends ValueSetter {
		@Override
		protected Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value) {
			return setCellValue(row, col, refSheet, (Date) value);
		}
		
		private Set<Ref>[] setCellValue(int rowIndex, int colIndex, RefSheet refSheet, Date value) {
			//locate the cell of the refSheet
			final Cell cell = getOrCreateCell(rowIndex, colIndex, refSheet, Cell.CELL_TYPE_NUMERIC);
			return BookHelper.setCellValue(cell, value);
		}
	}
	
	private class FormulaValueSetter extends ValueSetter {
		@Override
		protected Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value) {
			return setCellFormula(row, col, refSheet, (String) value);
		}
		
		private Set<Ref>[] setCellFormula(int rowIndex, int colIndex, RefSheet refSheet, String value) {
			//locate the cell of the refSheet
			final Cell cell = getOrCreateCell(rowIndex, colIndex, refSheet, Cell.CELL_TYPE_FORMULA);
			return BookHelper.setCellFormula(cell, value);
		}
	}
	
	private class StringValueSetter extends ValueSetter {
		@Override
		protected Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value) {
			return setCellValue(row, col, refSheet, (String) value);
		}
		
		private Set<Ref>[] setCellValue(int rowIndex, int colIndex, RefSheet refSheet, String value) {
			//locate the cell of the refSheet
			final Cell cell = getOrCreateCell(rowIndex, colIndex, refSheet, Cell.CELL_TYPE_STRING);
			return BookHelper.setCellValue(cell, value);
		}
	}
	
	private class RichTextStringValueSetter extends ValueSetter {
		@Override
		protected Set<Ref>[] setCellValue(int row, int col, RefSheet refSheet, Object value) {
			return setCellValue(row, col, refSheet, (RichTextString) value);
		}
		
		private Set<Ref>[] setCellValue(int rowIndex, int colIndex, RefSheet refSheet, RichTextString value) {
			//locate the model book and sheet of the refSheet
			final Cell cell = getOrCreateCell(rowIndex, colIndex, refSheet, Cell.CELL_TYPE_STRING);
			return BookHelper.setCellValue(cell, value);
		}
	}
	
	private Cell getCell(int rowIndex, int colIndex, RefSheet refSheet) {
		//locate the model book and sheet of the refSheet
		final Book book = BookHelper.getBook(_sheet, refSheet);
		if (book != null) {
			final Sheet sheet = book.getSheet(refSheet.getSheetName());
			if (sheet != null) {
				final Row row = sheet.getRow(rowIndex);
				if (row != null) {
					return row.getCell(colIndex);
				}
			}
		}
		return null;
	}
	
	private Cell getOrCreateCell(int rowIndex, int colIndex, RefSheet refSheet, int cellType) {
		//locate the model book and sheet of the refSheet
		final Book book = BookHelper.getBook(_sheet, refSheet);
		final String sheetname = refSheet.getSheetName();
		final Sheet sheet = getOrCreateSheet(book, sheetname);
		final Row row = getOrCreateRow(sheet, rowIndex);
		return getOrCreateCell(row, colIndex, cellType);
	}
	
	private Sheet getOrCreateSheet(Book book, String sheetName) {
		Sheet sheet = book.getSheet(sheetName);
		if (sheet == null) {
			sheet = book.createSheet(sheetName);
		}
		return sheet;
	}
	
	private Row getOrCreateRow(Sheet sheet, int rowIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		return row;
	}
	
	private Cell getOrCreateCell(Row row, int colIndex, int type) {
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex, type); 
		}
		return cell;
	}

	/*package*/ Ref addRef(Ref ref) {
		final Collection<Ref> refs = getRefs();
		_left = Math.min(ref.getLeftCol(), _left);
		_top = Math.min(ref.getTopRow(), _top);
		_right = Math.max(ref.getRightCol(), _right);
		_bottom = Math.max(ref.getBottomRow(), _bottom);
		refs.add(ref);
		return ref;
	}
	
	@Override
	public void delete(int shift) {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final RefBook refBook = refSheet.getOwnerBook();
			switch(shift) {
			default:
			case SHIFT_DEFAULT:
				if (ref.isWholeRow()) {
					final ChangeInfo info = BookHelper.deleteRows(_sheet, ref.getTopRow(), ref.getRowCount());
					notifyMergeChange(refBook, info, ref, SSDataEvent.ON_RANGE_DELETE, SSDataEvent.MOVE_V);
				} else if (ref.isWholeColumn()) {
					final ChangeInfo info = BookHelper.deleteColumns(_sheet, ref.getLeftCol(), ref.getColumnCount());
					notifyMergeChange(refBook, info, ref, SSDataEvent.ON_RANGE_DELETE, SSDataEvent.MOVE_H);
				}
				break;
			case SHIFT_LEFT:
				if (ref.isWholeRow() || ref.isWholeColumn()) {
					delete(SHIFT_DEFAULT);
				} else {
					final ChangeInfo info = BookHelper.deleteRange(_sheet, ref.getTopRow(), ref.getLeftCol(), ref.getBottomRow(), ref.getRightCol(), true);
					notifyMergeChange(refBook, info, ref, SSDataEvent.ON_RANGE_DELETE, SSDataEvent.MOVE_H);
				}
				break;
			case SHIFT_UP:
				if (ref.isWholeRow() || ref.isWholeColumn()) {
					delete(SHIFT_DEFAULT);
				} else {
					final ChangeInfo info = BookHelper.deleteRange(_sheet, ref.getTopRow(), ref.getLeftCol(), ref.getBottomRow(), ref.getRightCol(), false);
					notifyMergeChange(refBook, info, ref, SSDataEvent.ON_RANGE_DELETE, SSDataEvent.MOVE_V);
				}
				break;
			}
		}
	}
	
	@Override
	public void insert(int shift, int copyOrigin) {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final RefBook refBook = refSheet.getOwnerBook();
			switch(shift) {
			default:
			case SHIFT_DEFAULT:
				if (ref.isWholeRow()) {
					final ChangeInfo info = BookHelper.insertRows(_sheet, ref.getTopRow(), ref.getRowCount(), copyOrigin);
					notifyMergeChange(refBook, info, ref, SSDataEvent.ON_RANGE_INSERT, SSDataEvent.MOVE_V);
				} else if (ref.isWholeColumn()) {
					final ChangeInfo info = BookHelper.insertColumns(_sheet, ref.getLeftCol(), ref.getColumnCount(), copyOrigin);
					notifyMergeChange(refBook, info, ref, SSDataEvent.ON_RANGE_INSERT, SSDataEvent.MOVE_H);
				}
				break;
			case SHIFT_RIGHT:
				if (ref.isWholeRow() || ref.isWholeColumn()) {
					insert(SHIFT_DEFAULT, copyOrigin);
				} else {
					final ChangeInfo info = BookHelper.insertRange(_sheet, ref.getTopRow(), ref.getLeftCol(), ref.getBottomRow(), ref.getRightCol(), true, copyOrigin);
					notifyMergeChange(refBook, info, ref, SSDataEvent.ON_RANGE_INSERT, SSDataEvent.MOVE_H);
				}
				break;
			case SHIFT_DOWN:
				if (ref.isWholeRow() || ref.isWholeColumn()) {
					insert(SHIFT_DEFAULT, copyOrigin);
				} else {
					final ChangeInfo info = BookHelper.insertRange(_sheet, ref.getTopRow(), ref.getLeftCol(), ref.getBottomRow(), ref.getRightCol(), false, copyOrigin);
					notifyMergeChange(refBook, info, ref, SSDataEvent.ON_RANGE_INSERT, SSDataEvent.MOVE_V);
				}
				break;
			}
		}
	}
	
	private void notifyMergeChange(RefBook refBook, ChangeInfo info, Ref ref, String event, int orient) {
		if (info == null) {
			return;
		}
		final Set<Ref> last = info.getToEval();
		final Set<Ref> all = info.getAffected();
		refBook.publish(new SSDataEvent(event, ref, orient));
		//must delete and add in batch, or merge ranges can interfere to each other
		for(MergeChange change : info.getMergeChanges()) {
			final Ref orgMerge = change.getOrgMerge();
			if (orgMerge != null) {
				refBook.publish(new SSDataEvent(SSDataEvent.ON_MERGE_DELETE, orgMerge, orient));
			}
		}
		for(MergeChange change : info.getMergeChanges()) {
			final Ref merge = change.getMerge();
			if (merge != null) {
				refBook.publish(new SSDataEvent(SSDataEvent.ON_MERGE_ADD, merge, orient));
			}
		}
		BookHelper.reevaluateAndNotify((Book) _sheet.getWorkbook(), last, all);
	}

	@Override
	public void pasteSpecial(int pasteType, int operation, boolean SkipBlanks, boolean transpose) {
		//TODO
		//clipboardRange.pasteSpecial(this, pasteType, pasteOp, skipBlanks, transpose);
	}
	
	@Override
	public void pasteSpecial(Range dstRange, int pasteType, int pasteOp, boolean skipBlanks, boolean transpose) {
		paste0(dstRange, pasteType, pasteOp, skipBlanks, transpose);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void sort(Range rng1, boolean desc1, Range rng2, int type, boolean desc2, Range rng3, boolean desc3, int header, int orderCustom,
			boolean matchCase, boolean sortByRows, int sortMethod, int dataOption1, int dataOption2, int dataOption3) {
		final Ref key1 = rng1 != null ? rng1.getRefs().iterator().next() : null;
		final Ref key2 = rng2 != null ? rng2.getRefs().iterator().next() : null;
		final Ref key3 = rng3 != null ? rng3.getRefs().iterator().next() : null;
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final int bRow = ref.getBottomRow();
			final int rCol = ref.getRightCol();
			final Sheet sheet = BookHelper.getSheet(_sheet, ref.getOwnerSheet());
			Set<Ref>[] refs = BookHelper.sort(sheet, tRow, lCol, bRow, rCol, 
								key1, desc1, key2, type, desc2, key3, desc3, header, 
								orderCustom, matchCase, sortByRows, sortMethod, dataOption1, dataOption2, dataOption3);
			if(refs != null) {
				refs[1].add(ref);
			} else {
				final Set<Ref> all = new HashSet<Ref>();
				all.add(ref);
				refs = new Set[2];
				refs[0] = Collections.emptySet();
				refs[1] = all;
			}
			reevaluateAndNotify(refs);
		}
	}
	@Override
	public void copy(Range dstRange) {
		paste0(dstRange, Range.PASTE_ALL, Range.PASTEOP_NONE, false, false);
	}
	private void paste0(Range dstRange, int pasteType, int pasteOp, boolean skipBlanks, boolean transpose) {
		if (_refs != null && !_refs.isEmpty() && !dstRange.getRefs().isEmpty()) {
			//check if follow the copy rule
			//destination range allow only one contiguous reference
			if (dstRange.getRefs().size() > 1) {
				throw new UiException("Command cannot be used on multiple selections");
			}
			//source range can handle only same rows/columns multiple references
			Iterator<Ref> it = _refs.iterator();
			Ref ref1 = it.next();
			int srcRowCount = ref1.getRowCount();
			int srcColCount = ref1.getColumnCount();
			final Ref dstRef = dstRange.getRefs().iterator().next();
			final Set<Ref> last = new HashSet<Ref>();
			final Set<Ref> all = new HashSet<Ref>();
			if (_refs.size() > 1) { //multiple src references
				final SortedMap<Integer, Ref> srcRefs = new TreeMap<Integer, Ref>();
				boolean sameRow = false;
				boolean sameCol = false;
				final int lCol = ref1.getLeftCol();
				final int tRow = ref1.getTopRow();
				final int rCol = ref1.getRightCol();
				final int bRow = ref1.getBottomRow();
				while (it.hasNext()) {
					final Ref ref = it.next();
					if (lCol == ref.getLeftCol() && rCol == ref.getRightCol()) { //same column
						if (sameRow) { //cannot be both sameRow and sameColumn
							throw new UiException("Command cannot be used on multiple selections");
						}
						if (srcRefs.isEmpty()) {
							srcRefs.put(new Integer(tRow), ref1); //sorted on Row
						}
						srcRefs.put(new Integer(ref.getTopRow()), ref);
						sameCol = true;
						srcRowCount += ref.getRowCount();
					} else if (tRow == ref.getTopRow() && bRow == ref.getBottomRow()) { //same row
						if (sameCol) { //cannot be both sameRow and sameColumn
							throw new UiException("Command cannot be used on multiple selections");
						}
						if (srcRefs.isEmpty()) {
							srcRefs.put(new Integer(lCol), ref1); //sorted on column
						}
						srcRefs.put(new Integer(ref.getLeftCol()), ref);
						sameRow = true;
						srcColCount += ref.getColumnCount();
					} else { //not the same column or same row
						throw new UiException("Command cannot be used on multiple selections");
					}
				}
				pasteType = pasteType + Range.PASTE_VALUES; //no formula 
				copyMulti(sameRow, srcRefs, srcColCount, srcRowCount, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
			} else {
				copy(ref1, srcColCount, srcRowCount, dstRange, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
			}
			final Book book = (Book) _sheet.getWorkbook();
			BookHelper.reevaluateAndNotify(book, last, all);
		}
	}

	@Override
	public void borderAround(BorderStyle lineStyle, String color) {
		setBorders(BookHelper.BORDER_OUTLINE, lineStyle, color);
	}
	
	@Override
	public void merge(boolean across) {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final RefBook refBook = refSheet.getOwnerBook();
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final int bRow = ref.getBottomRow();
			final int rCol = ref.getRightCol();
			final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
			final ChangeInfo info = BookHelper.merge(sheet, tRow, lCol, bRow, rCol, across);
			notifyMergeChange(refBook, info, ref, SSDataEvent.ON_CONTENTS_CHANGE, SSDataEvent.MOVE_NO);
		}
	}
	
	@Override
	public void unMerge() {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final RefBook refBook = refSheet.getOwnerBook();
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final int bRow = ref.getBottomRow();
			final int rCol = ref.getRightCol();
			final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
			final ChangeInfo info = BookHelper.unMerge(sheet, tRow, lCol, bRow, rCol);
			notifyMergeChange(refBook, info, ref, SSDataEvent.ON_CONTENTS_CHANGE, SSDataEvent.MOVE_NO);
		}
	}
	
	@Override
	public Range getCells(int row, int col) {
		return new RangeImpl(row, col, _sheet, _sheet);
	}
			
	private void copyMulti(boolean sameRow, SortedMap<Integer, Ref> srcRefs, int srcColCount, int srcRowCount, Ref dstRef, int pasteType, int pasteOp, boolean skipBlanks, boolean transpose, Set<Ref> last, Set<Ref> all) {
		final int dstColCount = transpose ? dstRef.getRowCount() : dstRef.getColumnCount();
		final int dstRowCount = transpose ? dstRef.getColumnCount() : dstRef.getRowCount();
		
		if ((dstRowCount % srcRowCount) == 0 && (dstColCount % srcColCount) == 0) {
			copyRefs(sameRow, srcRefs, srcColCount, srcRowCount, dstColCount/srcColCount, dstRowCount/srcRowCount, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
		} else if (dstColCount == 1 && (dstRowCount % srcRowCount) == 0) {
			copyRefs(sameRow, srcRefs, srcColCount, srcRowCount, 1, dstRowCount/srcRowCount, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
		} else if (dstRowCount == 1 && (dstColCount % srcColCount) == 0) {
			copyRefs(sameRow, srcRefs, srcColCount, srcRowCount, dstColCount/srcColCount, 1, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
		} else {
			copyRefs(sameRow, srcRefs, srcColCount, srcRowCount, 1, 1, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
		}
	}

	private void rowCopyRefs(Map<Integer, Ref> srcRefs, int srcColCount, int srcRowCount, int colRepeat, int rowRepeat, RefSheet dstSheet, int tRow, int bRow, int lCol, int pasteType, int pasteOp, boolean skipBlanks, boolean transpose, Set<Ref> last, Set<Ref> all) {
		int dsttRow = tRow;
		int dstbRow = bRow;
		for(int rr = rowRepeat; rr > 0; --rr) {
			int lCol0 = lCol;
			for (int cr = colRepeat; cr > 0; --cr) {
				for (Entry<Integer, Ref> srcEntry : srcRefs.entrySet()) {
					final Ref srcRef = srcEntry.getValue();
					int rCol0 = lCol0 + (transpose ? srcRef.getRowCount() : srcRef.getColumnCount()) - 1; 
					final Ref dstRef0 = new AreaRefImpl(dsttRow, lCol0, dstbRow, rCol0, dstSheet);
					copyRef(srcRef, 1, 1, dstRef0, pasteType, pasteOp, skipBlanks, false, last, all);
					lCol0 = rCol0 + 1;
				}
			}
			dsttRow = dstbRow + 1;
			dstbRow += srcRowCount;
		}
	}
	
	private void colCopyRefs(Map<Integer, Ref> srcRefs, int srcColCount, int srcRowCount, int colRepeat, int rowRepeat, RefSheet dstSheet, int lCol, int rCol, int tRow, int pasteType, int pasteOp, boolean skipBlanks, boolean transpose, Set<Ref> last, Set<Ref> all) {
		int dstlCol = lCol;
		int dstrCol = rCol;
		for (int cr = colRepeat; cr > 0; --cr) {
			int tRow0 = tRow;
			for(int rr = rowRepeat; rr > 0; --rr) {
				for (Entry<Integer, Ref> srcEntry : srcRefs.entrySet()) {
					final Ref srcRef = srcEntry.getValue();
					int bRow0 = tRow0 + (transpose ? srcRef.getColumnCount() : srcRef.getRowCount()) - 1; 
					final Ref dstRef0 = new AreaRefImpl(tRow0, dstlCol, bRow0, dstrCol, dstSheet);
					copyRef(srcRef, 1, 1, dstRef0, pasteType, pasteOp, skipBlanks, false, last, all);
					tRow0 = bRow0 + 1;
				}
			}
			dstlCol += dstrCol + 1;
			dstrCol += srcColCount;
		}
	}
	
	private void copyRefs(boolean sameRow, SortedMap<Integer, Ref> srcRefs, int srcColCount, int srcRowCount, int colRepeat, int rowRepeat, Ref dstRef, int pasteType, int pasteOp, boolean skipBlanks, boolean transpose, Set<Ref> last, Set<Ref> all) {
		if (pasteType == Range.PASTE_COLUMN_WIDTHS) {
			final Integer lastKey = srcRefs.lastKey();
			final Ref srcRef = srcRefs.get(lastKey);
			final int srclCol = srcRef.getLeftCol();
			final Sheet srcSheet = BookHelper.getSheet(_sheet, srcRef.getOwnerSheet());
			final int widthRepeatCount = srcRef.getColumnCount();
			copyColumnWidths(srcSheet, widthRepeatCount, srclCol, dstRef.getOwnerSheet(), dstRef.getTopRow(), dstRef.getLeftCol(), 
					(transpose ? srcColCount : srcRowCount) * rowRepeat, (transpose ? srcRowCount : srcColCount) * colRepeat);
			return;
		}
		
		final RefSheet dstSheet = dstRef.getOwnerSheet();
		final int tRow = dstRef.getTopRow();
		final int lCol = dstRef.getLeftCol();
		if (!transpose) {
			final int bRow = tRow + srcRowCount - 1;
			final int rCol = lCol + srcColCount - 1;
			if (sameRow) {
				rowCopyRefs(srcRefs, srcColCount, srcRowCount, colRepeat, rowRepeat, dstSheet, tRow, bRow, lCol, pasteType, pasteOp, skipBlanks, transpose, last, all);
			} else { //sameCol
				colCopyRefs(srcRefs, srcColCount, srcRowCount, colRepeat, rowRepeat, dstSheet, lCol, rCol, tRow, pasteType, pasteOp, skipBlanks, transpose, last, all);
			}
		} else { //row -> column, column -> row
			final int bRow = tRow + srcColCount - 1;
			final int rCol = lCol + srcRowCount - 1;
			if (sameRow) {
				colCopyRefs(srcRefs, srcRowCount, srcColCount, colRepeat, rowRepeat, dstSheet, lCol, rCol, tRow, pasteType, pasteOp, skipBlanks, transpose, last, all);
			} else { //sameCol
				rowCopyRefs(srcRefs, srcRowCount, srcColCount, colRepeat, rowRepeat, dstSheet, tRow, bRow, lCol, pasteType, pasteOp, skipBlanks, transpose, last, all);
			}
		}
	}
	
	private void copy(Ref srcRef, int srcColCount, int srcRowCount, Range dstRange, Ref dstRef, int pasteType, int pasteOp, boolean skipBlanks, boolean transpose, Set<Ref> last, Set<Ref> all) {
		if (pasteType == Range.PASTE_COLUMN_WIDTHS) { //ignore transpose in such case when only one srcRef
			final int lCol = srcRef.getLeftCol();
			final int rCol = srcRef.getRightCol();
			for (int j = lCol; j <= rCol; ++j) {
				final int char256 = _sheet.getColumnWidth(j);
				dstRange.setColumnWidth(char256);
			}
		}
		final int dstColCount = transpose ? dstRef.getRowCount() : dstRef.getColumnCount();
		final int dstRowCount = transpose ? dstRef.getColumnCount() : dstRef.getRowCount();
		
		if ((dstRowCount % srcRowCount) == 0 && (dstColCount % srcColCount) == 0) {
			copyRef(srcRef, dstColCount/srcColCount, dstRowCount/srcRowCount, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
		} else if (dstColCount == 1 && (dstRowCount % srcRowCount) == 0) {
			copyRef(srcRef, 1, dstRowCount/srcRowCount, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
		} else if (dstRowCount == 1 && (dstColCount % srcColCount) == 0) {
			copyRef(srcRef, dstColCount/srcColCount, 1, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
		} else {
			copyRef(srcRef, 1, 1, dstRef, pasteType, pasteOp, skipBlanks, transpose, last, all);
		}
	}

	private void copyColumnWidths(Sheet srcSheet, int widthRepeatCount, int srclCol, RefSheet dstRefSheet, int dsttRow, int dstlCol, int dstRowCount, int dstColCount) {
		final int dstbRow = dsttRow + dstRowCount - 1;
		final int dstrCol = dstlCol + dstColCount - 1;
		final Sheet dstSheet = BookHelper.getSheet(_sheet, dstRefSheet);
		for (int count = 0; count < dstColCount; ++count) {
			final int dstCol = dstlCol + count;
			final int srcCol = srclCol + count % widthRepeatCount; 
			final int char256 = srcSheet.getColumnWidth(srcCol);
			BookHelper.setColumnWidth(dstSheet, dstCol, dstCol, char256);
		}
		final Book book = (Book) dstSheet.getWorkbook();
		final Set<Ref> affected = new HashSet<Ref>();
		affected.add(new AreaRefImpl(dsttRow, dstlCol, dstbRow, dstrCol, dstRefSheet));
		BookHelper.notifySizeChanges(book, affected);
	}
	
	private void copyRef(Ref srcRef, int colRepeat, int rowRepeat, Ref dstRef, int pasteType, int pasteOp, boolean skipBlanks, boolean transpose, Set<Ref> last, Set<Ref> all) {
		if (pasteType == Range.PASTE_COLUMN_WIDTHS) {
			final int srcRowCount = srcRef.getRowCount();
			final int srcColCount = srcRef.getColumnCount();
			final int srclCol = srcRef.getLeftCol();
			final Sheet srcSheet = BookHelper.getSheet(_sheet, srcRef.getOwnerSheet());
			copyColumnWidths(srcSheet, srcColCount, srclCol, dstRef.getOwnerSheet(), dstRef.getTopRow(), dstRef.getLeftCol(), 
					(transpose ? srcColCount : srcRowCount) * rowRepeat, (transpose ? srcRowCount : srcColCount) * colRepeat);
			return;
		}

		final int tRow = srcRef.getTopRow();
		final int lCol = srcRef.getLeftCol();
		final int bRow = srcRef.getBottomRow();
		final int rCol = srcRef.getRightCol();
		final RefSheet dstRefsheet = dstRef.getOwnerSheet();
		final Sheet srcSheet = BookHelper.getSheet(_sheet, srcRef.getOwnerSheet());
		final Sheet dstSheet = BookHelper.getSheet(_sheet, dstRefsheet);
		
		if (!transpose) {
			int dstRow = dstRef.getTopRow(); 
			for(int rr = rowRepeat; rr > 0; --rr) {
				for(int srcRow = tRow; srcRow <= bRow; ++srcRow, ++dstRow) {
					int dstCol= dstRef.getLeftCol();
					for (int cr = colRepeat; cr > 0; --cr) {
						for (int srcCol = lCol; srcCol <= rCol; ++srcCol, ++dstCol) {
							final Cell cell = BookHelper.getCell(srcSheet, srcRow, srcCol);
							final Set<Ref>[] refs = (cell != null) ? 
								skipBlanks && cell.getCellType() == Cell.CELL_TYPE_BLANK ? null : BookHelper.copyCell(cell, dstSheet, dstRow, dstCol, pasteType, pasteOp):
								skipBlanks ? null : BookHelper.removeCell(dstSheet, dstRow, dstCol);
							if (refs != null) {
								last.addAll(refs[0]);
								all.addAll(refs[1]);
							}
						}
					}
				}
			}
		} else { //row -> column, column -> row
			int dstCol = dstRef.getLeftCol(); 
			for(int rr = rowRepeat; rr > 0; --rr) {
				for(int srcRow = tRow; srcRow <= bRow; ++srcRow, ++dstCol) {
					int dstRow = dstRef.getTopRow();
					for (int cr = colRepeat; cr > 0; --cr) {
						for (int srcCol = lCol; srcCol <= rCol; ++srcCol, ++dstRow) {
							final Cell cell = BookHelper.getCell(srcSheet, srcRow, srcCol);
							final Set<Ref>[] refs = (cell != null) ? 
								skipBlanks && cell.getCellType() == Cell.CELL_TYPE_BLANK ? null : BookHelper.copyCell(cell, dstSheet, dstRow, dstCol, pasteType, pasteOp):
								skipBlanks ? null : BookHelper.removeCell(dstSheet, dstRow, dstCol);
							if (refs != null) {
								last.addAll(refs[0]);
								all.addAll(refs[1]);
							}
						}
					}
				}
			}
		}
		all.add(dstRef);
	}
	
	@Override
	public void setBorders(short borderIndex, BorderStyle lineStyle, String color) {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final int bRow = ref.getBottomRow();
			final int rCol = ref.getRightCol();
			final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
			final Set<Ref> all = BookHelper.setBorders(sheet, tRow, lCol, bRow, rCol, borderIndex, lineStyle, color);
			if (all != null) {
				final Book book = (Book) _sheet.getWorkbook();
				BookHelper.notifyCellChanges(book, all);
			}
		}
	}
	
	@Override
	public void setColumnWidth(int char256) {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final int lCol = ref.getLeftCol();
			final int rCol = ref.getRightCol();
			final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
			final Set<Ref> all = BookHelper.setColumnWidth(sheet, lCol, rCol, char256);
			if (all != null) {
				final Book book = (Book) _sheet.getWorkbook();
				BookHelper.notifySizeChanges(book, all);
			}
		}
	}
	
	@Override
	public void setRowHeight(int points) {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final int tRow = ref.getTopRow();
			final int bRow = ref.getBottomRow();
			final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
			final Set<Ref> all = BookHelper.setRowHeight(sheet, tRow, bRow, (short) (points * 20)); //in twips
			if (all != null) {
				final Book book = (Book) _sheet.getWorkbook();
				BookHelper.notifySizeChanges(book, all);
			}
		}
	}
	
	@Override
	public void move(int nRow, int nCol) {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final RefBook refBook = refSheet.getOwnerBook();
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final int bRow = ref.getBottomRow();
			final int rCol = ref.getRightCol();
			final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
			final ChangeInfo info = BookHelper.moveRange(sheet, tRow, lCol, bRow, rCol, nRow, nCol);
			notifyMergeChange(refBook, info, ref, SSDataEvent.ON_CONTENTS_CHANGE, SSDataEvent.MOVE_NO);
		}
	}
	
	@Override
	public void setStyle(CellStyle style) {
		if (_refs != null && !_refs.isEmpty()) {
			final Set<Ref> all = new HashSet<Ref>();
			for (Ref ref : _refs) {
				final RefSheet refSheet = ref.getOwnerSheet();
				final int tRow = ref.getTopRow();
				final int lCol = ref.getLeftCol();
				final int bRow = ref.getBottomRow();
				final int rCol = ref.getRightCol();
				final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
				final Set<Ref> refs = BookHelper.setCellStyle(sheet, tRow, lCol, bRow, rCol, style);
				all.addAll(refs);
			}
			if (!all.isEmpty()) {
				final Book book = (Book) _sheet.getWorkbook();
				BookHelper.notifyCellChanges(book, all);
			}
		}
	}
	
	@Override
	public void autoFill(Range dstRange, int fillType) {
		if (_refs != null && !_refs.isEmpty() && !dstRange.getRefs().isEmpty()) {
			//destination range allow only one contiguous reference
			if (dstRange.getRefs().size() > 1) {
				throw new UiException("Command cannot be used on multiple selections");
			}
			final Ref srcRef = _refs.iterator().next();
			final Ref dstRef = dstRange.getRefs().iterator().next();
			fillRef(srcRef, dstRef, fillType);
		}
	}
	
	@Override
	public void clearContents() {
		//TODO not implemented yet
		if (_refs != null && !_refs.isEmpty()) {
			final Ref ref = _refs.iterator().next();
			final RefSheet refSheet = ref.getOwnerSheet();
			final int tRow = ref.getTopRow();
			final int lCol = ref.getLeftCol();
			final int bRow = ref.getBottomRow();
			final int rCol = ref.getRightCol();
			final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
			clearContents(sheet, tRow, lCol, bRow, rCol);
		}
	}
	
	private void clearContents(Sheet sheet, int tRow, int lCol, int bRow, int rCol) {
		final Set<Ref> last = new HashSet<Ref>();
		final Set<Ref> all = new HashSet<Ref>();
		for(int r = tRow; r <= bRow; ++r) {
			for(int c = lCol; c <= rCol; ++c) {
				final Set<Ref>[] refs = BookHelper.clearCell(sheet, r, c);
				if (refs != null) {
					last.addAll(refs[0]);
					all.addAll(refs[1]);
				}
			}
		}
		final Book book = (Book) sheet.getWorkbook();
		all.add(new AreaRefImpl(tRow, lCol, bRow, rCol, BookHelper.getRefSheet(book, sheet)));
		BookHelper.reevaluateAndNotify(book, last, all);
	}
	
	private void fillRef(Ref srcRef, Ref dstRef, int fillType) {
		final Set<Ref>[] refs = BookHelper.fill(_sheet, srcRef, dstRef, fillType);
		if (refs != null) {
			final Book book = (Book) _sheet.getWorkbook();
			BookHelper.reevaluateAndNotify(book, refs[0], refs[1]);
		}
	}

	@Override
	public void fillDown() {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref srcRef = _refs.iterator().next();
			final Ref dstRef = new AreaRefImpl(srcRef.getTopRow(), srcRef.getLeftCol(), srcRef.getTopRow(), srcRef.getRightCol(), srcRef.getOwnerSheet());
			fillRef(srcRef, dstRef, Range.FILL_COPY);
		}
	}
	
	@Override
	public void fillLeft() {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref srcRef = _refs.iterator().next();
			final Ref dstRef = new AreaRefImpl(srcRef.getTopRow(), srcRef.getRightCol(), srcRef.getBottomRow(), srcRef.getRightCol(), srcRef.getOwnerSheet());
			fillRef(srcRef, dstRef, Range.FILL_COPY);
		}
	}

	@Override
	public void fillRight() {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref srcRef = _refs.iterator().next();
			final Ref dstRef = new AreaRefImpl(srcRef.getTopRow(), srcRef.getLeftCol(), srcRef.getBottomRow(), srcRef.getLeftCol(), srcRef.getOwnerSheet());
			fillRef(srcRef, dstRef, Range.FILL_COPY);
		}
	}

	@Override
	public void fillUp() {
		if (_refs != null && !_refs.isEmpty()) {
			final Ref srcRef = _refs.iterator().next();
			final Ref dstRef = new AreaRefImpl(srcRef.getBottomRow(), srcRef.getLeftCol(), srcRef.getBottomRow(), srcRef.getRightCol(), srcRef.getOwnerSheet());
			fillRef(srcRef, dstRef, Range.FILL_COPY);
		}
	}
	
	@Override
	public void setHidden(boolean hidden) {
		if (_refs != null && !_refs.isEmpty()) {
			final Set<Ref> all = new HashSet<Ref>();
			for (Ref ref : _refs) {
				if (ref.isWholeRow()) {
					final RefSheet refSheet = ref.getOwnerSheet();
					final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
					final int tRow = ref.getTopRow();
					final int bRow = ref.getBottomRow();
					final Set<Ref> refs = BookHelper.setRowsHidden(sheet, tRow, bRow, hidden);
					all.addAll(refs);
				} else if (ref.isWholeColumn()) {
					final RefSheet refSheet = ref.getOwnerSheet();
					final Sheet sheet = BookHelper.getSheet(_sheet, refSheet);
					final int lCol = ref.getLeftCol();
					final int rCol = ref.getRightCol();
					final Set<Ref> refs = BookHelper.setColumnsHidden(sheet, lCol, rCol, hidden);
					all.addAll(refs);
				}
			}
			if (!all.isEmpty()) {
				final Book book = (Book) _sheet.getWorkbook();
				BookHelper.notifySizeChanges(book, all);
			}
		}
	}
	
	@Override
	public void setDisplayGridlines(boolean show) {
		final Book book = (Book)_sheet.getWorkbook();
		final int index1= book.getSheetIndex(_sheet);
		final int index2 = book.getSheetIndex(_lastSheet);
		final Set<Ref> all = new HashSet<Ref>(); 
		for(int j = index1; j <= index2; ++j) {
			final Sheet sheet = book.getSheetAt(j);
			final boolean old = sheet.isDisplayGridlines();
			if (old != show) {
				sheet.setDisplayGridlines(show);
				//sheet is important, row, column is not in this event
				all.add(new CellRefImpl(0, 0, BookHelper.getRefSheet(book, sheet))); 
			}
		}
		if (!all.isEmpty()) {
			BookHelper.notifyGridlines(book, all, show);
		}
	}
}
