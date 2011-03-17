/**
 * 
 */
package org.zkoss.zss.model.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.zkoss.poi.ss.formula.ptg.Ptg;
import org.zkoss.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.zkoss.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.poi.hssf.util.HSSFColor;
import org.zkoss.poi.ss.formula.FormulaRenderer;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellValue;
import org.zkoss.poi.ss.usermodel.FormulaEvaluator;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.poi.ss.usermodel.Workbook;
import org.zkoss.util.resource.ClassLocator;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.impl.BookHelper;
import org.zkoss.zss.model.impl.ExcelImporter;
import org.zkoss.zss.model.impl.HSSFBookImpl;

/**
 * Insert a column and check if the formula still work
 * @author henrichen
 */
public class Book06XlsInsertColumnsTest {
	private Book _workbook;
	private FormulaEvaluator _evaluator;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		final String filename = "Book6.xls";
		final InputStream is = new ClassLocator().getResourceAsStream(filename);
		_workbook = new ExcelImporter().imports(is, filename);
		assertTrue(_workbook instanceof Book);
		assertTrue(_workbook instanceof HSSFBookImpl);
		assertTrue(_workbook instanceof HSSFWorkbook);
		assertEquals(filename, ((Book)_workbook).getBookName());
		assertEquals("Sheet1", _workbook.getSheetName(0));
		assertEquals("Sheet2", _workbook.getSheetName(1));
		assertEquals("Sheet3", _workbook.getSheetName(2));
		assertEquals(0, _workbook.getSheetIndex("Sheet1"));
		assertEquals(1, _workbook.getSheetIndex("Sheet2"));
		assertEquals(2, _workbook.getSheetIndex("Sheet3"));

