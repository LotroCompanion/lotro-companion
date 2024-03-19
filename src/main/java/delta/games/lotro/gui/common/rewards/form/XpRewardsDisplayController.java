package delta.games.lotro.gui.common.rewards.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.l10n.L10n;

/**
 * Controller for a panel to display XP rewards.
 * @author DAM
 */
public class XpRewardsDisplayController
{
  private JLabel _xp;
  private JLabel _xpIcon;
  private JLabel _itemXp;
  private JLabel _itemXpIcon;
  private JLabel _mountXp;
  private JLabel _mountXpIcon;
  private JPanel _panel;

  /**
   * Constructor.
   */
  public XpRewardsDisplayController()
  {
    _xp=GuiFactory.buildLabel("0");
    _itemXp=GuiFactory.buildLabel("0");
    _mountXp=GuiFactory.buildLabel("0");
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

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    // XP
    ret.add(_xp,c);
    c.gridx++;
    Icon xpIcon=IconsManager.getIcon("/resources/gui/rewards/xp.png");
    _xpIcon=GuiFactory.buildIconLabel(xpIcon);
    ret.add(_xpIcon,c);
    c.gridx++;
    // Silver
    ret.add(_itemXp,c);
    c.gridx++;
    Icon itemXpIcon=IconsManager.getIcon("/resources/gui/rewards/li-xp.png");
    _itemXpIcon=GuiFactory.buildIconLabel(itemXpIcon);
    ret.add(_itemXpIcon,c);
    c.gridx++;
    // Copper
    ret.add(_mountXp);
    c.gridx++;
    Icon mountXpIcon=IconsManager.getIcon("/resources/gui/rewards/mount-xp.png");
    _mountXpIcon=GuiFactory.buildIconLabel(mountXpIcon);
    ret.add(_mountXpIcon,c);
    c.gridx++;
    return ret;
  }

  /**
   * Display XP values.
   * @param xp XP to show (hidden if 0).
   * @param itemXp Item XP to show (hidden if 0).
   * @param mountXp Mount XP to show (hidden if 0).
   */
  public void setValues(int xp, int itemXp, int mountXp)
  {
    // XP
    _xp.setVisible(xp>0);
    _xpIcon.setVisible(xp>0);

    _xp.setText(L10n.getString(xp));
    // Item XP
    _itemXp.setVisible(itemXp>0);
    _itemXpIcon.setVisible(itemXp>0);
    _itemXp.setText(L10n.getString(itemXp));
    // Mount XP
    _mountXp.setVisible(mountXp>0);
    _mountXpIcon.setVisible(mountXp>0);
    _mountXp.setText(L10n.getString(mountXp));
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    _xp=null;
    _xpIcon=null;
    _itemXp=null;
    _itemXpIcon=null;
    _mountXp=null;
    _mountXpIcon=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
