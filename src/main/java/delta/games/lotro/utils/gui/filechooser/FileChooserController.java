package delta.games.lotro.utils.gui.filechooser;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import delta.games.lotro.Config;
import delta.games.lotro.Preferences;
import delta.games.lotro.utils.TypedProperties;

/**
 * Controller for a file chooser.
 * @author DAM
 */
public class FileChooserController
{
  private static final String CURRENT_FILE_PREFERENCE="current.file";
  private String _id;
  private String _title;
  private JFileChooser _chooser;

  /**
   * Constructor.
   * @param id Identifier.
   * @param title Window title.
   */
  public FileChooserController(String id, String title)
  {
    _id=id;
    _title=title;
    _chooser=new JFileChooser();
  }

  /**
   * Choose a file.
   * @param parent Parent component.
   * @param approveButtonText Title of the approve button.
   * @return A file or <code>null</code> if none chosen.
   */
  public File chooseFile(Component parent, String approveButtonText)
  {
    _chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(_id);
    String dirStr=props.getStringProperty(CURRENT_FILE_PREFERENCE,null);
    File currentDir=null;
    if (dirStr!=null)
    {
      currentDir=new File(dirStr);
    }
    _chooser.setDialogTitle(_title);
    _chooser.setMultiSelectionEnabled(false);
    _chooser.setCurrentDirectory(currentDir);
    int ok=_chooser.showDialog(parent,approveButtonText);
    File chosenFile=null;
    if (ok==JFileChooser.APPROVE_OPTION)
    {
      chosenFile=_chooser.getSelectedFile();
      currentDir=_chooser.getCurrentDirectory();
      if (currentDir!=null)
      {
        props.setStringProperty(CURRENT_FILE_PREFERENCE,currentDir.getAbsolutePath());
        preferences.savePreferences(props);
      }
    }
    return chosenFile;
  }
}
