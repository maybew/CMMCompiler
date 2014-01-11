package cmm.ui;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import sun.awt.SunHints.Value;

import cmm.staticValues.Values;
import cmm.ui.components.Explorer;
import cmm.ui.components.WordOutput;

public class MainFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int width=Values.getMainFrameWidth();
	private static int height=Values.getMainFrameHeight();
	private static int locationX=100;
	private static int localionY=0;
	
	public static TextPane textPane=new TextPane();
	public static JMenuBar menuBar=new MenuBar();
	public static OutputPanel outputPanel=new OutputPanel();
	public static ToolBarPanel toolBarPanel=new ToolBarPanel();
	public static StatePanel statePanel=new StatePanel();
	public static ExplorerPanel explorerPanel=new ExplorerPanel();
	
	
	public MainFrame(){
		
		this.setTitle("CMM������ v1.2");
		//this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setLocation(locationX, localionY);
		this.setSize(width,height);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		JPanel temPanel=new JPanel(new BorderLayout());
		
		
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel centerPanel=new JPanel();
		centerPanel.setLayout(new BorderLayout());
		
		//��Ӵ���
		this.setJMenuBar(menuBar);
		
		temPanel.add(outputPanel,BorderLayout.CENTER);
		JSplitPane jSplitPane1=new JSplitPane(JSplitPane.VERTICAL_SPLIT,textPane ,temPanel);	
		 jSplitPane1.setOneTouchExpandable(true);
		 //jSplitPane.setMinimumSize(new Dimension(100,50));

		centerPanel.add(jSplitPane1,BorderLayout.CENTER);
		
		//explorer��mainpanel֮�����ˮƽ�϶�
		JSplitPane jSplitPane2=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,explorerPanel ,centerPanel);	
		 jSplitPane2.setOneTouchExpandable(true);
		 //jSplitPane.setMinimumSize(new Dimension(100,50));
		mainPanel.add(jSplitPane2,BorderLayout.CENTER);
		mainPanel.add(toolBarPanel,BorderLayout.NORTH);
		
		//mainPanel.add(explorer,BorderLayout.WEST);
		
		
		 
		this.add(mainPanel,BorderLayout.CENTER);	
		this.add(statePanel,BorderLayout.SOUTH);
		
		this.setVisible(true);
		
		//����jsplitpane�ķָ��С��ע�⣬��setvisible֮��Ҳ������ʾ�����Ժ�������β���
		jSplitPane1.setDividerLocation(0.8);
		
		//�ı䴰���С���¼�
		this.addComponentListener(new ComponentAdapter(){

			public void componentResized(ComponentEvent e) {
				Values.setMainFrameWidth(MainFrame.this.getWidth());
				Values.setMainFrameHeight(MainFrame.this.getHeight());
				outputPanel.setSize();
			}
			
		});
		
		
		
		
	}
	
	
}
