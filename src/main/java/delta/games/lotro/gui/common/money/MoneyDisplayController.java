package delta.games.lotro.gui.common.money;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.common.money.Money;

/**
 * Controller for a panel to display a money amount.
 * @author DAM
 */
public class MoneyDisplayController
{
  private JLabel _gold;
  private JLabel _silver;
  private JLabel _copper;
  private JPanel _panel;

  /**
   * Constructor.
   */
  public MoneyDisplayController()
  {
    _gold=GuiFactory.buildLabel("0");
    _gold.setMinimumSize(new Dimension(25, 10));
    _gold.setHorizontalAlignment(SwingConstants.RIGHT);
    _silver=GuiFactory.buildLabel("0");
    _silver.setMinimumSize(new Dimension(25, 10));
    _silver.setHorizontalAlignment(SwingConstants.RIGHT);
    _copper=GuiFactory.buildLabel("0");
    _copper.setMinimumSize(new Dimension(20, 10));
    _copper.setHorizontalAlignment(SwingConstants.RIGHT);
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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    // Gold
    ret.add(_gold,c);
    c.gridx++;
    Icon goldIcon=IconsManager.getIcon("/resources/gui/money/gold.png");
    JLabel goldLabel=GuiFactory.buildIconLabel(goldIcon);
    ret.add(goldLabel,c);
    c.gridx++;
    // Silver
    ret.add(_silver,c);
    c.gridx++;
    Icon silverIcon=IconsManager.getIcon("/resources/gui/money/silver.png");
    JLabel silverLabel=GuiFactory.buildIconLabel(silverIcon);
    ret.add(silverLabel,c);
    c.gridx++;
    // Copper
    ret.add(_copper);
    c.gridx++;
    Icon copperIcon=IconsManager.getIcon("/resources/gui/money/copper.png");
    JLabel copperLabel=GuiFactory.buildIconLabel(copperIcon);
    ret.add(copperLabel,c);
    c.gridx++;
    return ret;
  }

  /**
   * Display a money amount.
   * @param money Money to show.
   */
  public void setMoney(Money money)
  {
    // Gold
    int gold=money.getGoldCoins();
    _gold.setText(L10n.getString(gold));
    // Silver
    int silver=money.getSilverCoins();
    _silver.setText(L10n.getString(silver));
    // Copper
    int copper=money.getCopperCoins();
    _copper.setText(L10n.getString(copper));
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    _gold=null;
    _silver=null;
    _copper=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
