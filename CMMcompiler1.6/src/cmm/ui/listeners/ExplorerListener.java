package cmm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import cmm.functions.Files;
import cmm.ui.components.MyTreeNode;

public class ExplorerListener implements MouseListener {
	private JTree tree = null;

	public ExplorerListener(JTree tree) {
		this.tree = tree;
	}

	/**
	 * ˫�����ļ�
	 */
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount()>=2){
			MyTreeNode node =
		(MyTreeNode) tree.getLastSelectedPathComponent(); //�������ѡ�еĽ��  
		
		if(node!=null&&node.getFile()!=null) {
			//�����ļ�����
			if(node.getFile().getName().endsWith(".txt")) new Files().openFile(node.getFile());
		}
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
