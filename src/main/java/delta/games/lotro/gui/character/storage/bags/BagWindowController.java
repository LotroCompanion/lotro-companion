package delta.games.lotro.gui.character.storage.bags;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.BagsSetup;
import delta.games.lotro.character.storage.bags.SingleBagSetup;

/**
 * Controller for a window to display a bag of a single character.
 * @author DAM
 */
public class BagWindowController extends DefaultDisplayDialogController<Void>
{
  // Data
  private BagsManager _bagsManager;
  private int _bagIndex;
  private SingleBagSetup _setup;
  // Controllers
  private BagDisplayPanelController _displayPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param bagsManager Bags to use.
   * @param bagIndex Index of the bag to show.
   */
  public BagWindowController(WindowController parent, BagsManager bagsManager, int bagIndex)
  {
    super(parent,null);
    _bagsManager=bagsManager;
    _bagIndex=bagIndex;
    initData();
  }

  private void initData()
  {
    BagsSetup setups=_bagsManager.getBagsSetup();
    _setup=setups.getBagSetup(_bagIndex);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    _displayPanel=new BagDisplayPanelController(this,_bagsManager,_setup);
    return _displayPanel.getPanel();
  }

  @Override
  public void configureWindow()
  {
    super.configureWindow();
    // Title
    String title="Bag #"+_bagIndex; // I18n
    setTitle(title);
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
    // Data
    _bagsManager=null;
    _setup=null;
    // Controllers
    if (_displayPanel!=null)
    {
      _displayPanel.dispose();
      _displayPanel=null;
    }
  }
}
