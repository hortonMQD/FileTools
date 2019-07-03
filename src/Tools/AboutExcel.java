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
 * ���ÿ�Դ���POI3.0.2��̬����EXCEL�ĵ�
 */
public class AboutExcel {
	public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

	/**
	 * ����һ��ͨ�õķ��������Խ�������JAVA�����е�������EXCEL����ʽ�����IO�豸��
	 * 
	 * @param title
	 *            ��������
	 * @param headers
	 *            ���������������
	 * @param dataset
	 *            ��Ҫ��ʾ�����ݼ���,������һ��Ҫ���÷���javabean������Ķ���
	 * @param out
	 *            ������豸�����������󣬿��Խ�EXCEL�ĵ������������ļ���
	 */
	public void exportExcel(String title, String[] headers,List<FileInfo> dataset, OutputStream out) {
		// ����һ��������
		HSSFWorkbook workbook = new HSSFWorkbook();
		// ����һ�����
		HSSFSheet sheet = workbook.createSheet(title);
		// ���ñ��Ĭ���п��Ϊ15���ֽ�
		sheet.setDefaultColumnWidth((short) 30);

		// ������������
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);//��������ڱ������еĵ�i��
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);//�����ı�Ϊ�����������ĵ�i��
			cell.setCellValue(text);	//���ý������ڵı���ı�
			
		}
		
		// �����������ݣ�����������
		for(int i = 0;i<dataset.size();i++) {
			
			row = sheet.createRow(i+1);// �������������
			FileInfo t = dataset.get(i);
			
			
			String[] texts = {null,null,null,null,null,null,null};
			texts[0] = t.getId();
			texts[1] = t.getFileName();
			texts[2] = t.getFilePath();
			
			
			for (short j = 0; j < headers.length; j++) {
				HSSFCell cell = row.createCell(j);//��������ڱ������еĵ�i��
				HSSFRichTextString text = new HSSFRichTextString(texts[j]);//�����ı�Ϊ�������ĵ�i��
				cell.setCellValue(text);	//���ý������ڵı���ı�
				
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
	 * ����һ��������
	 * @param file ������excel�ļ�
	 * @return������һ��������
	 * @throws IOException
	 */
	 private static HSSFWorkbook readExcelFile(File file) throws IOException {
	       return new HSSFWorkbook(new FileInputStream(file));
	    }
	
	 
	 
	 
	
	 
	
	/**
	 * ����������ݵ�����excel��
	 * @param dataset����������ݼ���
	 * @param filePath:�����ļ�����·��
	 */
	public void exportText(List<FileInfo> dataset,String filePath) {
		AboutExcel ex = new AboutExcel();
		String[] headers = { "���", "�ļ���", "�洢��ַ"};		
		
		try {
			//File file = new File();
			OutputStream out = new FileOutputStream(filePath+"/�ļ���Ϣ.xls");//����һ���ļ������
			
			ex.exportExcel("ѧ��������Ϣ",headers, dataset, out);
			//���õ������������빤�������ƣ�������������Ϣ���ϣ��ļ������
			out.close();//�ر��ļ������
			JOptionPane.showMessageDialog(null, "�����ɹ�!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
