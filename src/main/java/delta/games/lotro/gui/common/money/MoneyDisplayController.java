package delta.games.lotro.gui.common.money;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
    _gold=GuiFactory.buildLabel("99999");
    _gold.setMinimumSize(_gold.getPreferredSize());
    _gold.setMaximumSize(_gold.getPreferredSize());
    _gold.setHorizontalAlignment(SwingConstants.RIGHT);
    _silver=GuiFactory.buildLabel("999");
    Dimension silverSize=_silver.getPreferredSize();
    _silver.setMinimumSize(silverSize);
    _silver.setMaximumSize(_silver.getPreferredSize());
    _silver.setSize(silverSize);
    _silver.setHorizontalAlignment(SwingConstants.RIGHT);
    _copper=GuiFactory.buildLabel("99");
    Dimension copperSize=_copper.getPreferredSize();
    _copper.setMinimumSize(copperSize);
    _copper.setMaximumSize(_copper.getPreferredSize());
    _copper.setSize(copperSize);
    _copper.setHorizontalAlignment(SwingConstants.RIGHT);
    _panel=buildPanel();
    _panel.setSize(_panel.getPreferredSize());
    _panel.setMinimumSize(_panel.getPreferredSize());
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
    JPanel ret=GuiFactory.buildPanel(null);
    ret.setLayout(new BoxLayout(ret,BoxLayout.LINE_AXIS));
    // Gold
    ret.add(_gold);
    ret.add(Box.createRigidArea(new Dimension(1,0)));
    Icon goldIcon=IconsManager.getIcon("/resources/gui/money/gold.png");
    JLabel goldLabel=GuiFactory.buildIconLabel(goldIcon);
    ret.add(goldLabel);
    ret.add(Box.createRigidArea(new Dimension(1,0)));
    // Silver
    ret.add(_silver);
    ret.add(Box.createRigidArea(new Dimension(1,0)));
    Icon silverIcon=IconsManager.getIcon("/resources/gui/money/silver.png");
    JLabel silverLabel=GuiFactory.buildIconLabel(silverIcon);
    ret.add(silverLabel);
    ret.add(Box.createRigidArea(new Dimension(1,0)));
    // Copper
    ret.add(_copper);
    ret.add(Box.createRigidArea(new Dimension(1,0)));
    Icon copperIcon=IconsManager.getIcon("/resources/gui/money/copper.png");
    JLabel copperLabel=GuiFactory.buildIconLabel(copperIcon);
    ret.add(copperLabel);
    ret.add(Box.createRigidArea(new Dimension(1,0)));
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
