package cmm.staticValues;

import java.io.File;
import java.util.Vector;


import cmm.functions.Files;
import cmm.ui.MainFrame;

public class Values {
	private static int MainFrameWidth=1000;
	private static int MainFrameHeight=700;
	private static File file=null;	//��ǰ�������ļ�
	private static Vector<File> files=new Vector<File>();	//�����Ѿ��򿪵��ļ�
	private static String fileContentString="";
	private static String defaultWorkBench="D:\\workspace\\CMMcompiler1.6";	//Ĭ�Ϲ���Ŀ¼
	
	public static int getMainFrameWidth() {
		return MainFrameWidth;
	}
	public static void setMainFrameWidth(int mainFrameWidth) {
		MainFrameWidth = mainFrameWidth;
	}
	public static int getMainFrameHeight() {
		return MainFrameHeight;
	}
	public static void setMainFrameHeight(int mainFrameHeight) {
		MainFrameHeight = mainFrameHeight;
	}
	public static File getFile() {
		return file;
	}
	public static void addFile(File f) {
		if(file==null){	//��ʾ�½��ļ�
			file = f;
			files.add(f);
			MainFrame.textPane.add(f);
			return ;
		}
		/**
		 * �����Ѿ��򿪵��ļ���ֱ�Ӷ�λ�����ļ������ٴ�һ��
		 */
		if(files.size()>0){
			for(int i=0;i<files.size();i++){
				if(new Files().compareFile(files.get(i),f)){
					Values.selectFile(i);
					MainFrame.textPane.selectTab(i);
					return;
				}
			}
		}
		file = f;
		files.add(f);
		MainFrame.textPane.add(file);
		
	}
	public static String getFileContent(){
		return fileContentString;
	}
	public static void setFileContent(String file){
		fileContentString=file;
	}
	public static void deleteFile(int index){
		files.remove(index);
	}
	/**
	 * ѡ��ĳһ���ļ�Ϊ��ǰ�ļ�
	 * @param index
	 */
	public static void selectFile(int index){
		if(index==-1){	//ȫ���ر���
			file=null;
			fileContentString=null;
			return;
		}
		file=files.get(index);
		fileContentString=new Files().getFileContent(file);
	}
	
	public static Vector<File> getFiles() {
		return files;
	}
	public static String getDefaultWorkBench() {
		return defaultWorkBench;
	}
	public static void setDefaultWorkBench(String defaultWorkBench) {
		Values.defaultWorkBench = defaultWorkBench;
	}
	
}
