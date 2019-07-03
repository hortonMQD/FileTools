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
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();// 启用BeautyEye主题
			UIManager.put("RootPane.setupButtonVisible", false);//隐藏BeautyEye主题中的设置按钮

		} catch (Exception e) {   
			e.printStackTrace();
		}
		
		jFrame = new JFrame("查重大师");
		Button_Panel = initButtonPanel();		//初始化按钮面板
		TabelPanel = initTabelPanel();			//初始化表格面板
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
		
		Checkdata(file1, file2);		//文件比较方法
	}
	/**
	 * 开始文件查重
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
	 * 初始化按钮面板
	 * @return
	 */
	public JPanel initButtonPanel() {
		JPanel Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
		ok_button = new JButton("导  出");
		delete_button = new JButton("删  除");
		//cancel_button = new JButton("取  消");
		ok_button.addActionListener(this);
		delete_button.addActionListener(this);
		Panel.add(ok_button);
		Panel.add(delete_button);
		//Panel.add(cancel_button);
		
		return Panel;
	}
	
	
	/**
	 * 初始化表格面板
	 * @return
	 */
	public JPanel initTabelPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane ScrollPane = new JScrollPane();
		table = new FixedTable(); // 创建指定表格模型的表格
		table.setCellEditable(false);//让表格不可编辑
		ScrollPane.setViewportView(table);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(new String[]{ "序号", "文件名", "存储路径"});
        progressBar = new JProgressBar();
        panel.add(BorderLayout.NORTH,ScrollPane);
        panel.add(BorderLayout.SOUTH,progressBar);
        itemMenu = new JPopupMenu();
        
        deleteItem = new JMenuItem("删除");
        itemMenu.add(deleteItem);
        
        table.add(itemMenu);
        
        //删除菜单项添加点击事件
        deleteItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String filePath = (String)table.getValueAt(table.getSelectedRow(), 2);//获取选中行的第三列数据，赋值为文件地址
				File file = new File(filePath);
				if(file.exists()) {
					file.delete();
					JOptionPane.showMessageDialog(null,"该文件已删除","提示消息",JOptionPane.WARNING_MESSAGE); //弹出提示信息
				}else {
					JOptionPane.showMessageDialog(null,"该文件不存在","提示消息",JOptionPane.WARNING_MESSAGE); //弹出提示信息
				}
			}
		});
        
        
        
        
        
        table.addMouseListener(new MouseAdapter() {	//	表格添加鼠标事件监听
			public void mouseClicked(MouseEvent e) {//当鼠标点击时
				
				if(e.getButton() == e.BUTTON3) {		//如果鼠标是右键事件
					//String filePath = (String)table.getValueAt(table.getSelectedRow(), 2);//获取选中行的第一列数据，赋值为id
					itemMenu.show(e.getComponent(), e.getX(), e.getY());
				}else if(e.getClickCount() == 2) {		//如果鼠标是双击事件
					String filePath = (String)table.getValueAt(table.getSelectedRow(), 2);//获取选中行的第三列数据，赋值为文件地址
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
						JOptionPane.showMessageDialog(null,"该文件不存在","提示消息",JOptionPane.WARNING_MESSAGE); //弹出提示信息
					}
					
					
				}//if语句结束
			}//mouseClicked()结束
		});//addMouseListener()结束
        
        return panel;
	}
	
	
	


	/**
	 * 计算出共有多少文件进行比较
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
	 * 文件查重调用方法，通过md5值和文件长度来判断是否存在同一文件
	 * @param f：查重文件
	 */
	public void dfs(File f) {
		if (!f.isDirectory()) {		//判断文件是否为文件夹
			cur++;
			FileInputStream fis;
			try {
				fis = new FileInputStream(f.getAbsolutePath());
				//计算出文件的md5值
				String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
				IOUtils.closeQuietly(fis);
				if (map.containsKey(md5)) {		//判断是否为已记录文件
					Vector<File> vector = map.get(md5);		//获取md5值相同的文件，进行进一步比较
					File exsit = vector.get(0);		
					if (CompareFile(f, exsit)) {		//通过比对文件长度、文件具体内容来判断是否为同一文件
						map.get(md5).addElement(f);
					}else {
						return;
					}
				}else {		//如果不是已比较文件
					Vector<File> temp = new Vector<>();
					temp.add(f);
					map.put(md5, temp);//记录文件
				}
				return;
			} catch (Exception e) {
				
			}
		}else {			//如果文件为文件夹，则获取文件夹内文件列表，进行递归
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				dfs(files[i]);
			}
		}
	}
	
	
	/**
	 * 文件查重调用方法
	 * @param file1：原文件夹
	 * @param file2：目标文件夹
	 * @return
	 */
	public Boolean CompareFile(File file1,File file2) {
		try {
			BufferedInputStream inFile1 = new BufferedInputStream(new FileInputStream(file1));
			BufferedInputStream inFile2 = new BufferedInputStream(new FileInputStream(file2));
			//判断文件长度是否一样
			if (inFile1.available() != inFile2.available()) {
				inFile1.close();
				inFile2.close();
				return false;
			}
			
			//比较文件具体内容是否一样
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
	 * 设置表格数据
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
					objects[1] = f.getValue().get(i).getName().toString();		//获得文件名
					objects[2] = f.getValue().get(i).getAbsolutePath().toString();//获得文件路径
					model.addRow(objects);
					table.setModel(model);
				}
			}
		}
	}

	/**
	 * 设置按钮点击事件
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case "导  出":
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
	         fcDlg.setDialogTitle("请选择导出文件路径");
	         fcDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	         int returnVal = fcDlg.showOpenDialog(null);
	         if (returnVal == JFileChooser.APPROVE_OPTION) {
	           String filepath = fcDlg.getSelectedFile().getPath();
	          exportExcel.exportText(fileInfos,filepath);
	         }
			break;
		case "删  除":
			String filePath = (String)table.getValueAt(table.getSelectedRow(), 2);//获取选中行的第三列数据，赋值为文件地址
			File file = new File(filePath);
			if(file.exists()) {
				file.delete();
				JOptionPane.showMessageDialog(null,"该文件已删除","提示消息",JOptionPane.WARNING_MESSAGE); //弹出提示信息
			}else {
				JOptionPane.showMessageDialog(null,"该文件不存在","提示消息",JOptionPane.WARNING_MESSAGE); //弹出提示信息
			}
			break;
		default:
			break;
		}
	}
	
	
	
}
