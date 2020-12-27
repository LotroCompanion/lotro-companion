package delta.games.lotro.gui.stats.deeds.form;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;

/**
 * Controller for the "deed summary" panel.
 * @author DAM
 */
public class DeedSummaryPanelController implements Disposable
{
  // Data
  private DeedDescription _deed;
  // Controllers
  private DeedLinkController _linkCtrl;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param deed Deed to show.
   * @param parentController Parent controller.
   */
  public DeedSummaryPanelController(DeedDescription deed, WindowController parentController)
  {
    _deed=deed;
    _linkCtrl=new DeedLinkController(deed,parentController);
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Build the managed panel.
   * @return a new panel.
   */
  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // Icon
    DeedType type=_deed.getType();
    ImageIcon icon=LotroIconsManager.getDeedTypeIcon(type);
    JLabel deedIcon=GuiFactory.buildIconLabel(icon);
    panel.add(deedIcon);
    // Name
    JLabel label=_linkCtrl.getLabel();
    panel.add(label);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // Data
    _deed=null;
    // Controllers
    if (_linkCtrl!=null)
    {
      _linkCtrl.dispose();
      _linkCtrl=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
