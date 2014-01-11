package cmm.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.TabableView;

import cmm.functions.Files;
import cmm.staticValues.Values;

public class TextPane extends JPanel{
	static JTabbedPane jTabbedPane=new JTabbedPane();
	static Vector<EditTextPanel> editTextPanelVector=new Vector<EditTextPanel>();
	private static int index=0;	//��ǰѡ�����ļ�
	private static int lastIndex=0;	//�ϴ�ѡ�е��ļ�
	public TextPane(){
		this.setLayout(new BorderLayout());
		this.add(jTabbedPane);
		jTabbedPane.addChangeListener(new   ChangeListener(){
		      public   void   stateChanged(ChangeEvent   e){
		        JTabbedPane   tabbedPane   =   (JTabbedPane)e.getSource();
		        lastIndex=index;
		        index  =   tabbedPane.getSelectedIndex();
		        Values.selectFile(index);
		       // System.out.println("select:"+index);
		      }
		}); 
		
	}
	 
	/**
	 * ���
	 */
	public static void add(File file){
		
		
		EditTextPanel editTextPanel=new EditTextPanel();
		editTextPanel.setText(Values.getFileContent());
		//editTextPanel.setBackground(Color.WHITE);
		editTextPanelVector.add(editTextPanel);

		jTabbedPane.addTab(null,editTextPanel);
		lastIndex=index;
		index=jTabbedPane.getTabCount()-1;
		
		
		
		//����رհ�ť
		JPanel panel=new JPanel();	
		panel.setOpaque(false);
		String fileName=null;
		if(file==null){
			fileName="�½��ļ�";
		}else{
			fileName=file.getName();
		}
		JLabel label=new JLabel(fileName);
		JButton closeTabButton=new JButton();
		closeTabButton.setOpaque(false);
		panel.add(label);
		panel.add(closeTabButton);
		closeTabButton.addActionListener(new CloseTabButtonListener(panel));
		ImageIcon imageIcon=new ImageIcon(ToolBarPanel.class.getResource("/images/dialog_close.png"));
		closeTabButton.setIcon(imageIcon);
		closeTabButton.setToolTipText("�ر�ѡ�");
		closeTabButton.setPreferredSize(new Dimension(16,16));
		closeTabButton.setBorder(null);
		jTabbedPane.setTabComponentAt(index, panel);
		
		//ѡ��������ӵ���һ��
		jTabbedPane.setSelectedIndex(index);
		//System.out.println("select:"+index);
	}
	
	/**
	 * ɾ��
	 */
	public static void delete(int index){
		editTextPanelVector.remove(index);
		jTabbedPane.remove(index);
		//System.out.println("remove:"+index);
		
	}
	//ɾ������ѡ�еģ�
	public static void deleteNow(){
		editTextPanelVector.remove(index);
		jTabbedPane.remove(index);
		//System.out.println("remove:"+index);
		
	}

	public static int getIndex() {
		return index;
	}
	public static int getLatsIndex(){
		return lastIndex;
	}
	
	/**
	 * ѡ��һ��ѡ�
	 */
	public static void selectTab(int index){
		jTabbedPane.setSelectedIndex(index);
	}
	/**
	 * ��õ�ǰѡ���ļ�������
	 * @return
	 */
	public String getInputString(){
		return editTextPanelVector.get(index).getInputString();
	}

}
/**
	 * �رհ�ť
	 */
	class CloseTabButtonListener implements ActionListener{
		JPanel panel=null;
		public CloseTabButtonListener(JPanel panel){
			this.panel=panel;
		}
		public void actionPerformed(ActionEvent e) {
			int index=TextPane.jTabbedPane.indexOfTabComponent(panel);
			Values.deleteFile(index);
			MainFrame.textPane.delete(index);
		}
		
	}
	
	