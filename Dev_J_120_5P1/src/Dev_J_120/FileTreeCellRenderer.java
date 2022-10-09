package Dev_J_120;


import java.awt.Component;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    private final FileSystemView fileSystemView;
    private final JLabel label;

    FileTreeCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {

        if(value instanceof File) {
           File file = (File)value;
           label.setIcon(fileSystemView.getSystemIcon(file));
           label.setText(fileSystemView.getSystemDisplayName(file));
           label.setToolTipText(file.getPath());

           if (selected) {
               label.setBackground(backgroundSelectionColor);
               label.setForeground(textSelectionColor);
               }
           else {
               label.setBackground(backgroundNonSelectionColor);
               label.setForeground(textNonSelectionColor);
                }        
        }
        return label;
    }
}