		_evaluator = ((Book)_workbook).getFormulaEvaluator();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		_workbook = null;
		_evaluator = null;
	}
	
	@Test
	public void testInsertColumnC() {
		Worksheet sheet1 = _workbook.getWorksheet("Sheet1");
		Row row1 = sheet1.getRow(0);
		Row row2 = sheet1.getRow(1);
		Row row3 = sheet1.getRow(2);
		Row row4 = sheet1.getRow(3);
		Row row5 = sheet1.getRow(4);
		Row row6 = sheet1.getRow(5);
		assertEquals(1, row1.getCell(5).getNumericCellValue(), 0.0000000000000001); //F1: 1
		assertEquals(2, row2.getCell(5).getNumericCellValue(), 0.0000000000000001);	//F2: 2
		assertEquals(3, row3.getCell(5).getNumericCellValue(), 0.0000000000000001); //F3: 3
		assertEquals(7, row4.getCell(0).getNumericCellValue(), 0.0000000000000001); //A4: 7
		assertEquals(9, row1.getCell(2).getNumericCellValue(), 0.0000000000000001); //C1: 9
		assertEquals(11, row1.getCell(3).getNumericCellValue(), 0.0000000000000001); //D1: 11
		
		//A1: =SUM(F1:F3)
		Cell cellA1 = row1.getCell(0); //A1: =SUM(F1:F3)
		CellValue valueA1 = _evaluator.evaluate(cellA1);
		assertEquals(6, valueA1.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA1.getCellType());
		testToFormulaString(cellA1, "SUM(F1:F3)");
		
		//A2: =SUM(D2:IV2) IV: 256
		Cell cellA2 = row2.getCell(0); //A2: =SUM(D2:IV2) IV: 256
		CellValue valueA2 = _evaluator.evaluate(cellA2);
		assertEquals(2, valueA2.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA2.getCellType());
		testToFormulaString(cellA2, "SUM(D2:IV2)");

		//A3: =SUM(IU3:IV3) IU: 255, IV: 256
		Cell cellA3 = row3.getCell(0); //A3: =SUM(IU3:IV3) 
		CellValue valueA3 = _evaluator.evaluate(cellA3);
		assertEquals(0, valueA3.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA3.getCellType());
		testToFormulaString(cellA3, "SUM(IU3:IV3)");
		
		//A5: =SUM(C1:D1)
		Cell cellA5 = row5.getCell(0);
		CellValue valueA5 = _evaluator.evaluate(cellA5);
		assertEquals(20, valueA5.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA5.getCellType());
		testToFormulaString(cellA5, "SUM(C1:D1)");
		
		//A6: =SUM(B1:D1)
		Cell cellA6 = row6.getCell(0);
		CellValue valueA6 = _evaluator.evaluate(cellA6);
		assertEquals(20, valueA6.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA6.getCellType());
		testToFormulaString(cellA6, "SUM(B1:D1)");
		
		//H1: =SUM(F1:F3)
		Cell cellH1 = row1.getCell(7); 
		CellValue valueH1 = _evaluator.evaluate(cellH1);
		assertEquals(6, valueH1.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueH1.getCellType());
		testToFormulaString(cellH1, "SUM(F1:F3)");

		//H4: =A4
		Cell cellH4 = row4.getCell(7); 
		CellValue valueH4 = _evaluator.evaluate(cellH4);
		assertEquals(7, valueH4.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueH4.getCellType());
		testToFormulaString(cellH4, "A4");
		
		//Insert before column C
		BookHelper.insertColumns(sheet1, 2, 1, Range.FORMAT_LEFTABOVE);
		_evaluator.notifySetFormula(cellA1);
		_evaluator.notifySetFormula(cellA2);
		_evaluator.notifySetFormula(cellA3);
		_evaluator.notifySetFormula(cellA5);
		_evaluator.notifySetFormula(cellA6);
		
		assertEquals(sheet1.getColumnWidth(1), sheet1.getColumnWidth(2)); //column c width == column b width
		assertEquals(HSSFColor.RED.index, row1.getCell(1).getCellStyle().getFillForegroundColor());
		assertEquals(HSSFColor.YELLOW.index, row1.getCell(1).getCellStyle().getFillBackgroundColor());
		assertEquals(HSSFColor.RED.index, row1.getCell(2).getCellStyle().getFillForegroundColor());
		assertEquals(HSSFColor.YELLOW.index, row1.getCell(2).getCellStyle().getFillBackgroundColor());
		
		assertEquals(1, row1.getCell(6).getNumericCellValue(), 0.0000000000000001); //G1: 1
		assertEquals(2, row2.getCell(6).getNumericCellValue(), 0.0000000000000001);	//G2: 2
		assertEquals(3, row3.getCell(6).getNumericCellValue(), 0.0000000000000001); //G3: 3
		assertEquals(7, row4.getCell(0).getNumericCellValue(), 0.0000000000000001); //A4: 7

		//A1: =SUM(G1:G3)
		valueA1 = _evaluator.evaluate(cellA1);
		assertEquals(6, valueA1.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA1.getCellType());
		testToFormulaString(cellA1, "SUM(G1:G3)");
		
		//A2: =SUM(E2:IV2) IV: 256
		valueA2 = _evaluator.evaluate(cellA2);
		assertEquals(2, valueA2.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA2.getCellType());
		testToFormulaString(cellA2, "SUM(E2:IV2)");

		//A3: =SUM(IV3:IV3) IV: 256
		cellA3 = row3.getCell(0); //A3: =SUM(IU3:IV3) 
		valueA6 = _evaluator.evaluate(cellA3);
		assertEquals(0, valueA6.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA6.getCellType());
		testToFormulaString(cellA3, "SUM(IV3:IV3)");
		
		//A5: =SUM(C1:D1) -> =SUM(D1:E1)
		cellA5 = row5.getCell(0);
		valueA5 = _evaluator.evaluate(cellA5);
		assertEquals(20, valueA5.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA5.getCellType());
		testToFormulaString(cellA5, "SUM(D1:E1)");
		
		//A6: =SUM(B1:D1) -> =SUM(B1:E1)
		cellA6 = row6.getCell(0);
		valueA6 = _evaluator.evaluate(cellA6);
		assertEquals(20, valueA6.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA6.getCellType());
		testToFormulaString(cellA6, "SUM(B1:E1)");
		
		//I1: =SUM(G1:G3)
		Cell cellI1 = row1.getCell(8); 
		CellValue valueI1 = _evaluator.evaluate(cellI1);
		assertEquals(6, valueI1.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueI1.getCellType());
		testToFormulaString(cellI1, "SUM(G1:G3)");

		//I4: =A4
		Cell cellI4 = row4.getCell(8); 
		CellValue valueI4 = _evaluator.evaluate(cellI4);
		assertEquals(7, valueI4.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueI4.getCellType());
		testToFormulaString(cellI4, "A4");
		
	}
	
	@Test
	public void testInsertRangeC1_C4() {
		Worksheet sheet1 = _workbook.getWorksheet("Sheet1");
		Row row1 = sheet1.getRow(0);
		Row row2 = sheet1.getRow(1);
		Row row3 = sheet1.getRow(2);
		Row row4 = sheet1.getRow(3);
		Row row5 = sheet1.getRow(4);
		Row row6 = sheet1.getRow(5);
		assertEquals(1, row1.getCell(5).getNumericCellValue(), 0.0000000000000001); //F1: 1
		assertEquals(2, row2.getCell(5).getNumericCellValue(), 0.0000000000000001);	//F2: 2
		assertEquals(3, row3.getCell(5).getNumericCellValue(), 0.0000000000000001); //F3: 3
		assertEquals(7, row4.getCell(0).getNumericCellValue(), 0.0000000000000001); //A4: 7
		assertEquals(9, row1.getCell(2).getNumericCellValue(), 0.0000000000000001); //C1: 9
		assertEquals(11, row1.getCell(3).getNumericCellValue(), 0.0000000000000001); //D1: 11
		
		//A1: =SUM(F1:F3)
		Cell cellA1 = row1.getCell(0); //A1: =SUM(F1:F3)
		CellValue valueA1 = _evaluator.evaluate(cellA1);
		assertEquals(6, valueA1.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA1.getCellType());
		testToFormulaString(cellA1, "SUM(F1:F3)");
		
		//A2: =SUM(D2:IV2) IV: 256
		Cell cellA2 = row2.getCell(0); //A2: =SUM(D2:IV2) IV: 256
		CellValue valueA2 = _evaluator.evaluate(cellA2);
		assertEquals(2, valueA2.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA2.getCellType());
		testToFormulaString(cellA2, "SUM(D2:IV2)");

		//A3: =SUM(IU3:IV3) IU: 255, IV: 256
		Cell cellA3 = row3.getCell(0); //A3: =SUM(IU3:IV3) 
		CellValue valueA3 = _evaluator.evaluate(cellA3);
		assertEquals(0, valueA3.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA3.getCellType());
		testToFormulaString(cellA3, "SUM(IU3:IV3)");
		
		//A5: =SUM(C1:D1)
		Cell cellA5 = row5.getCell(0);
		CellValue valueA5 = _evaluator.evaluate(cellA5);
		assertEquals(20, valueA5.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA5.getCellType());
		testToFormulaString(cellA5, "SUM(C1:D1)");
		
		//A6: =SUM(B1:D1)
		Cell cellA6 = row6.getCell(0);
		CellValue valueA6 = _evaluator.evaluate(cellA6);
		assertEquals(20, valueA6.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA6.getCellType());
		testToFormulaString(cellA6, "SUM(B1:D1)");
		
		//H1: =SUM(F1:F3)
		Cell cellH1 = row1.getCell(7); 
		CellValue valueH1 = _evaluator.evaluate(cellH1);
		assertEquals(6, valueH1.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueH1.getCellType());
		testToFormulaString(cellH1, "SUM(F1:F3)");

		//H4: =A4
		Cell cellH4 = row4.getCell(7); 
		CellValue valueH4 = _evaluator.evaluate(cellH4);
		assertEquals(7, valueH4.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueH4.getCellType());
		testToFormulaString(cellH4, "A4");
		
		//Insert C1:C4
		BookHelper.insertRange(sheet1, 0, 2, 3, 2, true, Range.FORMAT_LEFTABOVE);
		_evaluator.notifySetFormula(cellA1);
		_evaluator.notifySetFormula(cellA2);
		_evaluator.notifySetFormula(cellA3);
		_evaluator.notifySetFormula(cellA5);
		_evaluator.notifySetFormula(cellA6);
		
		assertEquals(1, row1.getCell(6).getNumericCellValue(), 0.0000000000000001); //G1: 1
		assertEquals(2, row2.getCell(6).getNumericCellValue(), 0.0000000000000001);	//G2: 2
		assertEquals(3, row3.getCell(6).getNumericCellValue(), 0.0000000000000001); //G3: 3
		assertEquals(7, row4.getCell(0).getNumericCellValue(), 0.0000000000000001); //A4: 7
		assertEquals(9, row1.getCell(3).getNumericCellValue(), 0.0000000000000001); //D1: 9
		assertEquals(11, row1.getCell(4).getNumericCellValue(), 0.0000000000000001); //E1: 11

		//A1: =SUM(G1:G3)
		valueA1 = _evaluator.evaluate(cellA1);
		assertEquals(6, valueA1.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA1.getCellType());
		testToFormulaString(cellA1, "SUM(G1:G3)");
		
		//A2: =SUM(E2:IV2) IV: 256
		valueA2 = _evaluator.evaluate(cellA2);
		assertEquals(2, valueA2.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA2.getCellType());
		testToFormulaString(cellA2, "SUM(E2:IV2)");

		//A3: =SUM(IV3:IV3) IV: 256
		cellA3 = row3.getCell(0); //A3: =SUM(IU3:IV3) 
		valueA6 = _evaluator.evaluate(cellA3);
		assertEquals(0, valueA6.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA6.getCellType());
		testToFormulaString(cellA3, "SUM(IV3:IV3)");
		
		//A5: =SUM(C1:D1) -> =SUM(D1:E1)
		cellA5 = row5.getCell(0);
		valueA5 = _evaluator.evaluate(cellA5);
		assertEquals(20, valueA5.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA5.getCellType());
		testToFormulaString(cellA5, "SUM(D1:E1)");
		
		//A6: =SUM(B1:D1) -> =SUM(B1:E1)
		cellA6 = row6.getCell(0);
		valueA6 = _evaluator.evaluate(cellA6);
		assertEquals(20, valueA6.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueA6.getCellType());
		testToFormulaString(cellA6, "SUM(B1:E1)");
		
		//I1: =SUM(G1:G3)
		Cell cellI1 = row1.getCell(8); 
		CellValue valueI1 = _evaluator.evaluate(cellI1);
		assertEquals(6, valueI1.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueI1.getCellType());
		testToFormulaString(cellI1, "SUM(G1:G3)");

		//I4: =A4
		Cell cellI4 = row4.getCell(8); 
		CellValue valueI4 = _evaluator.evaluate(cellI4);
		assertEquals(7, valueI4.getNumberValue(), 0.0000000000000001);
		assertEquals(Cell.CELL_TYPE_NUMERIC, valueI4.getCellType());
		testToFormulaString(cellI4, "A4");
		
	}
	
	private void testToFormulaString(Cell cell, String expect) {
		HSSFEvaluationWorkbook evalbook = HSSFEvaluationWorkbook.create((HSSFWorkbook)_workbook);
		Ptg[] ptgs = BookHelper.getCellPtgs(cell);
		final String formula = FormulaRenderer.toFormulaString(evalbook, ptgs);
		assertEquals(expect, formula);
	}
}
