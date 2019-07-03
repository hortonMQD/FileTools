package Tools;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * 
 * @author Horton
 *date:2019.3.25
 */


@SuppressWarnings("serial")
public class FileRecheckingTest extends JFrame{

	private JFrame jFrame;
	private JPanel TextPanel;
	private JPanel ButtonPanel;
	private JLabel FolderLabel_1;
	private JLabel FolderLabel_2;
	private JTextField Folder_1;
	private JTextField Folder_2;
	private JButton Folder_1_button;
	private JButton Folder_2_button;
	
	private JButton ok_button;
	private JButton cancel_button;
	
	String path1 = "0";
	String path2 = "0";
	
	
	public static void main(String[] args) {
		FileRecheckingTest fileRecheckingTest = new FileRecheckingTest();
	}
	
	
	public JPanel initTextPanel() {
		
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();// 启用BeautyEye主题
			UIManager.put("RootPane.setupButtonVisible", false);//隐藏BeautyEye主题中的设置按钮

		} catch (Exception e) {   
			e.printStackTrace();
		}
		
		JPanel Panel = new JPanel(new GridLayout(2, 3));
		FolderLabel_1 = new JLabel("查重文件夹1");
		FolderLabel_2 = new JLabel("查重文件夹2");
		Folder_1 = new JTextField();
		Folder_2 = new JTextField();
		Folder_1.setEditable(false);
		Folder_2.setEditable(false);
		Folder_1_button = new JButton("•••");
		Folder_2_button = new JButton("•••");
		Folder_1_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				path1 = chooseFolder();
				if (path1 == null) {
					JOptionPane.showMessageDialog(null,"请选择文件夹","提示消息",JOptionPane.WARNING_MESSAGE); //弹出提示信息
					path1 = "0";
				} else {
					Folder_1.setText(path1);
				}
			}
		});
		Folder_2_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				path2 = chooseFolder();
				if (path2 == null) {
					path2 = "0";
					JOptionPane.showMessageDialog(null,"请选择文件夹","提示消息",JOptionPane.WARNING_MESSAGE); //弹出提示信息
				} else {
					Folder_2.setText(path2);
				}
			}
		});
		
		Panel.add(FolderLabel_1);
		
		Panel.add(Folder_1);
		Panel.add(Folder_1_button);
		Panel.add(FolderLabel_2);
		Panel.add(Folder_2);
		Panel.add(Folder_2_button);
		
		return Panel;
	}
	
	
	public JPanel initButtonPanel() {
		JPanel Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ok_button = new JButton("确定");
		cancel_button = new JButton("取消");
		
		ok_button.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!path1.equals("0") && !path2.equals("0")) {		//如果都选择了文件夹
					FileListTabel showPanel = new FileListTabel(path1, path2);		//开始比较两个文件夹之间相同的文件
				}else {
					JOptionPane.showMessageDialog(null,"请选择文件夹","提示消息",JOptionPane.WARNING_MESSAGE); //弹出提示信息
				}
				
			}
		});
		
		
		cancel_button.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				Folder_1.setText("");
				Folder_2.setText("");
				path1 = "0";
				path2 = "0";
			}
		});
		
		Panel.add(ok_button);
		Panel.add(cancel_button);
		
		return Panel;
	}
	
	
	
	public FileRecheckingTest() {
		jFrame = new JFrame("查重大师");
		
		ButtonPanel = initButtonPanel();		//初始化确定、取消按钮面板
		TextPanel = initTextPanel();			//初始化选择查重文件夹面板
		
		Container container = jFrame.getContentPane();	
		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(BorderLayout.NORTH,TextPanel);
		jPanel.add(BorderLayout.SOUTH,ButtonPanel);
		container.add(jPanel);
		
		jFrame.setVisible(true);
		jFrame.setSize(400, 200);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	/**
	 * 获得文件夹路径
	 * @return  文件路径
	 */
	public String chooseFolder() {
		JFileChooser fileChooser = new JFileChooser();
		File chooseFile;
		String chooseFilePath = null;
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	//设置用户只需选择目录
		fileChooser.setMultiSelectionEnabled(false);	//设置用户只能选择单个文件夹
		int result = fileChooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			chooseFile = fileChooser.getSelectedFile();
			if (chooseFile.isDirectory()) {
				chooseFilePath = chooseFile.getAbsolutePath().toString();
			}
		}
		return chooseFilePath;
	}
	
	
}
