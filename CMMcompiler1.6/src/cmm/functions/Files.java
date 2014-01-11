package cmm.functions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cmm.staticValues.Values;
import cmm.ui.MainFrame;
import cmm.ui.components.ConfirmDialog;
import cmm.ui.components.SaveFileWindow;

public class Files {
	FileInputStream fin = null;
	FileOutputStream fout=null;
	BufferedInputStream bin=null;
	BufferedOutputStream bout=null;
	String fileContent="";
	File file=null;
	//���ļ�
	public void openFile(File file){
		Values.setFileContent(new Files().getFileContent(file));
		Values.addFile(file);
		
	}
	
	/**�رյ�ǰѡ�е��ļ�
	 * 
	 */
	public void closeFile(){
		int index=MainFrame.textPane.getIndex();
		Values.deleteFile(index);
		MainFrame.textPane.delete(index);
	}
	
	/**
	 * ���Զ�ȡ�ļ�����
	 * @param file
	 * @return
	 */
	public String getFileContent(File file){
		this.file=file;
		if(file==null) return null;
		int b;
		try{
			fin=new FileInputStream(file);
		}catch(FileNotFoundException e){
			System.out.println("File not found!");
		}
		bin=new BufferedInputStream(fin);
		try{
			while((b=bin.read())!=-1){
				fileContent+=((char)b);
			}
		}catch(IOException e){
			System.out.println("file read Error!");
			
		}
		try{
			bin.close();
		}catch(IOException e){
			System.out.println("file close error!");
		}
		return fileContent;
	}
	
	/**
	 * �Ƚ������ļ��Ƿ���ͬ
	 * @param file1
	 * @param file2
	 * @return
	 */
	public boolean compareFile(File file1,File file2){
		if(file1==null||file2==null)return false;
		if(!file1.getName().equals(file2.getName())) return false;	//�ļ�����ͬ������false
		FileInputStream fis1=null ;
		FileInputStream fis2 =null;
		try {
			fis1 = new FileInputStream(file1);
			fis2 = new FileInputStream(file2);
			
			int len1 = fis1.available();
			int len2 = fis2.available();
			
			if (len1 == len2) {//������ͬ����ȽϾ�������
				//���������ֽڻ�����
				byte[] data1 = new byte[len1];
				byte[] data2 = new byte[len2];
				
				//�ֱ������ļ������ݶ��뻺����
				fis1.read(data1);
				fis2.read(data2);
				
				//���αȽ��ļ��е�ÿһ���ֽ�
				for (int i=0; i<len1; i++) {
					//ֻҪ��һ���ֽڲ�ͬ�������ļ��Ͳ�һ��
					if (data1[i] != data2[i]) {
						//System.out.println("�ļ����ݲ�һ��");
						return false;
					}
				}
				
				//System.out.println("�����ļ���ȫ��ͬ");
				return true;
			} else {
				//���Ȳ�һ�����ļ��϶���ͬ
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {//�ر��ļ�������ֹ�ڴ�й©
			if (fis1 != null) {
				try {
					fis1.close();
				} catch (IOException e) {
					//����
					e.printStackTrace();
				}
			}
			if (fis2 != null) {
				try {
					fis2.close();
				} catch (IOException e) {
					//����
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	
	//�����ļ�
	public void saveFile(){
		//��������
		String string=MainFrame.textPane.getInputString();
		boolean ifnull=(Values.getFile()==null);
		File newFile=null;
		if(ifnull){	//��������Ѿ������ļ�
			SaveFileWindow saveFileWindow=new SaveFileWindow();
			if(saveFileWindow.ifChosed()){
				newFile=new File(saveFileWindow.getFile().getAbsolutePath());
				
			}else{
				return;
			}
		}
		if(ifnull) this.file=newFile;
		else this.file=Values.getFile();
		System.out.println(file.getAbsolutePath());
		int b;
		try{
			fout=new FileOutputStream(file.getAbsolutePath());
		}catch(FileNotFoundException e){
			System.out.println("File not found!");
			e.printStackTrace();
		}
		
		bout=new BufferedOutputStream(fout);
		try{
			for(int i=0;i<string.length();i++){
				bout.write((int)string.charAt(i));
			}
		}catch(IOException e){
			System.out.println("file write Error!");
		}
		try{
			bout.close();
		}catch(IOException e){
			System.out.println("file close error!");
		}
		
		//������½����ļ����ر���ǰ�ģ������ڵ�
		if(ifnull){
			MainFrame.textPane.deleteNow();
			openFile(file);
		}
		if(!ifnull){
			new ConfirmDialog("����ɹ���");
		}
	}
	
	/**
	 * �������ļ�,���ļ���null
	 */
	public void createFile(){
		Values.addFile(null);
	}
}
