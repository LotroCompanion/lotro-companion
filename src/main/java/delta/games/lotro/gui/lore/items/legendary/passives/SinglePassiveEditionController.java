package delta.games.lotro.gui.lore.items.legendary.passives;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.StatsSetElement;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.lore.items.legendary.passives.Passive;

/**
 * Controller for the UI items of a single passive.
 * @author DAM
 */
public class SinglePassiveEditionController
{
  // Data
  private Passive _passive;
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
   * @param passive Current passive (may be <code>null</code>).
   * @param itemId Item identifier.
   */
  public SinglePassiveEditionController(WindowController parent, Passive passive, int itemId)
  {
    _parent=parent;
    _passive=passive;
    _itemId=itemId;
    _level=1;
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
        handleButtonClick();
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
        handleDelete();
      }
    };
    _deleteButton.addActionListener(listenerDelete);
    updateUi();
  }

  /**
   * Set the level to use for passive stats computations.
   * @param level Level to set.
   */
  public void setLevel(int level)
  {
    _level=level;
    updateStats();
  }

  private void handleButtonClick()
  {
    Passive passive=PassiveChooser.selectPassive(_parent,_itemId,_level,_passive);
    if (passive!=null)
    {
      _passive=passive;
      updateUi();
    }
  }

  private void handleDelete()
  {
    _passive=null;
    updateUi();
  }

  /**
   * Get the managed passive.
   * @return the managed passive.
   */
  public Passive getPassive()
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
    // - Resize window
    _parent.pack();
  }

  private void updateStats()
  {
    if (_passive!=null)
    {
      BasicStatsSet stats=_passive.getStatsProvider().getStats(1,_level);
      List<StatsSetElement> statElements=stats.getStatElements();
      int nbStats=statElements.size();
      if (nbStats==1)
      {
        List<String> lines=StatUtils.getStatsForDisplay(stats);
        _value.setText(lines.get(0));
      }
    }
    else
    {
      _value.setText("");
    }
  }

  /**
   * Get the label to display the passive.
   * @return a single line label.
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
