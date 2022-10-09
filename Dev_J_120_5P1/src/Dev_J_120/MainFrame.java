package Dev_J_120;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class MainFrame extends JFrame{
    
    private JTree tree;
    private JTextArea textArea;
    private JSplitPane splitPane;
    private MyTreeModel treeModel;

    public MainFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(400, 230, 1000, 700);
        init();
        addMenu();       
    }
    
    private void init(){
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        textArea = new JTextArea();
        treeModel = new MyTreeModel(System.getProperty("user.dir"));
        setTitle(treeModel.getRoot().toString()); 
        tree = new JTree(treeModel); 
        tree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                TreePath seldPath = tree.getSelectionPath(); 
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    if(tree.isCollapsed(seldPath)) 
                       tree.expandPath(seldPath);
                    else
                       tree.collapsePath(seldPath); 
            }
        });
        setSelectionListener();
        tree.setCellRenderer(new FileTreeCellRenderer());
        JScrollPane treeScroll = new JScrollPane(tree);
        JScrollPane textScroll = new JScrollPane(textArea);
        Dimension dim = new Dimension(200, 500);
        treeScroll.setMinimumSize(dim);
        textScroll.setMinimumSize(dim);
        treeScroll.setBorder(new EmptyBorder(5, 5, 5, 5));
        textScroll.setBorder(new EmptyBorder(5, 5, 5, 5));
        treeScroll.setBackground(Color.WHITE);
        textScroll.setBackground(Color.WHITE); 
        splitPane = new JSplitPane(HORIZONTAL_SPLIT, treeScroll, textScroll);
        panel.add(splitPane, BorderLayout.CENTER);
        add(panel);
        Util.closeApp(this); 
    }
     private void addMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("Open dir");
        menuBar.add(menu);
        menu.add(menuItem);        
        menuItem.addActionListener(e -> {
            File newDir = Util.chooseDir(this);
            if(newDir!=null && newDir.exists()) {
               setTitle(newDir.toString());
               textArea.setText(""); 
               treeModel = new MyTreeModel(newDir.toString()); 
               tree.setModel(treeModel);  
              }
        });
    }

    public void setSelectionListener(){
        tree.addTreeSelectionListener(new TreeSelectionListener() {
        @Override
        public void valueChanged(TreeSelectionEvent event) {
            File file = (File) tree.getLastSelectedPathComponent();
            if(file!=null && file.isFile())
               try {
                   textArea.setText(fileReader(file));
                   textArea.setCaretPosition(0);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            else 
               textArea.setText(""); 
                }
        });
    }
    public String fileReader(File file) throws FileNotFoundException, IOException, OutOfMemoryError{
        FileReader fr = new FileReader(file); 
        StringBuilder sb = new StringBuilder();
            char[] buf = new char[8192];
            int n;
            while((n = fr.read(buf)) >= 0) {
                sb.append(buf, 0, n);
            }
        return sb.toString();
    }
}
