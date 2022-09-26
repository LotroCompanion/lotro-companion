package delta.games.lotro.gui.character.storage.wallet;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.games.lotro.lore.items.paper.filters.PaperItemFilter;

/**
 * Full wallet panel controller:
 * <ul>
 * <li>wallet filter,
 * <li>wallet display.
 * </ul>
 * @author DAM
 */
public class WalletPanelController implements FilterUpdateListener
{
  // Data
  private PaperItemFilter _filter;
  // UI
  private JPanel _panel;
  // Controllers
  private WalletFilterController _filterController;
  private WalletDisplayPanelController _display;

  /**
   * Constructor.
   * @param display Display panel.
   */
  public WalletPanelController(WalletDisplayPanelController display)
  {
    _filter=new PaperItemFilter();
    _filterController=new WalletFilterController(_filter,this);
    _display=display;
    _panel=buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    JPanel top=_filterController.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    ret.add(top,c);
    JPanel center=_display.getPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(center,c);
    return ret;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  @Override
  public void filterUpdated()
  {
    _display.applyFilter(_filter);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    _display=null;
  }
}
