package delta.games.lotro.gui.lore.agents.mobs;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.enums.AgentClass;
import delta.games.lotro.common.enums.Alignment;
import delta.games.lotro.common.enums.ClassificationFilter;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.LotroEnumEntry;
import delta.games.lotro.common.enums.Species;
import delta.games.lotro.common.enums.SubSpecies;
import delta.games.lotro.common.enums.filter.LotroEnumValueFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.enums.EnumUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.agents.mobs.MobDescription;
import delta.games.lotro.lore.agents.mobs.filter.MobFilter;
import delta.games.lotro.lore.agents.mobs.filter.MobNameFilter;

/**
 * Controller for a mob filter edition panel.
 * @author DAM
 */
public class MobsFilterController implements ActionListener
{
  // Data
  private MobFilter _filter;
  private List<MobDescription> _mobs;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Mob attributes UI --
  private JTextField _contains;
  private ComboBoxController<Alignment> _alignement;
  private ComboBoxController<AgentClass> _agentClass;
  private ComboBoxController<ClassificationFilter> _classification;
  private ComboBoxController<Genus> _genus;
  private ComboBoxController<Species> _species;
  private ComboBoxController<SubSpecies> _subSpecies;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param mobs Managed mobs.
   * @param filterUpdateListener Filter update listener.
   */
  public MobsFilterController(MobFilter filter, List<MobDescription> mobs, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _mobs=mobs;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<MobDescription> getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
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
   * Invoked when the managed filter has been updated.
   */
  protected void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _alignement.selectItem(null);
      _agentClass.selectItem(null);
      _classification.selectItem(null);
      _genus.selectItem(null);
      _species.selectItem(null);
      _subSpecies.selectItem(null);
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    MobNameFilter nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Alignement
    LotroEnumValueFilter<Alignment,MobDescription> alignementFilter=_filter.getAlignmentFilter();
    Alignment alignment=alignementFilter.getValue();
    _alignement.selectItem(alignment);
    // Agent class
    LotroEnumValueFilter<AgentClass,MobDescription> agentClassFilter=_filter.getAgentClassFilter();
    AgentClass agentClass=agentClassFilter.getValue();
    _agentClass.selectItem(agentClass);
    // Classification
    LotroEnumValueFilter<ClassificationFilter,MobDescription> classificationFilter=_filter.getClassificationFilter();
    ClassificationFilter classification=classificationFilter.getValue();
    _classification.selectItem(classification);
    // Genus
    LotroEnumValueFilter<Genus,MobDescription> genusFilter=_filter.getGenusFilter();
    Genus genus=genusFilter.getValue();
    _genus.selectItem(genus);
    // Species
    LotroEnumValueFilter<Species,MobDescription> speciesFilter=_filter.getSpeciesFilter();
    Species species=speciesFilter.getValue();
    _species.selectItem(species);
    // Subspecies
    LotroEnumValueFilter<SubSpecies,MobDescription> subSpeciesFilter=_filter.getSubSpeciesFilter();
    SubSpecies subSpecies=subSpeciesFilter.getValue();
    _subSpecies.selectItem(subSpecies);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Mob attributes
    JPanel mobPanel=buildMobPanel();
    Border border=GuiFactory.buildTitledBorder("Mob"); // 18n
    mobPanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(mobPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);

    // Glue
    Component glue=Box.createGlue();
    c=new GridBagConstraints(2,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(glue,c);
    y++;

    return panel;
  }

  private JPanel buildMobPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      containsPanel.add(GuiFactory.buildLabel("Name filter:")); // 18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          MobNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    // Alignment
    {
      line1Panel.add(GuiFactory.buildLabel("Alignment:")); // 18n
      _alignement=buildAlignmentCombo(MobsUiUtils.getAlignments(_mobs),_filter.getAlignmentFilter());
      line1Panel.add(_alignement.getComboBox());
    }
    // Agent class
    {
      line1Panel.add(GuiFactory.buildLabel("Class:")); // 18n
      _agentClass=buildAlignmentCombo(MobsUiUtils.getAgentClasses(_mobs),_filter.getAgentClassFilter());
      line1Panel.add(_agentClass.getComboBox());
    }
    // Classification
    {
      line1Panel.add(GuiFactory.buildLabel("Classification:")); // 18n
      _classification=buildAlignmentCombo(MobsUiUtils.getClassifications(_mobs),_filter.getClassificationFilter());
      line1Panel.add(_classification.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Genus
    {
      line2Panel.add(GuiFactory.buildLabel("Genus:")); // 18n
      _genus=buildAlignmentCombo(MobsUiUtils.getGenuses(_mobs),_filter.getGenusFilter());
      line2Panel.add(_genus.getComboBox());
    }
    // Species
    {
      line2Panel.add(GuiFactory.buildLabel("Species:")); // 18n
      _species=buildAlignmentCombo(MobsUiUtils.getSpecies(_mobs),_filter.getSpeciesFilter());
      line2Panel.add(_species.getComboBox());
    }
    // SubSpecies
    {
      line2Panel.add(GuiFactory.buildLabel("Subspecies:")); // 18n
      _subSpecies=buildAlignmentCombo(MobsUiUtils.getSubspecies(_mobs),_filter.getSubSpeciesFilter());
      line2Panel.add(_subSpecies.getComboBox());
    }
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
    y++;

    return panel;
  }

  private <T extends LotroEnumEntry> ComboBoxController<T> buildAlignmentCombo(List<T> values, final LotroEnumValueFilter<T,MobDescription> filter)
  {
    ComboBoxController<T> ret=EnumUiUtils.buildEnumCombo(values,true);
    ItemSelectionListener<T> listener=new ItemSelectionListener<T>()
    {
      @Override
      public void itemSelected(T selected)
      {
        filter.setValue(selected);
        filterUpdated();
      }
    };
    ret.addListener(listener);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_alignement!=null)
    {
      _alignement.dispose();
      _alignement=null;
    }
    if (_agentClass!=null)
    {
      _agentClass.dispose();
      _agentClass=null;
    }
    if (_classification!=null)
    {
      _classification.dispose();
      _classification=null;
    }
    if (_genus!=null)
    {
      _genus.dispose();
      _genus=null;
    }
    if (_species!=null)
    {
      _species.dispose();
      _species=null;
    }
    if (_subSpecies!=null)
    {
      _subSpecies.dispose();
      _subSpecies=null;
    }
    _contains=null;
    _reset=null;
  }
}
