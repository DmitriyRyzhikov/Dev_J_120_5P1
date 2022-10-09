package Dev_J_120;


import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MyTreeModel implements TreeModel {
        
        private final String root;
        private final Vector listeners;

        public MyTreeModel(String root) {
            
            this.root = root;
            listeners = new Vector();
        }   
        
        @Override
        public Object getRoot() {
            return new File(root);
        }

        @Override
        public Object getChild(Object parent, int index) {
            File directory = (File) parent;
            File[] files = directory.listFiles();
            Arrays.sort(files, Util::compareFiles);           
            return (new File(directory, files[index].getName()));
        }

        @Override
        public int getChildCount(Object parent) {
            File fileSystemMember = (File) parent;
            if (fileSystemMember.isDirectory() && Files.isReadable(fileSystemMember.toPath())) { 
                String[] directoryMembers = fileSystemMember.list();
                return directoryMembers.length;
               }
            else 
                return 0;
        }

        @Override
        public boolean isLeaf(Object node) {
            return ((File) node).isFile();
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue){
            File oldFile = (File) path.getLastPathComponent();
            String fileParentPath = oldFile.getParent();
            String newFileName = (String) newValue;
            File targetFile = new File(fileParentPath, newFileName);
            oldFile.renameTo(targetFile);
            File parent = new File(fileParentPath);
            int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
            Object[] changedChildren = { targetFile };
            fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
            
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            File directory = (File) parent;
            File directoryMember = (File) child;
            String[] directoryMemberNames = directory.list();
            int result = -1;
            for (int i = 0; i < directoryMemberNames.length; ++i) {
               if (directoryMember.getName().equals(directoryMemberNames[i])) {
                   result = i;
                   break;
                  }
                }
            return result;
        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {
            if (l != null && !listeners.contains(l)) 
                listeners.addElement(l);
        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {
            if (l != null)
               listeners.removeElement(l);
        } 
        public void fireTreeNodesInserted(TreeModelEvent e) {
            Enumeration listenerCount = listeners.elements();
            while (listenerCount.hasMoreElements()) {
                  TreeModelListener listener = (TreeModelListener) listenerCount.nextElement();
                  listener.treeNodesInserted(e);
                 }
        }  
        public void fireTreeNodesRemoved(TreeModelEvent e) {
            Enumeration listenerCount = listeners.elements();
            while (listenerCount.hasMoreElements()) {
                  TreeModelListener listener = (TreeModelListener) listenerCount.nextElement();
                  listener.treeNodesRemoved(e);
                 }
        }
        private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
            TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
            Iterator iterator = listeners.iterator();
            TreeModelListener listener = null;
            while (iterator.hasNext()) {
                  listener = (TreeModelListener) iterator.next();
                  listener.treeNodesChanged(event);
    }
  }
        public void fireTreeStructureChanged(TreeModelEvent e) {
            Enumeration listenerCount = listeners.elements();
            while (listenerCount.hasMoreElements()) {
                  TreeModelListener listener = (TreeModelListener) listenerCount.nextElement();
                  listener.treeStructureChanged(e);
                 }
        }
}    