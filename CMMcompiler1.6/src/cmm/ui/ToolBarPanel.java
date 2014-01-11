package cmm.ui;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cmm.ui.listeners.CloseFileListener;
import cmm.ui.listeners.CreateFileListener;
import cmm.ui.listeners.OpenFileListener;
import cmm.ui.listeners.SaveFileListener;
import cmm.ui.listeners.SyntaxParseListener;


public class ToolBarPanel extends JPanel{
	JButton jButton0=new JButton();
	JButton jButton1=new JButton();
	JButton jButton2=new JButton();
	JButton jButton3=new JButton();
	JButton jButton4=new JButton();
	JButton jButton5=new JButton();
	JButton jButton6=new JButton();
	JButton jButton7=new JButton();
	JButton jButton8=new JButton();
	JButton jButton9=new JButton();
	JButton jButton10=new JButton();
	JButton jButton11=new JButton();
	JButton jButton12=new JButton();
	JButton jButton13=new JButton();
	JButton jButton14=new JButton();
	
	public JLabel jLabel1=new JLabel("100%");	//Ϊ�˷����޸�
	
	public ToolBarPanel(){
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		this.add(jButton0);
		this.add(jButton1);
		this.add(jButton2);
		this.add(jButton3);
		this.add(jButton4);
		this.add(jLabel1);
		this.add(jButton5);
		this.add(jButton6);
//		this.add(jButton7);
//		this.add(jButton8);
//		this.add(jButton9);
//		this.add(jButton10);
//		this.add(jButton11);
//		this.add(jButton12);
//		this.add(jButton13);
//		this.add(jButton14);
		
		jButton0.setPreferredSize(new Dimension(36,36));
		jButton1.setPreferredSize(new Dimension(36,36));
		jButton2.setPreferredSize(new Dimension(36,36));
		jButton3.setPreferredSize(new Dimension(36,36));
		jButton4.setPreferredSize(new Dimension(36,36));
		jButton5.setPreferredSize(new Dimension(36,36));
		jButton6.setPreferredSize(new Dimension(36,36));
		jButton7.setPreferredSize(new Dimension(36,36));
		
		//����ͼƬ
		ImageIcon imageIcon0=new ImageIcon(ToolBarPanel.class.getResource("/images/creatFile.png"));
		ImageIcon imageIcon1=new ImageIcon(ToolBarPanel.class.getResource("/images/folder.png"));
		ImageIcon imageIcon2=new ImageIcon(ToolBarPanel.class.getResource("/images/save.png"));
		ImageIcon imageIcon3=new ImageIcon(ToolBarPanel.class.getResource("/images/trash_full.png"));
		ImageIcon imageIcon4=new ImageIcon(ToolBarPanel.class.getResource("/images/search_add.png"));
		ImageIcon imageIcon5=new ImageIcon(ToolBarPanel.class.getResource("/images/search_remove.png"));
		ImageIcon imageIcon6=new ImageIcon(ToolBarPanel.class.getResource("/images/button_play.png"));
		
		jButton0.setIcon(imageIcon0);
		jButton1.setIcon(imageIcon1);
		jButton2.setIcon(imageIcon2);
		jButton3.setIcon(imageIcon3);
		jButton4.setIcon(imageIcon4);
		jButton5.setIcon(imageIcon5);
		jButton6.setIcon(imageIcon6);
		
		jButton0.setToolTipText("�½��ļ�");
		jButton1.setToolTipText("���ļ�");
		jButton2.setToolTipText("�����ļ�");
		jButton3.setToolTipText("�ر��ļ�");
		jButton4.setToolTipText("�Ŵ�����");
		jButton5.setToolTipText("��С����");
		jButton6.setToolTipText("����");
		
		//��Ӽ���
		jButton0.addActionListener(new CreateFileListener());
		jButton1.addActionListener(new OpenFileListener());
		jButton2.addActionListener(new SaveFileListener());
		jButton3.addActionListener(new CloseFileListener());
		jButton6.addActionListener(new SyntaxParseListener());
		
	}
}
