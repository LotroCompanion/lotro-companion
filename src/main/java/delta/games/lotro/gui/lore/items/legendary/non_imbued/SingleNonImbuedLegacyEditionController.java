package delta.games.lotro.gui.lore.items.legendary.non_imbued;

import java.awt.Dimension;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.lore.items.Item;
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
  protected int _itemLevel;
  protected Item _itemReference;
  protected int[] _internalRanks;
  // Controllers
  protected WindowController _parent;
  // GUI
  protected JLabel _icon;
  private MultilineLabel2 _value;
  // Current rank
  private ComboBoxController<Integer> _rank;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param constraints Constraints.
   */
  protected SingleNonImbuedLegacyEditionController(WindowController parent, ClassAndSlot constraints)
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
    // - current level
    _rank=buildRankCombo();
  }

  /**
   * Setup reference data.
   * @param itemLevel Item level of the edited item instance.
   * @param item Reference item.
   */
  public void setReferenceData(int itemLevel, Item item)
  {
    _itemLevel=itemLevel;
    _itemReference=item;
  }

  private ComboBoxController<Integer> buildRankCombo()
  {
    ComboBoxController<Integer> ret=new ComboBoxController<Integer>();
    ItemSelectionListener<Integer> rankListener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer newInternalRank)
      {
        if (newInternalRank!=null)
        {
          handleCurrentRankUpdate();
        }
      }
    };
    ret.addListener(rankListener);
    return ret;
  }

  protected void updateRanksCombo(int[] ranks)
  {
    _internalRanks=ranks;
    Integer previousValue=_rank.getSelectedItem();
    if ((ranks!=null) && (ranks.length>0))
    {
      // Remove old items
      _rank.removeAllItems();
      // Push new items
      for(int i=0;i<ranks.length;i++)
      {
        int internalRank=ranks[i];
        int uiRank=(i+1);
        _rank.addItem(Integer.valueOf(internalRank),"Rank "+uiRank);
      }
      if (previousValue!=null)
      {
        _rank.selectItem(previousValue);
      }
      if (_rank.getSelectedItem()==null)
      {
        _rank.selectItem(Integer.valueOf(ranks[0]));
      }
    }
  }

  protected void updateRanksComboWithDefaultRanks(int size)
  {
    int[] ranks=new int[size];
    for(int i=0;i<size;i++)
    {
      ranks[i]=i+1;
    }
    updateRanksCombo(ranks);
  }

  /**
   * Extract data from UI to the given storage.
   * @param storage Storage to use.
   */
  public void getData(T storage)
  {
    // Rank
    Integer rank=_rank.getSelectedItem();
    if (rank!=null)
    {
      storage.setRank(rank.intValue());
    }
  }

  private void handleCurrentRankUpdate()
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
    AbstractNonImbuedLegacy legacy=legacyInstance.getLegacy();
    setupLegacy(legacy);
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
      _rank.selectItem(Integer.valueOf(rank));
      // - Update derived UI
      updateIcon();
    }
    updateStats();
  }

  protected void setupLegacy(AbstractNonImbuedLegacy legacy)
  {
    _legacy=legacy;
  }

  protected void updateGadgetsState()
  {
    boolean enabled=hasLegacy();
    _rank.getComboBox().setEnabled(enabled);
  }

  protected void updateStats()
  {
    if (hasLegacy())
    {
      StatsProvider statsProvider=getStatsProvider();
      int rank=getRank();
      BasicStatsSet stats=statsProvider.getStats(1,rank);
      String[] lines=StatUtils.getStatsDisplayLines(stats);
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
    Integer rank=_rank.getSelectedItem();
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
   * Get the combo controller for the current rank.
   * @return a combobox controller.
   */
  public ComboBoxController<Integer> getCurrentRankEditionController()
  {
    return _rank;
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
    if (_rank!=null)
    {
      _rank.dispose();
      _rank=null;
    }
  }
}
