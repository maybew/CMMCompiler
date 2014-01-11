package cmm.ui.components;


/**
 * ���ļ�
 */
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


public class ChooseFileWindow {
	private File file=null;
	private boolean chosed=false;
	
	public ChooseFileWindow(int type){
		JFileChooser chooser=new JFileChooser(); 
		chooser.setFileSelectionMode(type); 
		
		
		CMMFileFilter filter=new CMMFileFilter();
		chooser.setFileFilter(filter);
		int result=chooser.showOpenDialog(null); 
		if(result==JFileChooser.APPROVE_OPTION){ 
			chosed=true;
			file=chooser.getSelectedFile();
			System.out.println(file);
		}
	}
	//�Ƿ�����ļ�
	public boolean ifChosed(){
		return chosed;
	}
	
	//����ѡ����ļ�
	public File getFile(){
		return file;
	}

}