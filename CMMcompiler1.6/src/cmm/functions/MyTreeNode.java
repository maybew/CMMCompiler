package cmm.functions;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * DefaultMutableTreeNode�����࣬Ϊ�˼�ס������Ϣ
 * @author axun
 *
 */
public class MyTreeNode extends DefaultMutableTreeNode{
	private int line=-1;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
	
}
