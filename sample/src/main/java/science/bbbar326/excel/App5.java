package science.bbbar326.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class App5 {

	private static final String TARGET_SHEET_NAME = "students";
	private static final int HEADER_ROW_NO = 2;
	private static final int SEI_CLUMUN_NO = 0;
	private static final int MEI_CLUMUN_NO = 1;

	public static void main(String[] args) throws Exception {

		String filePath = "src/main/resources/development/data.xlsx";

		if (args != null && args.length != 0) {
			filePath = args[0];
		}

		App5 app = new App5();
		app.execute(filePath);
	}

	private void execute(String filePath) throws Exception {
		InputStream inputStream = new FileInputStream(filePath);
		Workbook workbook = WorkbookFactory.create(inputStream);

		Sheet sheet = workbook.getSheet(TARGET_SHEET_NAME);

		Iterator<Row> rows = sheet.rowIterator();

		// -----------------------------------------------
		// ヘッダー
		// -----------------------------------------------
		List<Cell> header = new ArrayList<>();

		// -----------------------------------------------
		// 全件データ
		// -----------------------------------------------
		List<List<Cell>> datalist = new ArrayList<>();

		while (rows.hasNext()) {
			Row row = rows.next();
			Iterator<Cell> cells = row.cellIterator();

			// -----------------------------------------------
			// 各データ
			// -----------------------------------------------
			List<Cell> data = new ArrayList<>();

			while (cells.hasNext()) {
				Cell cell = cells.next();

				if (cell.getAddress().getRow() == HEADER_ROW_NO) {
					header.add(cell);
				}
				if (cell.getAddress().getRow() > HEADER_ROW_NO) {
					data.add(cell);
				}
			}

			if (data.size() != 0) {
				datalist.add(data);
			}

		}

		// -----------------------------------------------
		// 扱いやすいようデータを整理
		// -----------------------------------------------

		List<PointInfo> list = new ArrayList<>();

		for (int i = 0; i < datalist.size(); i++) {
			PointInfo info = new PointInfo();

			List<Cell> celllist = datalist.get(i);

			// -----------------------------------------------
			// 名前（姓 名）
			// -----------------------------------------------
			info.name.put((header.get(SEI_CLUMUN_NO).toString()), celllist.get(SEI_CLUMUN_NO).toString());
			info.name.put((header.get(MEI_CLUMUN_NO).toString()), celllist.get(MEI_CLUMUN_NO).toString());

			// -----------------------------------------------
			// 得点
			// -----------------------------------------------
			for (int k = 0; k < celllist.size(); k++) {
				if (k <= MEI_CLUMUN_NO) {
					continue;
				}
				info.points.put(header.get(k).toString(), (int) celllist.get(k).getNumericCellValue());
			}

			list.add(info);
		}

		// -----------------------------------------------
		// 出力
		// -----------------------------------------------
		output(list);

	}

	/**
	 * 取得した得点情報を出力します。
	 * */
	protected void output(List<PointInfo> list) {
		// -----------------------------------------------
		// ヘッダー
		// -----------------------------------------------
		System.out.print("名前");
		System.out.print("\t");
		list.get(0).points.keySet().forEach(s -> System.out.print(s + "\t"));
		System.out.print("合計点");
		System.out.print("\t");
		System.out.println();

		// -----------------------------------------------
		// データ
		// -----------------------------------------------
		for (PointInfo e : list) {
			// -----------------------------------------------
			// 名前（姓 名）
			// -----------------------------------------------
			e.name.values().forEach(s -> System.out.print(s + " "));
			System.out.print("\t");

			// -----------------------------------------------
			// 得点
			// -----------------------------------------------
			e.points.values().forEach(s -> System.out.print(s + "\t"));

			// -----------------------------------------------
			// 合計点
			// -----------------------------------------------
			System.out.print(e.getSumPoint());

			System.out.println();
		}
	}

	/**
	 * 得点情報
	 * */
	class PointInfo {
		/** 名前（姓名） */
		Map<String, String> name;

		/** 得点（教科,得点） */
		Map<String, Integer> points;

		/**
		 * コンストラクタ
		 * */
		PointInfo() {
			name = new LinkedHashMap<>();
			points = new LinkedHashMap<>();
		}

		/**
		 * 合計点を取得する
		 * */
		int getSumPoint() {
			int sum = 0;
			for (Integer e : points.values()) {
				sum += e;
			}
			return sum;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}

	}

}
