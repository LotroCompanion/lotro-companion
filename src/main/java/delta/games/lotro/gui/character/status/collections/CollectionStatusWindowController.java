package delta.games.lotro.gui.character.status.collections;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.collections.CollectionStatus;

/**
 * Controller for a window to display the status of a single collection on a single character.
 * @author DAM
 */
public class CollectionStatusWindowController extends DefaultDisplayDialogController<CollectionStatus>
{
  // Controllers
  private CollectionStatusDisplayPanelController _displayPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to use.
   */
  public CollectionStatusWindowController(WindowController parent, CollectionStatus status)
  {
    super(parent,status);
    _displayPanel=new CollectionStatusDisplayPanelController(this,status);
  }

  /**
   * Get the identifier of the collection status window for the given collection status.
   * @param status Status to use.
   * @return An identifier.
   */
  public static String getIdentifier(CollectionStatus status)
  {
    return "COLLECTION_STATUS#"+status.getCollection().getIdentifier();
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier(_data);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    return _displayPanel.getPanel();
  }

  @Override
  public void configureWindow()
  {
    super.configureWindow();
    // Title
    setTitle("Collection status"); // I18n
    // Dimensions
    JDialog dialog=getDialog();
    dialog.setResizable(false);
    pack();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_displayPanel!=null)
    {
      _displayPanel.dispose();
      _displayPanel=null;
    }
  }
}
