package delta.games.lotro.gui.character.storage.carryAlls;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.carryAlls.CarryAllInstance;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a window to display a single carry-all.
 * @author DAM
 */
public class CarryAllWindowController extends DefaultDisplayDialogController<Void>
{
  private static final int MAX_HEIGHT=600;
  // Data
  private CarryAllInstance _carryAll;
  // Controllers
  private CarryAllDisplayPanelController _displayPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param carryAll Carry-all to use.
   */
  public CarryAllWindowController(WindowController parent, CarryAllInstance carryAll)
  {
    super(parent,null);
    _carryAll=carryAll;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    _displayPanel=new CarryAllDisplayPanelController(this,_carryAll);
    return _displayPanel.getPanel();
  }

  @Override
  public void configureWindow()
  {
    super.configureWindow();
    // Title
    Item carryAllItem=_carryAll.getReference();
    String itemName=(carryAllItem!=null)?carryAllItem.getName():"?";
    String title="Carry-all: "+itemName; // I18n
    setTitle(title);
    // Dimensions
    JDialog dialog=getDialog();
    dialog.setResizable(true);
    pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width+30,MAX_HEIGHT);
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _carryAll=null;
    // Controllers
    if (_displayPanel!=null)
    {
      _displayPanel.dispose();
      _displayPanel=null;
    }
  }
}
