package science.bbbar326.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class App5 {
	public static void main(String[] args) throws Exception {
		InputStream inputStream = new FileInputStream("src/main/resources/development/data.xlsx");
		Workbook workbook = WorkbookFactory.create(inputStream);

		// シート名がわかっている場合
		Sheet sheet = workbook.getSheet("students");

		// 全行を繰り返し処理する場合
		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = rows.next();
			// 全セルを繰り返し処理する場合
			Iterator<Cell> cells = row.cellIterator();
			while (cells.hasNext()) {
				Cell cell = cells.next();
				String stringValue = cell.toString();
				if (cell.getAddress().getColumn() == 1) {
					System.out.print(stringValue);
				} else {
					System.out.print(stringValue + "\t");
				}
			}
			System.out.println();
		}
	}
}
