package delta.games.lotro.utils.gui.filechooser;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;

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
   * Get the managed chooser.
   * @return the managed chooser.
   */
  public JFileChooser getChooser()
  {
    return _chooser;
  }

  /**
   * Choose a directory.
   * @param parent Parent component.
   * @param approveButtonText Title of the approve button.
   * @return A directory or <code>null</code> if none chosen.
   */
  public File chooseDirectory(Component parent, String approveButtonText)
  {
    _chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    File chosenDir=choose(parent,approveButtonText);
    if (chosenDir!=null)
    {
      Preferences preferences=Config.getInstance().getPreferences();
      TypedProperties props=preferences.getPreferences(_id);
      props.setStringProperty(CURRENT_FILE_PREFERENCE,chosenDir.getAbsolutePath());
      preferences.savePreferences(props);
    }
    return chosenDir;
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
    File chosenFile=choose(parent,approveButtonText);
    if (chosenFile!=null)
    {
      Preferences preferences=Config.getInstance().getPreferences();
      TypedProperties props=preferences.getPreferences(_id);
      File currentDir=chosenFile.getParentFile();
      props.setStringProperty(CURRENT_FILE_PREFERENCE,currentDir.getAbsolutePath());
      preferences.savePreferences(props);
    }
    return chosenFile;
  }

  private File choose(Component parent, String approveButtonText)
  {
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
    }
    return chosenFile;
  }
}
