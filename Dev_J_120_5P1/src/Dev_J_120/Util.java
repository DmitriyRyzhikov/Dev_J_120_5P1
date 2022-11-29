package Dev_J_120;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public final class Util {
    
    public static void closeApp(JFrame frame){

        frame.addWindowListener(new WindowAdapter() { 
            @Override
            public void windowClosing(WindowEvent e) {
                int n = JOptionPane
                        .showConfirmDialog(e.getWindow(), "Closing the app? Are you sure?",
                                "Confirmation of closing", JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                if (n == 0) {
                    e.getWindow().setVisible(false);
                    frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE); }           
                else
                    frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            }
        });
    }
    public static File chooseDir(JFrame frame) {
        File fileDir = new File(System.getProperty("user.dir"));
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ch = chooser.showOpenDialog(frame);
            if(ch == JFileChooser.APPROVE_OPTION){
               fileDir = chooser.getSelectedFile();
        }
        return fileDir;
    }
    public static int compareFiles(File f1, File f2) {
        if(f1.isDirectory() && f2.isFile())
            return -1;
        if(f1.isFile() && f2.isDirectory())
            return 1;
        return f1.getName().compareTo(f2.getName());
    }   
}
