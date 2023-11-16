package com.example.propyski4;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    // Метод для чтения данных из Excel файла
    public static String[][] readExcelFile(File file, int startRow, int endRow, int startCol, int endCol) throws IOException {
        Workbook workbook = WorkbookFactory.create(file);///////////////  at com.example.propyski4.ExcelReader.readExcelFile(ExcelReader.java:21)  at com.example.propyski4.MainActivity.addDataToTextView(MainActivity.java:573)
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Sheet sheet = workbook.getSheetAt(0);
        List<String[]> rows = new ArrayList<>();
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row != null) { // Проверка на null
                String[] cells = new String[endCol - startCol + 1];
                for (int j = startCol; j <= endCol; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) { // Проверка на null
                        String value;
                        if (cell.getCellType() == CellType.FORMULA) {
                            CellValue cellValue = evaluator.evaluate(cell);/////at com.example.propyski4.ExcelReader.readExcelFile(ExcelReader.java:34)
                            switch (cellValue.getCellType()) {
                                case NUMERIC:
                                    double numericValue = cellValue.getNumberValue();
                                    long roundedValue = Math.round(numericValue);
                                    value = Long.toString(roundedValue);
                                    break;
                                case STRING:
                                    value = cellValue.getStringValue();
                                    break;
                                default:
                                    value = cell.toString();
                            }
                        } else if (cell.getCellType() == CellType.NUMERIC) {
                            double numericValue = cell.getNumericCellValue();
                            long roundedValue = Math.round(numericValue);
                            value = Long.toString(roundedValue);
                        } else {
                            value = cell.toString();
                        }
                        cells[j - startCol] = value;
                    } else {
                        cells[j - startCol] = ""; // Если ячейка пуста, сохраняем пустую строку
                    }
            }
            rows.add(cells);
            }
        }
        workbook.close();
        String[][] data = new String[rows.size()][];
        data = rows.toArray(data);
        return data;
    }





}
