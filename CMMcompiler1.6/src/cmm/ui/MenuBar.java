package cmm.ui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import cmm.functions.GramParse;
import cmm.ui.listeners.CloseFileListener;
import cmm.ui.listeners.CreateFileListener;
import cmm.ui.listeners.GramParseListener;
import cmm.ui.listeners.OpenFileListener;
import cmm.ui.listeners.SaveFileListener;
import cmm.ui.listeners.SyntaxParseListener;
import cmm.ui.listeners.WordAnalyseListener;

public class MenuBar extends JMenuBar{
	private JMenu menu1=new JMenu("�ļ�");
	private JMenu menu2=new JMenu("�༭");
	private JMenu menu3=new JMenu("����");
	private JMenu menu4=new JMenu("����");
	private JMenu menu5=new JMenu("����");
	private JMenu menu6=new JMenu("����");
	private JMenu menu7=new JMenu("����");
	
	private JMenu menu1_1=new JMenu("�½�");
	private JMenuItem menu1_1_1=new JMenuItem("txt�ļ�");
	private JMenuItem menu1_2=new JMenuItem("���ļ�");
	private JMenuItem menu1_3=new JMenuItem("����");
	private JMenuItem menu1_4=new JMenuItem("���Ϊ");
	private JMenuItem menu1_5=new JMenuItem("�ر��ļ�");
	private JMenuItem menu1_6=new JMenuItem("�˳�����");
	
	private JMenuItem menu5_1=new JMenuItem("�ʷ�����");
	private JMenuItem menu5_2=new JMenuItem("�﷨����");
	private JMenuItem menu5_3=new JMenuItem("�������");
	
	public MenuBar(){
		this.add(menu1);
		this.add(menu2);
		this.add(menu3);
		this.add(menu4);
		this.add(menu5);
		this.add(menu6);
		this.add(menu7);
		
		menu1.add(menu1_1);
		menu1_1.add(menu1_1_1);
		menu1.add(menu1_2);
		menu1.add(menu1_3);
		menu1.add(menu1_4);
		menu1.add(menu1_5);
		menu1.add(menu1_6);
		
		menu5.add(menu5_1);
		menu5.add(menu5_2);
		menu5.add(menu5_3);
		
		//��Ӱ�ť����
		menu1_1_1.addActionListener(new CreateFileListener());
		menu1_2.addActionListener(new OpenFileListener());
		menu1_3.addActionListener(new SaveFileListener());
		menu1_5.addActionListener(new CloseFileListener());
		menu5_1.addActionListener(new WordAnalyseListener());
		menu5_2.addActionListener(new GramParseListener());
		menu5_3.addActionListener(new SyntaxParseListener());
		
	}
	
}
