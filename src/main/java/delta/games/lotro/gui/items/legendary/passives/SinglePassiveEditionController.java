package delta.games.lotro.gui.items.legendary.passives;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.gui.character.stats.StatDisplayUtils;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for the UI items of a single passive.
 * @author DAM
 */
public class SinglePassiveEditionController
{
  // Data
  private Effect _passive;
  private int _itemId;
  private int _level;
  // Controllers
  private WindowController _parent;
  // GUI
  private JLabel _value;
  private JButton _chooseButton;
  private JButton _deleteButton;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param passive Current effect (may be <code>null</code>).
   * @param itemId Item identifier.
   * @param level Item level to use for stats computations.
   */
  public SinglePassiveEditionController(WindowController parent, Effect passive, int itemId, int level)
  {
    _parent=parent;
    _passive=passive;
    _itemId=itemId;
    _level=level;
    // UI
    // - value display
    _value=GuiFactory.buildLabel("");
    // - chooser button
    _chooseButton=GuiFactory.buildButton("...");
    ActionListener listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleButtonClick((JButton)e.getSource());
      }
    };
    _chooseButton.addActionListener(listener);
    // - delete button
    _deleteButton=GuiFactory.buildIconButton("/resources/gui/icons/cross.png");
    ActionListener listenerDelete=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleDelete((JButton)e.getSource());
      }
    };
    _deleteButton.addActionListener(listenerDelete);
    updateUi();
  }

  private void handleButtonClick(JButton button)
  {
    Effect passive=PassiveChooser.selectPassive(_parent,_itemId,_passive);
    if (passive!=null)
    {
      _passive=passive;
      updateUi();
    }
  }

  private void handleDelete(JButton button)
  {
    _passive=null;
    updateUi();
  }

  /**
   * Get the managed passive.
   * @return the managed passive.
   */
  public Effect getPassive()
  {
    return _passive;
  }

  /**
   * Update UI to show the internal legacy data.
   */
  private void updateUi()
  {
    // - Update stats
    updateStats();
  }

  private void updateStats()
  {
    if (_passive!=null)
    {
      BasicStatsSet stats=_passive.getStatsProvider().getStats(1,_level);
      List<StatDescription> statDescriptions=stats.getSortedStats();
      int nbStats=statDescriptions.size();
      if (nbStats==1)
      {
        StatDescription stat=statDescriptions.get(0);
        String statName=stat.getName();
        FixedDecimalsInteger value=stats.getStat(stat);
        String valueStr=StatDisplayUtils.getStatDisplay(value,stat.isPercentage());
        String line=valueStr+" "+statName;
        _value.setText(line);
      }
    }
    else
    {
      _value.setText("");
    }
  }

  /**
   * Get the label to display the passive.
   * @return a multiline label.
   */
  public JLabel getValueLabel()
  {
    return _value;
  }

  /**
   * Get the managed choose button.
   * @return the managed choose button.
   */
  public JButton getChooseButton()
  {
    return _chooseButton;
  }

  /**
   * Get the managed 'delete' button.
   * @return the managed 'delete' button.
   */
  public JButton getDeleteButton()
  {
    return _deleteButton;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _passive=null;
    // Controllers
    _parent=null;
    // UI
    _value=null;
    _chooseButton=null;
    _deleteButton=null;
  }
}
