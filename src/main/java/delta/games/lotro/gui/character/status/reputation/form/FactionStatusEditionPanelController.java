package delta.games.lotro.gui.character.status.reputation.form;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.games.lotro.character.status.reputation.FactionStatus;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a panel to edition a faction status (level+reputation amount).
 * @author DAM
 */
public class FactionStatusEditionPanelController extends AbstractPanelController
{
  // Data
  private FactionStatus _status;
  // Controllers
  private ComboBoxController<Integer> _tiers;
  private IntegerEditionController _reputationValue;
  // UI
  private JPanel _reputationPanel;
  private JLabel _reputationMax;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to edit.
   */
  public FactionStatusEditionPanelController(AreaController parent, FactionStatus status)
  {
    super(parent);
    _status=status;
    JPanel panel=buildPanel();
    setPanel(panel);
    updateUi();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(GuiFactory.buildLabel("Current tier: ")); // I18n
    // Tiers
    _tiers=buildTierCombo();
    panel.add(_tiers.getComboBox());
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer item)
      {
        Faction faction=_status.getFaction();
        FactionLevel level=null;
        if (item!=null)
        {
          level=faction.getLevelByTier(item.intValue());
        }
        updateReputationPanel(level);
      }
    };
    _tiers.addListener(listener);
    // Reputation panel
    _reputationPanel=buildReputationPanel();
    panel.add(_reputationPanel);
    return panel;
  }

  private JPanel buildReputationPanel()
  {
    // Value editor
    JTextField reputationAmount=GuiFactory.buildTextField("");
    _reputationValue=new IntegerEditionController(reputationAmount);
    _reputationValue.setValueRange(Integer.valueOf(0),null);
    // Max label
    _reputationMax=GuiFactory.buildLabel(" / 000000");
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.CENTER,5,2));
    panel.add(GuiFactory.buildLabel("Reputation: ")); // I18n
    panel.add(_reputationValue.getTextField());
    panel.add(_reputationMax);
    return panel;
  }

  /**
   * Update UI from current data.
   */
  public void updateUi()
  {
    // Level
    FactionLevel level=_status.getFactionLevel();
    Integer tier=(level!=null)?Integer.valueOf(level.getTier()):null;
    _tiers.selectItem(tier);
    updateReputationPanel(level);
    // Reputation amount
    Integer reputation=_status.getReputation();
    if (reputation!=null)
    {
      if (level!=null)
      {
        int requiredRep=level.getRequiredReputation();
        int diff=reputation.intValue()-requiredRep;
        _reputationValue.setValue(Integer.valueOf(diff));
      }
    }
    else
    {
      _reputationValue.setValue(Integer.valueOf(0));
    }
  }

  private void updateReputationPanel(FactionLevel level)
  {
    boolean visible=true;
    if (level==null)
    {
      visible=false;
    }
    Faction faction=_status.getFaction();
    FactionLevel nextLevel=faction.getNext(level);
    if (nextLevel==null)
    {
      visible=false;
    }
    _reputationPanel.setVisible(visible);
    if (visible)
    {
      int maxRep=nextLevel.getRequiredReputation()-level.getRequiredReputation();
      _reputationValue.setValue(Integer.valueOf(0));
      _reputationValue.setValueRange(Integer.valueOf(0),Integer.valueOf(maxRep));
      _reputationMax.setText(" / "+maxRep);
    }
  }

  /**
   * Update data from the UI contents.
   */
  public void updateData()
  {
    Faction faction=_status.getFaction();
    // Selected level
    Integer tier=_tiers.getSelectedItem();
    FactionLevel level;
    if (tier!=null)
    {
      level=faction.getLevelByTier(tier.intValue());
    }
    else
    {
      level=null;
    }
    _status.setFactionLevel(level);
    // Reputation amount
    if (level!=null)
    {
      int requiredReputation=level.getRequiredReputation();
      int totalReputation=requiredReputation;
      if (_reputationPanel.isVisible())
      {
        Integer additionalReputation=_reputationValue.getValue();
        if (additionalReputation!=null)
        {
          totalReputation+=additionalReputation.intValue();
        }
      }
      _status.setReputation(Integer.valueOf(totalReputation));
    }
    else
    {
      _status.setReputation(null);
    }
  }

  /**
   * Build a combobox to select a faction level.
   * @return a combobox.
   */
  private ComboBoxController<Integer> buildTierCombo()
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    ctrl.addEmptyItem("");
    Faction faction=_status.getFaction();
    FactionLevel[] levels=faction.getLevels();
    for(FactionLevel level : levels)
    {
      String rawName=level.getName();
      String name=ContextRendering.render(this,rawName);
      ctrl.addItem(Integer.valueOf(level.getTier()),name);
    }
    return ctrl;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    if (_reputationPanel!=null)
    {
      _reputationPanel.removeAll();
      _reputationPanel=null;
    }
    if (_tiers!=null)
    {
      _tiers.dispose();
      _tiers=null;
    }
    if (_reputationValue!=null)
    {
      _reputationValue.dispose();
      _reputationValue=null;
    }
    _status=null;
  }
}
