/**
 * 
 */
package org.zkoss.zss.model.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.zkoss.poi.ss.formula.ptg.Ptg;
import org.zkoss.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.ss.formula.FormulaRenderer;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellValue;
import org.zkoss.poi.ss.usermodel.FormulaEvaluator;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.poi.ss.usermodel.Workbook;
import org.zkoss.poi.xssf.usermodel.XSSFEvaluationWorkbook;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.resource.ClassLocator;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;

/**
 * Insert a row and check if the formula still work
 * @author henrichen
 */
public class Book298XlsxNumberFormatTest {
	private Book _workbook;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		final String filename = "Book298NumberFormat.xlsx";
		final InputStream is = new ClassLocator().getResourceAsStream(filename);
		_workbook = new ExcelImporter().imports(is, filename);
		assertTrue(_workbook instanceof Book);
		assertTrue(_workbook instanceof XSSFBookImpl);
		assertTrue(_workbook instanceof XSSFWorkbook);
		assertEquals(filename, ((Book)_workbook).getBookName());
		assertEquals("Sheet1", _workbook.getSheetName(0));
		assertEquals("Sheet2", _workbook.getSheetName(1));
		assertEquals("Sheet3", _workbook.getSheetName(2));
		assertEquals(0, _workbook.getSheetIndex("Sheet1"));
		assertEquals(1, _workbook.getSheetIndex("Sheet2"));
		assertEquals(2, _workbook.getSheetIndex("Sheet3"));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		_workbook = null;
	}
	
	@Test
	public void test19() {
		Worksheet sheet1 = _workbook.getWorksheet("Sheet1");
		Range rng = Ranges.range(sheet1, "A1");
		rng.setEditText("19");
		assertEquals("19", rng.getText().toString()); //19
	}
	
	@Test
	public void test1234() {
		Worksheet sheet1 = _workbook.getWorksheet("Sheet1");
		Range rng = Ranges.range(sheet1, "A1");
		rng.setEditText("1234");
		assertEquals("1,234", rng.getText().toString()); //1,234
	}
	
	@Test
	public void test5678dot123() {
		Worksheet sheet1 = _workbook.getWorksheet("Sheet1");
		Range rng = Ranges.range(sheet1, "A1");
		rng.setEditText("5678.123");
		assertEquals("5,678.123", rng.getText().toString()); //5,678.123
	}
	
	@Test
	public void test1234567() {
		Worksheet sheet1 = _workbook.getWorksheet("Sheet1");
		Range rng = Ranges.range(sheet1, "A1");
		rng.setEditText("1234567");
		assertEquals("1,234,567", rng.getText().toString()); //1,234,567
	}
	@Test
	public void test123456dot89() {
		Worksheet sheet1 = _workbook.getWorksheet("Sheet1");
		Range rng = Ranges.range(sheet1, "A1");
		rng.setEditText("1234567.89");
		assertEquals("1,234,567.89", rng.getText().toString()); //1,234,567.89
	}
}
