package delta.games.lotro.gui.items.legendary.non_imbued;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.common.ui.swing.text.NumberEditionController;
import delta.common.ui.swing.text.NumberListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.utils.StatDisplayUtils;
import delta.games.lotro.lore.items.legendary.non_imbued.AbstractNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegacyInstance;

/**
 * Base class for a controller of the UI items of a single non-imbued legacy.
 * @author DAM
 * @param <T> Type of the managed legacy instance.
 */
public abstract class SingleNonImbuedLegacyEditionController<T extends NonImbuedLegacyInstance>
{
  // Data
  protected AbstractNonImbuedLegacy _legacy;
  protected ClassAndSlot _constraints;
  // Controllers
  protected WindowController _parent;
  // GUI
  protected JLabel _icon;
  private MultilineLabel2 _value;
  private JButton _chooseButton;
  // Current level
  private IntegerEditionController _currentRank;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param constraints Constraints.
   */
  public SingleNonImbuedLegacyEditionController(WindowController parent, ClassAndSlot constraints)
  {
    _parent=parent;
    _constraints=constraints;
    // UI
    // - icon
    _icon=GuiFactory.buildTransparentIconlabel(32);
    // - value display
    _value=new MultilineLabel2();
    Dimension dimension=new Dimension(200,32);
    _value.setMinimumSize(dimension);
    _value.setSize(dimension);
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
    // - current level
    JTextField rankTextField=GuiFactory.buildTextField("");
    _currentRank=new IntegerEditionController(rankTextField);
    NumberListener<Integer> rankListener=new NumberListener<Integer>()
    {
      @Override
      public void valueChanged(NumberEditionController<Integer> source, Integer newValue)
      {
        if (newValue!=null)
        {
          handleCurrentRankUpdate(newValue.intValue());
        }
      }
    };
    // - init listeners
    _currentRank.addValueListener(rankListener);
  }

  /**
   * Extract data from UI to the given storage.
   * @param storage Storage to use.
   */
  public void getData(T storage)
  {
    // Rank
    Integer rank=_currentRank.getValue();
    if (rank!=null)
    {
      storage.setRank(rank.intValue());
    }
  }

  protected abstract void handleButtonClick(JButton button);

  private void handleCurrentRankUpdate(int rank)
  {
    updateStats();
  }

  protected boolean hasLegacy()
  {
    return _legacy!=null;
  }

  /**
   * Update UI to show the given legacy data.
   * @param legacyInstance Legacy instance to show.
   */
  public void setLegacyInstance(T legacyInstance)
  {
    _legacy=legacyInstance.getLegacy();
    updateUiFromLegacy(legacyInstance);
  }

  /**
   * Update UI display using the given legacy instance.
   * @param legacyInstance Legacy instance to show.
   */
  protected void updateUiFromLegacy(T legacyInstance)
  {
    // Update gadgets state
    updateGadgetsState();
    // Update UI to reflect the internal legacy data
    if (hasLegacy())
    {
      // - Update current rank
      int rank=legacyInstance.getRank();
      _currentRank.setValue(Integer.valueOf(rank));
      // - Update derived UI
      updateIcon();
    }
    updateStats();
  }

  protected void updateGadgetsState()
  {
    if (hasLegacy())
    {
      _currentRank.setState(true,true);
    }
    else
    {
      _currentRank.setState(false,false);
    }
  }

  protected void updateStats()
  {
    if (hasLegacy())
    {
      StatsProvider statsProvider=getStatsProvider();
      int rank=getRank();
      BasicStatsSet stats=statsProvider.getStats(1,rank);
      String[] lines=StatDisplayUtils.getStatsDisplayLines(stats);
      _value.setText(lines);
    }
    else
    {
      _value.setText(new String[]{});
    }
  }

  protected abstract StatsProvider getStatsProvider();

  protected abstract void updateIcon();

  private int getRank()
  {
    Integer rank=_currentRank.getValue();
    return rank!=null?rank.intValue():1;
  }

  /**
   * Get the icon gadget.
   * @return the icon gadget.
   */
  public JLabel getIcon()
  {
    return _icon;
  }

  /**
   * Get the label to display the legacy stats.
   * @return a multiline label.
   */
  public MultilineLabel2 getValueLabel()
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
   * Get the edition controller for the current rank.
   * @return an integer edition controller.
   */
  public IntegerEditionController getCurrentRankEditionController()
  {
    return _currentRank;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _legacy=null;
    _constraints=null;
    // Controllers
    _parent=null;
    // UI
    _icon=null;
    _value=null;
    _chooseButton=null;
    if (_currentRank!=null)
    {
      _currentRank.dispose();
      _currentRank=null;
    }
  }
}
