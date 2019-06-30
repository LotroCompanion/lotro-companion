package delta.games.lotro.gui.common.money;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.games.lotro.common.money.Money;

/**
 * Controller for a panel to edit a money amount.
 * @author DAM
 */
public class MoneyEditionPanelController
{
  private IntegerEditionController _gold;
  private IntegerEditionController _silver;
  private IntegerEditionController _copper;
  private JPanel _panel;

  /**
   * Constructor.
   */
  public MoneyEditionPanelController()
  {
    _gold=buildEditor(4,Integer.valueOf(9999));
    _silver=buildEditor(3,Integer.valueOf(999));
    _copper=buildEditor(2,Integer.valueOf(99));
    _panel=buildPanel();
  }

  private IntegerEditionController buildEditor(int nbColumns, Integer max)
  {
    JTextField textField=GuiFactory.buildTextField("");
    IntegerEditionController editor=new IntegerEditionController(textField,nbColumns);
    editor.setValueRange(Integer.valueOf(0),max);
    return editor;
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
    ret.add(_gold.getTextField(),c);
    c.gridx++;
    Icon goldIcon=IconsManager.getIcon("/resources/gui/money/gold.png");
    JLabel goldLabel=GuiFactory.buildIconLabel(goldIcon);
    ret.add(goldLabel,c);
    c.gridx++;
    // Silver
    ret.add(_silver.getTextField(),c);
    c.gridx++;
    Icon silverIcon=IconsManager.getIcon("/resources/gui/money/silver.png");
    JLabel silverLabel=GuiFactory.buildIconLabel(silverIcon);
    ret.add(silverLabel,c);
    c.gridx++;
    // Copper
    ret.add(_copper.getTextField());
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
    if (money!=null)
    {
      // Gold
      int gold=money.getGoldCoins();
      _gold.setValue(Integer.valueOf(gold));
      // Silver
      int silver=money.getSilverCoins();
      _silver.setValue(Integer.valueOf(silver));
      // Copper
      int copper=money.getCopperCoins();
      _copper.setValue(Integer.valueOf(copper));
    }
  }

  /**
   * Get the currently edited value.
   * @return a money value or <code>null</code> if empty or fully invalid.
   */
  public Money getMoney()
  {
    Money ret=null;
    // Get value
    Integer gold=_gold.getValue();
    // Silver
    Integer silver=_silver.getValue();
    // Copper
    Integer copper=_copper.getValue();
    if ((gold!=null) || (silver!=null) || (copper!=null))
    {
      ret=new Money();
      ret.setGoldCoins(gold!=null?gold.intValue():0);
      ret.setSilverCoins(silver!=null?silver.intValue():0);
      ret.setCopperCoins(copper!=null?copper.intValue():0);
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_gold!=null)
    {
      _gold.dispose();
      _gold=null;
    }
    if (_silver!=null)
    {
      _silver.dispose();
      _silver=null;
    }
    if (_copper!=null)
    {
      _copper.dispose();
      _copper=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
