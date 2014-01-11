package cmm.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cmm.functions.GramParse;
import cmm.functions.SyntaxParse;
import cmm.functions.WordParse;
import cmm.ui.MainFrame;

public class SyntaxParseListener implements ActionListener{

	public void actionPerformed(ActionEvent arg0) {
		WordParse wordParse=new WordParse(MainFrame.textPane.getInputString());
		wordParse.start();
		if(wordParse.hasError()){	//��������
			MainFrame.outputPanel.consoleOutput(wordParse.getOutputString()+"\n�﷨�����޷�����");
			MainFrame.outputPanel.select(2);
			return;
		}
		GramParse gramParse=new GramParse(MainFrame.textPane.getInputString(),wordParse.getCmmTokens(),wordParse.getTokenAmount());
		gramParse.start();
		if(gramParse.hasError()){	//��������
			MainFrame.outputPanel.consoleOutput(gramParse.getOutputString()+"\n��������޷�����");
			MainFrame.outputPanel.select(2);
			return;
		}
		//gramParse.showGramTree();
		//gramParse.showTable();
		SyntaxParse syntaxParse=new SyntaxParse(gramParse.getTreeModel());
		syntaxParse.setOutputObject(MainFrame.outputPanel.console);
		syntaxParse.showTable();
		syntaxParse.start();
		
//		MainFrame.outputPanel.consoleOutput(syntaxParse.getOutputString());
		MainFrame.outputPanel.select(2);
	}
	
}
