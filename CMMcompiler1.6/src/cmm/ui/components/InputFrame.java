package cmm.ui.components;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import cmm.client.CMMClient;
import cmm.ui.MainFrame;

import sun.security.krb5.Config;




//ȷ�� �Ի���,���������� ���ṩ�κβ���
public class InputFrame{
	private static String inputString="";
	private Dialog dialog=null;
	JTextField jTextField=null;
	private int type=1;
	public InputFrame(int type){	//1 int 2 real
		this.type=type;
		dialog=new Dialog(CMMClient.mainFrame);
		dialog.setTitle("����");
		dialog.setModal(true);
		dialog.setLayout(new GridLayout(3,1));
		
		JLabel jLabel=new JLabel();
		if(type==1){
			jLabel.setText("������һ����������");
		}
		else if(type==2){
			jLabel.setText("������һ������������");
		}
		dialog.add(jLabel);
		jTextField=new JTextField();
		dialog.add(jTextField);
		
		JButton bt1=new JButton("ȷ��");
		JButton bt2=new JButton("ȡ��");
		bt1.addActionListener(new ConfirmActionListener());
		bt2.addActionListener(new CancelActionListener());
		JPanel temPanel=new JPanel();
		temPanel.add(bt1);
		//temPanel.add(bt2); //���ܲ�����
		dialog.add(temPanel);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - dialog.getWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - dialog.getHeight()) / 2);
		
		dialog.setVisible(true);
		
	}

	public static String getInputString() {
		return inputString;
	}
	public static void setInputString(String input) {
		inputString = input;
	}

class ConfirmActionListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//**�����������
		String string=jTextField.getText();
		try{
			if(type==1){
				Integer.parseInt(string);
			}else if(type==2){
				Double.parseDouble(string);
			}
		}catch (Exception ex) {
			new ConfirmDialog("����������һ��"+(type==1?"����":"ʵ��"));
			return ;
		}
		setInputString(string);
		dialog.dispose();
	}
	
}

class CancelActionListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		dialog.dispose();
	}
	
}
}
