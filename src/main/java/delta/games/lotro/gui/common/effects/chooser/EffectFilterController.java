package delta.games.lotro.gui.common.effects.chooser;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsManager;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.lore.agents.mobs.MobsUiUtils;
import delta.games.lotro.gui.lore.skills.SkillUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.agents.mobs.MobDescription;
import delta.games.lotro.lore.agents.mobs.MobsManager;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for an effect filter edition panel.
 * @author DAM
 */
public class EffectFilterController extends ObjectFilterPanelController implements ActionListener
{
  // Data
  private EffectChooserFilter _filter;
  private TypedProperties _props;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  private JButton _reset;
  // Controllers
  private DynamicTextEditionController _textController;
  private ComboBoxController<MobDescription> _mob;
  private ComboBoxController<SkillDescription> _skill;

  /**
   * Constructor.
   * @param props Filter state.
   */
  public EffectFilterController(TypedProperties props)
  {
    _filter=new EffectChooserFilter();
    _props=props;
    EffectChooserFilterIo.loadFrom(_filter,props);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<Effect> getFilter()
  {
    return _filter;
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

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      // Name
      if (_contains!=null)
      {
        _contains.setText("");
      }
      // Mob
      _mob.setSelectedItem(null);
      // Skill
      _skill.setSelectedItem(null);
    }
  }

  private void setFilter()
  {
    // Name
    if (_contains!=null)
    {
      String contains=_filter.getNameFilter().getPattern();
      if (contains!=null)
      {
        _contains.setText(contains);
      }
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Line 1: name
    JPanel line1Panel=buildLine1();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);
    // Line 2: skill and mob
    JPanel line2Panel=buildLine2();
    c=new GridBagConstraints(0,1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line2Panel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,0,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);

    return panel;
  }

  private JPanel buildLine1()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Name filter
    initNamePanel(panel);
    return panel;
  }

  private void initNamePanel(JPanel panel)
  {
    JPanel namePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    namePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("effects.filter.name")));
    _contains=GuiFactory.buildTextField("");
    _contains.setColumns(20);
    namePanel.add(_contains);
    TextListener listener=new TextListener()
    {
      @Override
      public void textChanged(String newText)
      {
        if (newText.isEmpty()) newText=null;
        _filter.getNameFilter().setPattern(newText);
        filterUpdated();
      }
    };
    _textController=new DynamicTextEditionController(_contains,listener);
    panel.add(namePanel);
  }

  private JPanel buildLine2()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    // Skill
    initSkill(panel);
    // Mob
    initMob(panel);
    return panel;
  }

  private void initMob(JPanel panel)
  {
    JPanel mobPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    mobPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("effects.filter.mob")));
    _mob=getMobCombo();
    mobPanel.add(_mob.getComboBox());
    panel.add(mobPanel);
  }

  private ComboBoxController<MobDescription> getMobCombo()
  {
    List<MobDescription> mobs=MobsManager.getInstance().getMobs();
    ComboBoxController<MobDescription> ret=MobsUiUtils.builMobsCombo(mobs,true);
    ItemSelectionListener<MobDescription> listener=new ItemSelectionListener<MobDescription>()
    {
      @Override
      public void itemSelected(MobDescription mob)
      {
        _filter.getMobFilter().setMob(mob);
        filterUpdated();
      }
    };
    ret.addListener(listener);
    return ret;
  }

  private void initSkill(JPanel panel)
  {
    JPanel skillPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    skillPanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("effects.filter.skill")));
    _skill=getSkillCombo();
    skillPanel.add(_skill.getComboBox());
    panel.add(skillPanel);
  }

  private ComboBoxController<SkillDescription> getSkillCombo()
  {
    List<SkillDescription> skills=SkillsManager.getInstance().getAll();
    ComboBoxController<SkillDescription> ret=SkillUiUtils.buildSkillsCombo(skills,true);
    ItemSelectionListener<SkillDescription> listener=new ItemSelectionListener<SkillDescription>()
    {
      @Override
      public void itemSelected(SkillDescription skill)
      {
        _filter.getSkillFilter().setSkill(skill);
        filterUpdated();
      }
    };
    ret.addListener(listener);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    if (_props!=null)
    {
      EffectChooserFilterIo.saveTo(_filter,_props);
      _props=null;
    }
    _filter=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_mob!=null)
    {
      _mob.dispose();
      _mob=null;
    }
    if (_skill!=null)
    {
      _skill.dispose();
      _skill=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
    _reset=null;
  }
}
