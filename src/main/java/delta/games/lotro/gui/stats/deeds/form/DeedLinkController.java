package delta.games.lotro.gui.stats.deeds.form;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.misc.Disposable;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Controller for a "deed link" label
 * @author DAM
 */
public class DeedLinkController implements Disposable
{
  // Data
  private DeedDescription _deed;
  // Controllers
  private WindowController _parent;
  private WindowsManager _windowsManager;
  private HyperLinkController _linkCtrl;

  /**
   * Constructor.
   * @param deed Deed to show.
   * @param parentController Parent controller.
   */
  public DeedLinkController(DeedDescription deed, WindowController parentController)
  {
    _deed=deed;
    _parent=parentController;
    _windowsManager=new WindowsManager();
    _linkCtrl=buildLinkController();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JLabel getLabel()
  {
    return _linkCtrl.getLabel();
  }

  /**
   * Build the managed panel.
   * @return a new panel.
   */
  private HyperLinkController buildLinkController()
  {
    String name=_deed.getName();
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showDeed();
      }
    };
    LocalHyperlinkAction deedLinkAction=new LocalHyperlinkAction(name,al);
    HyperLinkController linkCtrl=new HyperLinkController(deedLinkAction);
    JLabel label=linkCtrl.getLabel();
    label.setFont(label.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    return linkCtrl;
  }

  private void showDeed()
  {
    int nbWindows=_windowsManager.getAll().size();
    if (nbWindows==0)
    {
      NavigatorWindowController window=NavigatorFactory.buildNavigator(_parent,0);
      PageIdentifier ref=ReferenceConstants.getAchievableReference(_deed);
      window.navigateTo(ref);
      window.show(false);
      _windowsManager.registerWindow(window);
    }
    else
    {
      NavigatorWindowController window=(NavigatorWindowController)_windowsManager.getAll().get(0);
      PageIdentifier ref=ReferenceConstants.getAchievableReference(_deed);
      window.navigateTo(ref);
      window.bringToFront();
    }
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
    _parent=null;
    if (_linkCtrl!=null)
    {
      _linkCtrl.dispose();
      _linkCtrl=null;
    }
  }
}
