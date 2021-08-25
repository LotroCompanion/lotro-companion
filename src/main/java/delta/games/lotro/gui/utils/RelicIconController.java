package delta.games.lotro.gui.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicUiTools;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Controller for a relic icon.
 * @author DAM
 */
public class RelicIconController implements IconController
{
  private static final Logger LOGGER=Logger.getLogger(RelicIconController.class);

  private static final int DEFAULT_SIZE=32;

  private WindowController _parent;
  private ActionListener _listener;
  private JButton _icon;
  private Relic _relic;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public RelicIconController(WindowController parent)
  {
    _parent=parent;
    _icon=GuiFactory.buildIconButton();
    _icon.setSize(DEFAULT_SIZE,DEFAULT_SIZE);
    _listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showRelicForm();
      }
    };
    _icon.addActionListener(_listener);
  }

  /**
   * Get the managed item icon.
   * @return an icon.
   */
  public JButton getIcon()
  {
    return _icon;
  }

  /**
   * Set the displayed relic.
   * @param relic Relic to display.
   * @param count Count to display.
   */
  public void setRelic(Relic relic, int count)
  {
    Icon icon=RelicUiTools.buildRelicIcon(relic,count);
    _icon.setIcon(icon);
    _icon.setSize(icon.getIconWidth(),icon.getIconHeight());
    if (relic!=null)
    {
      _icon.setToolTipText(relic.getName());
    }
    _relic=relic;
  }

  private void showRelicForm()
  {
    if (_relic!=null)
    {
      LOGGER.info("Display form for relic: "+_relic);
      NavigatorWindowController navigator=null;
      if (_parent instanceof NavigatorWindowController)
      {
        navigator=(NavigatorWindowController)_parent;
      }
      else
      {
        int id=_parent.getWindowsManager().getAll().size();
        navigator=NavigatorFactory.buildNavigator(_parent,id);
      }
      PageIdentifier ref=ReferenceConstants.getRelicReference(_relic.getIdentifier());
      navigator.navigateTo(ref);
      navigator.bringToFront();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_icon!=null)
    {
      if (_listener!=null)
      {
        _icon.removeActionListener(_listener);
        _listener=null;
      }
      _icon=null;
    }
  }
}
