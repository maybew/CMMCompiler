/**
 * 编辑代码的界面
 * 有行号
 */
package cmm.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import cmm.ui.components.SyntaxHighLighterJTextPane;

public class EditTextPanel extends JPanel{
	private SyntaxHighLighterJTextPane jTextPane=new SyntaxHighLighterJTextPane();
	private JTextPane indexjPane;
	private int lastLines=-1;	//上一次的行数，判断需不需要重画行号
	
	public EditTextPanel(){
		indexjPane =new JTextPane();
		this.setLayout(new BorderLayout());
		
		JPanel container=new JPanel();

		indexjPane.setPreferredSize(new Dimension(40,100));
		indexjPane.setBorder(BorderFactory.createEtchedBorder());
		indexjPane.setBackground(new Color(245,245,245));
		indexjPane.setEditable(false);
		container.setLayout(new BorderLayout());
		container.add(jTextPane,BorderLayout.CENTER);

		container.add(indexjPane,BorderLayout.WEST);
		
		
		JScrollPane jScrollPane=new JScrollPane(container);
		this.add(jScrollPane,BorderLayout.CENTER);
		jTextPane.setText("");
		jTextPane.setBackground(Color.WHITE);
		jTextPane.setBorder(BorderFactory.createEtchedBorder());
		
		new MyThread().start();
	}
	
	public String getInputString(){
		return jTextPane.getText();
	}
	
	public void setInputString(String s){
		jTextPane.setText(s);
	}
	
	public void setText(String s){
		jTextPane.setText(s);
	}
	
	//显示行号
	public void showIndex(){

		int line=jTextPane.getLineCount()+2;
		if(line<25) line=25;	//至少显示25行的行号
		if(lastLines==line){	//行数没有变化，就不检查
			return;
		}else{
			DefaultStyledDocument m_doc;
			MutableAttributeSet normalAttr;
			StyleContext m_context = new StyleContext();
			m_doc = new DefaultStyledDocument(m_context);
			indexjPane.setDocument(m_doc);
			
			normalAttr = new SimpleAttributeSet();
			StyleConstants.setForeground(normalAttr, Color.black);
			//StyleConstants.setBold(normalAttr, true);	//总是粗体
			StyleConstants.setUnderline(normalAttr, false);	//不要下划线
			StyleConstants.setAlignment(normalAttr,StyleConstants.ALIGN_RIGHT);	//向右对齐
		
			
			
			lastLines=line;
			String s="";
			for(int i=1;i<=lastLines;i++){
				s+=(indexjPane.getText()+i+"\n");
			}
			indexjPane.setText(s);
			m_doc.setCharacterAttributes(0, indexjPane.getText().length()-1, normalAttr, false);
		}
	}
	
	//新建一个线程，定时刷新
	class MyThread extends Thread{

		public void run() {
			int i=0;
			
			while(true){
				try{
					sleep(100);	//sleep一秒钟
					showIndex();
					
				}catch(InterruptedException e){

				}
			}
			
		}
	}
	
	/*class PositionThread extends Thread{
		public void run() {
			
		}
	}*/
}
