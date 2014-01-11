package cmm.client;



import javax.swing.*;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.*;
import org.jvnet.substance.skin.EmeraldDuskSkin;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

import cmm.functions.Files;
import cmm.ui.MainFrame;

public class CMMClient {
	public static MainFrame mainFrame;
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new SubstanceLookAndFeel());
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
			//SubstanceLookAndFeel.setCurrentButtonShaper());
			//SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());
			//SubstanceLookAndFeel.setSkin(new EmeraldDuskSkin());
			//SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
			//SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
			//SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
			//SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
			// SubstanceLookAndFeel.setCurrentTitlePainter(new FlatTitePainter());
			} catch (Exception e) {
			System.err.println("Something went wrong!");
			}
		mainFrame=new MainFrame();
		new Files().createFile();	//先创建一个文件
	}
}
