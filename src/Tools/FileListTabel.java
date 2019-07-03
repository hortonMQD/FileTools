package Tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import Tools.FixedTable;

import pojo.FileInfo;

/**
 * 
 * @author Horton
 *date:2019.3.25
 */
public class FileListTabel extends JFrame implements ActionListener{
	
	private JFrame jFrame;
	private JProgressBar progressBar;
	private JPanel TabelPanel;
	private FixedTable table;
	private JPanel Button_Panel;
	private JButton ok_button;
	private JButton delete_button;
	private JButton cancel_button;
	private JMenuItem deleteItem;
	private JPopupMenu itemMenu;
	private int sum = 0;
	private int cur = 0;
	private int dcount = 0;
	private int fcount = 0;
	private Map<String, Vector<File>> map = new HashMap<>();
	
	
	private List<File> repetitionList = new Vector<>();
	
	

	public FileListTabel(String path1,String path2) {
		
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();// ����BeautyEye����
			UIManager.put("RootPane.setupButtonVisible", false);//����BeautyEye�����е����ð�ť

		} catch (Exception e) {   
			e.printStackTrace();
		}
		
		jFrame = new JFrame("���ش�ʦ");
		Button_Panel = initButtonPanel();		//��ʼ����ť���
		TabelPanel = initTabelPanel();			//��ʼ��������
		Container container = jFrame.getContentPane();	
		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(BorderLayout.NORTH,TabelPanel);
		jPanel.add(BorderLayout.CENTER,Button_Panel);		
		container.add(jPanel);
		
		jFrame.setVisible(true);
		jFrame.setSize(600, 580);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		File file1 = new File(path1);
		File file2 = new File(path2);	
		
