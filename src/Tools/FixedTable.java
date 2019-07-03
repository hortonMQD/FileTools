package Tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import Tools.FixedTable.MyRender;

public class FixedTable extends JTable{
	
	Color bankgrandColor = new Color(228, 254, 254);
	boolean isCellEditable = true;

	public boolean isCellEditable() {
		return isCellEditable;
	}

	public void setCellEditable(boolean isCellEditable) {
		this.isCellEditable = isCellEditable;
	}

	/**
	 * 有参构造方法
	 * 
	 * @param dm
	 *            - 表格数据模型
	 */
	public FixedTable(TableModel dm) {
		super(dm);
		init();
	}

	/**
	 * 无参数构造方法
	 */
	public FixedTable() {
		super();
		init();
	}

	private void init() {
		setSelectionForeground(Color.BLUE);
		// setShowGrid(true) ;

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 表格只能单选
		setDefaultRenderer(Object.class, new MyRender());// 表头高度
		setRowHeight(30);// 行高
		overridCopyAction();
	}

	/**
	 * 重写表格复制动作
	 */
	private void overridCopyAction() {
		// 创建键盘操作对象，此对象表示ctrl+c组合键，任意一个按键释放时激活
		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK, true);
		InputMap inputMap = getInputMap();// 输入事件键值对
		inputMap.put(keyStroke, "copy");// 将刚才的ctrl+c组合键与“copy”组合
		ActionMap actionMap = getActionMap();// 触发事件键值对
		actionMap.put("copy", new AbstractAction() {// 将刚才的“copy”与匿名类组合
			@Override
			public void actionPerformed(ActionEvent e) {// 出发的事件时
				int row = getSelectedRow();// 获取选中行索引
				int column = getSelectedColumn();// 获取选中列索引
				String value = (String) getValueAt(row, column);// 将表格中的数据复制成子字符串变量
				Toolkit tool = Toolkit.getDefaultToolkit();// 创建系统该默认组件工具包
				Clipboard clipboard = tool.getSystemClipboard();// 获得系统剪切板对象
				Transferable trans = new StringSelection(value);// 创建可以保存字符串的传输对象
				clipboard.setContents(trans, null);// 将当前传输对象放入剪切板
			}
		});
	}

	/**
	 * 定义表格头
	 */
	public JTableHeader getTableHeader() {
		JTableHeader tableHeader = super.getTableHeader();// 获得表格头对象
		tableHeader.setReorderingAllowed(false); // 设置表格列不可重排
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer(); // 获得表格头的单元格对象
		// 设置列名居中显示
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		hr.setFont(new Font("黑体", Font.BOLD, 20));
		hr.setPreferredSize(new Dimension(hr.getWidth(), 30));
		return tableHeader;
	}

	/**
	 * 让表格不可编辑
	 */
	public boolean isCellEditable(int row, int column) {
		return isCellEditable;
	}

	/**
	 * 隐藏表格第一列
	 */
	public void hiddenFirstColumn() {
		TableColumnModel firstColumnModel = getColumnModel();
		int count = firstColumnModel.getColumnCount();
		if (count > 0) {
			TableColumn firstColumn = firstColumnModel.getColumn(0);
			firstColumn.setMaxWidth(0);
			firstColumn.setMinWidth(0);
			firstColumn.setPreferredWidth(0);
			firstColumn.setResizable(false);// 列宽不可改变
		}
	}

	/**
	 * 自定义表格渲染类
	 * 
	 * @author horton
	 *
	 */
	class MyRender implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			DefaultTableCellRenderer cr = new DefaultTableCellRenderer();// 创建单元格对象
			cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);// 单元格中内容居中
			// 创建单元格渲染器
			JLabel renderer = (JLabel) cr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
//			if (row % 2 != 0) {// 如果是偶数行
//				renderer.setBackground(bankgrandColor);// 偶数行特殊颜色背景
//			} else {
//				renderer.setBackground(Color.WHITE);// 奇数行白色背景
//			}
//			renderer.setFont(new Font("微软雅黑", Font.PLAIN, 14));
			return renderer;
		}
	}

}
