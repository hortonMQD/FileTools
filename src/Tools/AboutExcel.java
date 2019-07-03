package Tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pojo.FileInfo;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档
 */
public class AboutExcel {
	public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

	/**
	 * 这是一个通用的方法，可以将放置在JAVA集合中的数据以EXCEL的形式输出到IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件中
	 */
	public void exportExcel(String title, String[] headers,List<FileInfo> dataset, OutputStream out) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 30);

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);//将焦点放在表格标题行的第i列
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);//设置文本为表格列名数组的第i个
			cell.setCellValue(text);	//设置焦点所在的表格文本
			
		}
		
		// 遍历集合数据，产生数据行
		for(int i = 0;i<dataset.size();i++) {
			
			row = sheet.createRow(i+1);// 产生表格数据行
			FileInfo t = dataset.get(i);
			
			
			String[] texts = {null,null,null,null,null,null,null};
			texts[0] = t.getId();
			texts[1] = t.getFileName();
			texts[2] = t.getFilePath();
			
			
			for (short j = 0; j < headers.length; j++) {
				HSSFCell cell = row.createCell(j);//将焦点放在表格标题行的第i列
				HSSFRichTextString text = new HSSFRichTextString(texts[j]);//设置文本为表格数组的第i个
				cell.setCellValue(text);	//设置焦点所在的表格文本
				
			}
	}
		try {
			workbook.write(out);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建一个工作表
	 * @param file ：传入excel文件
	 * @return：返回一个工作表
	 * @throws IOException
	 */
	 private static HSSFWorkbook readExcelFile(File file) throws IOException {
	       return new HSSFWorkbook(new FileInputStream(file));
	    }
	
	 
	 
	 
	
	 
	
	/**
	 * 将传入的数据导出到excel表
	 * @param dataset：传入的数据集合
	 * @param filePath:传入文件导出路径
	 */
	public void exportText(List<FileInfo> dataset,String filePath) {
		AboutExcel ex = new AboutExcel();
		String[] headers = { "序号", "文件名", "存储地址"};		
		
		try {
			//File file = new File();
			OutputStream out = new FileOutputStream(filePath+"/文件信息.xls");//创建一个文件输出流
			
			ex.exportExcel("学生基本信息",headers, dataset, out);
			//调用导出方法，传入工作表名称，数据列名，信息集合，文件输出流
			out.close();//关闭文件输出流
			JOptionPane.showMessageDialog(null, "导出成功!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