		Checkdata(file1, file2);		//�ļ��ȽϷ���
	}
	/**
	 * ��ʼ�ļ�����
	 * @param file1
	 * @param file2
	 */
	public void Checkdata(File file1,File file2) {
		new Thread(new Runnable() {
			public void run() {
				progressBar.setIndeterminate(true);
				findRecheckMethod(file1);
				findRecheckMethod(file2);
				dfs(file1);
				dfs(file2);
				if (map.size() != 0) {
					setTableData(map);
				}
				progressBar.setIndeterminate(false);
			}
		}).start();
	}
	
	
	/**
	 * ��ʼ����ť���
	 * @return
	 */
	public JPanel initButtonPanel() {
		JPanel Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
		ok_button = new JButton("��  ��");
		delete_button = new JButton("ɾ  ��");
		//cancel_button = new JButton("ȡ  ��");
		ok_button.addActionListener(this);
		delete_button.addActionListener(this);
		Panel.add(ok_button);
		Panel.add(delete_button);
		//Panel.add(cancel_button);
		
		return Panel;
	}
	
	
	/**
	 * ��ʼ��������
	 * @return
	 */
	public JPanel initTabelPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane ScrollPane = new JScrollPane();
		table = new FixedTable(); // ����ָ�����ģ�͵ı��
		table.setCellEditable(false);//�ñ�񲻿ɱ༭
		ScrollPane.setViewportView(table);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(new String[]{ "���", "�ļ���", "�洢·��"});
        progressBar = new JProgressBar();
        panel.add(BorderLayout.NORTH,ScrollPane);
        panel.add(BorderLayout.SOUTH,progressBar);
        itemMenu = new JPopupMenu();
        
        deleteItem = new JMenuItem("ɾ��");
        itemMenu.add(deleteItem);
        
        table.add(itemMenu);
        
        //ɾ���˵�����ӵ���¼�
        deleteItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String filePath = (String)table.getValueAt(table.getSelectedRow(), 2);//��ȡѡ���еĵ��������ݣ���ֵΪ�ļ���ַ
				File file = new File(filePath);
				if(file.exists()) {
					file.delete();
					JOptionPane.showMessageDialog(null,"���ļ���ɾ��","��ʾ��Ϣ",JOptionPane.WARNING_MESSAGE); //������ʾ��Ϣ
				}else {
					JOptionPane.showMessageDialog(null,"���ļ�������","��ʾ��Ϣ",JOptionPane.WARNING_MESSAGE); //������ʾ��Ϣ
				}
			}
		});
        
        
        
        
        
        table.addMouseListener(new MouseAdapter() {	//	����������¼�����
			public void mouseClicked(MouseEvent e) {//�������ʱ
				
				if(e.getButton() == e.BUTTON3) {		//���������Ҽ��¼�
					//String filePath = (String)table.getValueAt(table.getSelectedRow(), 2);//��ȡѡ���еĵ�һ�����ݣ���ֵΪid
					itemMenu.show(e.getComponent(), e.getX(), e.getY());
				}else if(e.getClickCount() == 2) {		//��������˫���¼�
					String filePath = (String)table.getValueAt(table.getSelectedRow(), 2);//��ȡѡ���еĵ��������ݣ���ֵΪ�ļ���ַ
					File file = new File(filePath);
					if(file.exists()) {
						Desktop desktop = Desktop.getDesktop();
						try {
							desktop.open(file);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(null,"���ļ�������","��ʾ��Ϣ",JOptionPane.WARNING_MESSAGE); //������ʾ��Ϣ
					}
					
					
				}//if������
			}//mouseClicked()����
		});//addMouseListener()����
        
        return panel;
	}
	
	
	


	/**
	 * ��������ж����ļ����бȽ�
	 * @param f
	 */
	public void findRecheckMethod(File f) {
		if(f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				findRecheckMethod(files[i]);
				sum++;
			}
		}else {
			sum++;
		}
	}
	
	
	/**
	 * �ļ����ص��÷�����ͨ��md5ֵ���ļ��������ж��Ƿ����ͬһ�ļ�
	 * @param f�������ļ�
	 */
	public void dfs(File f) {
		if (!f.isDirectory()) {		//�ж��ļ��Ƿ�Ϊ�ļ���
			cur++;
			FileInputStream fis;
			try {
				fis = new FileInputStream(f.getAbsolutePath());
				//������ļ���md5ֵ
				String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
				IOUtils.closeQuietly(fis);
				if (map.containsKey(md5)) {		//�ж��Ƿ�Ϊ�Ѽ�¼�ļ�
					Vector<File> vector = map.get(md5);		//��ȡmd5ֵ��ͬ���ļ������н�һ���Ƚ�
					File exsit = vector.get(0);		
					if (CompareFile(f, exsit)) {		//ͨ���ȶ��ļ����ȡ��ļ������������ж��Ƿ�Ϊͬһ�ļ�
						map.get(md5).addElement(f);
					}else {
						return;
					}
				}else {		//��������ѱȽ��ļ�
					Vector<File> temp = new Vector<>();
					temp.add(f);
					map.put(md5, temp);//��¼�ļ�
				}
				return;
			} catch (Exception e) {
				
			}
		}else {			//����ļ�Ϊ�ļ��У����ȡ�ļ������ļ��б����еݹ�
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				dfs(files[i]);
			}
		}
	}
	
	
	/**
	 * �ļ����ص��÷���
	 * @param file1��ԭ�ļ���
	 * @param file2��Ŀ���ļ���
	 * @return
	 */
	public Boolean CompareFile(File file1,File file2) {
		try {
			BufferedInputStream inFile1 = new BufferedInputStream(new FileInputStream(file1));
			BufferedInputStream inFile2 = new BufferedInputStream(new FileInputStream(file2));
			//�ж��ļ������Ƿ�һ��
			if (inFile1.available() != inFile2.available()) {
				inFile1.close();
				inFile2.close();
				return false;
			}
			
			//�Ƚ��ļ����������Ƿ�һ��
			while(inFile1.read() != -1 && inFile2.read() != -1) {
				if (inFile1.read() != inFile2.read()) {
					inFile1.close();
					inFile2.close();
					return false;
				}
			}
			
			inFile1.close();
			inFile2.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * ���ñ������
	 * @param map
	 */
	public void setTableData(Map<String, Vector<File>> map) {
		final DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setRowCount(0);
		for(Map.Entry<String, Vector<File>> f : map.entrySet()) {
			if (f.getValue().size() > 1) {
				for(int i = 0;i < f.getValue().size();i++) {
					Object[] objects = new Object[3];
					objects[0] = i+1;
					objects[1] = f.getValue().get(i).getName().toString();		//����ļ���
					objects[2] = f.getValue().get(i).getAbsolutePath().toString();//����ļ�·��
					model.addRow(objects);
					table.setModel(model);
				}
			}
		}
	}

	/**
	 * ���ð�ť����¼�
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case "��  ��":
			List<FileInfo> fileInfos = new ArrayList<>();
			for(int i = 0;i < table.getModel().getRowCount();i++) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setId(table.getModel().getValueAt(i, 0).toString());
				fileInfo.setFileName(table.getModel().getValueAt(i, 1).toString());
				fileInfo.setFilePath(table.getModel().getValueAt(i, 2).toString());
				fileInfos.add(fileInfo);
			}
			AboutExcel exportExcel = new AboutExcel();
			JFileChooser fcDlg = new JFileChooser();
	         fcDlg.setDialogTitle("��ѡ�񵼳��ļ�·��");
	         fcDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	         int returnVal = fcDlg.showOpenDialog(null);
	         if (returnVal == JFileChooser.APPROVE_OPTION) {
	           String filepath = fcDlg.getSelectedFile().getPath();
	          exportExcel.exportText(fileInfos,filepath);
	         }
			break;
		case "ɾ  ��":
			String filePath = (String)table.getValueAt(table.getSelectedRow(), 2);//��ȡѡ���еĵ��������ݣ���ֵΪ�ļ���ַ
			File file = new File(filePath);
			if(file.exists()) {
				file.delete();
				JOptionPane.showMessageDialog(null,"���ļ���ɾ��","��ʾ��Ϣ",JOptionPane.WARNING_MESSAGE); //������ʾ��Ϣ
			}else {
				JOptionPane.showMessageDialog(null,"���ļ�������","��ʾ��Ϣ",JOptionPane.WARNING_MESSAGE); //������ʾ��Ϣ
			}
			break;
		default:
			break;
		}
	}
	
	
	
}
