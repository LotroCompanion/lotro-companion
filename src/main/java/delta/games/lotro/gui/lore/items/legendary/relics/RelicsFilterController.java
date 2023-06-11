package delta.games.lotro.gui.lore.items.legendary.relics;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.common.enums.RunicTier;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryCodeComparator;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicFilter;
import delta.games.lotro.lore.items.legendary.relics.RelicType;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for a relic filter edition panel.
 * @author DAM
 */
public class RelicsFilterController extends ObjectFilterPanelController
{
  // Data
  private List<Relic> _relics;
  private RelicFilter _filter;
  // GUI
  private JPanel _panel;
  private ComboBoxController<RunicTier> _tier;
  private ComboBoxController<RelicType> _type;
  private JTextField _nameContains;
  private JTextField _statsContains;

  /**
   * Constructor.
   * @param filter Filter.
   * @param relics Relics to choose from.
   */
  public RelicsFilterController(RelicFilter filter, List<Relic> relics)
  {
    _filter=filter;
    _relics=relics;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  @Override
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
      setFilter();
      filterUpdated();
    }
    return _panel;
  }

  /**
   * Set filter.
   */
  public void setFilter()
  {
    // Type
    RelicType type=_filter.getRelicType();
    _type.selectItem(type);
    if (type!=null)
    {
      _type.getComboBox().setEnabled(false);
    }
    // Category
    RunicTier tier=_filter.getRelicTier();
    _tier.selectItem(tier);
    // Name
    String contains=_filter.getNameFilter();
    if (contains!=null)
    {
      _nameContains.setText(contains);
    }
    // Stats
    String statsContains=_filter.getStatsFilter();
    if (statsContains!=null)
    {
      _statsContains.setText(statsContains);
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Type
    _type=buildTypesCombo();
    // Category
    _tier=buildTiersCombo();
    // Name filter
    JPanel nameContainsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    {
      nameContainsPanel.add(GuiFactory.buildLabel("Name filter:"));
      _nameContains=buildReactiveTextField();
      nameContainsPanel.add(_nameContains);
    }
    // Stats filter
    JPanel statsContainsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    {
      statsContainsPanel.add(GuiFactory.buildLabel("Stats filter:"));
      _statsContains=buildReactiveTextField();
      statsContainsPanel.add(_statsContains);
    }
    JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    containsPanel.add(nameContainsPanel);
    containsPanel.add(statsContainsPanel);

    // Type panel
    JPanel typePanel=GuiFactory.buildPanel(new FlowLayout());
    typePanel.add(GuiFactory.buildLabel("Type:"));
    typePanel.add(_type.getComboBox());
    // Category panel
    JPanel categoryPanel=GuiFactory.buildPanel(new FlowLayout());
    categoryPanel.add(GuiFactory.buildLabel("Category:"));
    categoryPanel.add(_tier.getComboBox());

    // Global assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(typePanel,c);
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(categoryPanel,c);
    c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(containsPanel,c);
    return panel;
  }

  private ComboBoxController<RelicType> buildTypesCombo()
  {
    final ComboBoxController<RelicType> ctrl=new ComboBoxController<RelicType>();
    ctrl.addEmptyItem("");
    for(RelicType type : RelicType.getAll())
    {
      ctrl.addItem(type,type.getName());
    }
    ItemSelectionListener<RelicType> l=new ItemSelectionListener<RelicType>()
    {
      @Override
      public void itemSelected(RelicType type)
      {
        _filter.setRelicType(type);
        filterUpdated();
      }
    };
    ctrl.addListener(l);
    return ctrl;
  }

  private ComboBoxController<RunicTier> buildTiersCombo()
  {
    final ComboBoxController<RunicTier> ctrl=new ComboBoxController<RunicTier>();
    ctrl.addEmptyItem("");
    for(RunicTier tier  : getTiers())
    {
      ctrl.addItem(tier,tier.getLabel());
    }
    ItemSelectionListener<RunicTier> l=new ItemSelectionListener<RunicTier>()
    {
      @Override
      public void itemSelected(RunicTier tier)
      {
        _filter.setRelicTier(tier);
        filterUpdated();
      }
    };
    ctrl.addListener(l);
    return ctrl;
  }

  private List<RunicTier> getTiers()
  {
    List<RunicTier> ret=new ArrayList<RunicTier>();
    for(Relic relic : _relics)
    {
      RunicTier tier=relic.getTier();
      if (!ret.contains(tier))
      {
        ret.add(tier);
      }
    }
    Collections.sort(ret,new LotroEnumEntryCodeComparator<RunicTier>());
    return ret;
  }

  private JTextField buildReactiveTextField()
  {
    final JTextField textField=GuiFactory.buildTextField("");
    textField.setColumns(20);
    DocumentListener dl=new DocumentListener()
    {
      @Override
      public void removeUpdate(DocumentEvent e)
      {
        textFieldUpdated(textField);
      }
      @Override
      public void insertUpdate(DocumentEvent e)
      {
        textFieldUpdated(textField);
      }
      @Override
      public void changedUpdate(DocumentEvent e)
      {
        textFieldUpdated(textField);
      }
    };
    textField.getDocument().addDocumentListener(dl);
    return textField;
  }

  private void textFieldUpdated(JTextField textField)
  {
    String text=textField.getText();
    if (text.length()==0) text=null;
    if (textField==_nameContains)
    {
      _filter.setNameFilter(text);
    }
    if (textField==_statsContains)
    {
      _filter.setStatsFilter(text);
    }
    filterUpdated();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // Data
    _filter=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _tier=null;
    _type=null;
    _nameContains=null;
    _statsContains=null;
  }
}
