package cmm.ui.components;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

/**
     * �Զ���ڵ��࣬�����Ӧ�ļ�
     * @author axun
     *
     */
    public class MyTreeNode extends DefaultMutableTreeNode{
		private File file=null;
    	
    	public MyTreeNode(Object object){
    		super(object);
    	}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}
    	
    	
    }