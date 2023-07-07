package delta.games.lotro.gui.lore.loot.instances.filter;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.common.enums.Difficulty;
import delta.games.lotro.common.enums.GroupSize;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.enums.EnumUiUtils;

/**
 * Controller for a panel to edit instance parameters.
 * @author DAM
 */
public class InstanceParametersController
{
  // Data
  private InstanceParameters _parameters;
  private InstanceLootParametersConfiguration _cfg;
  // Controllers
  private ComboBoxController<Difficulty> _difficulty;
  private ComboBoxController<GroupSize> _size;
  private ComboBoxController<Integer> _level;
  // GUI
  private JPanel _panel;
  // Listeners
  private FilterUpdateListener _updateListener;

  /**
   * Constructor.
   * @param parameters Parameters to use.
   * @param cfg Configuration to use.
   * @param updateListener Update listener.
   */
  public InstanceParametersController(InstanceParameters parameters, InstanceLootParametersConfiguration cfg, FilterUpdateListener updateListener)
  {
    _parameters=parameters;
    _cfg=cfg;
    _updateListener=updateListener;
  }

  /**
   * Get the current parameters.
   * @return some parameters.
   */
  public InstanceParameters getParameters()
  {
    return _parameters;
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
      setParameters();
      parametersUpdated();
    }
    return _panel;
  }

  /**
   * Invoked when the managed parameters has been updated.
   */
  protected void parametersUpdated()
  {
    if (_updateListener!=null)
    {
      _updateListener.filterUpdated();
    }
  }

  private void setParameters()
  {
    // Difficulty
    Difficulty difficulty=_parameters.getDifficulty();
    _difficulty.selectItem(difficulty);
    // Group size
    GroupSize groupSize=_parameters.getSize();
    _size.selectItem(groupSize);
    // Level
    int level=_parameters.getLevel();
    _level.selectItem(Integer.valueOf(level));
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Parameters
    JPanel parametersPanel=buildParametersPanel();
    Border border=GuiFactory.buildTitledBorder("Parameters"); // I18n
    parametersPanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(parametersPanel,c);

    // Glue
    Component glue=Box.createGlue();
    c=new GridBagConstraints(2,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(glue,c);
    y++;

    return panel;
  }

  private JPanel buildParametersPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Difficulty
    {
      line1Panel.add(GuiFactory.buildLabel("Difficulty:")); // I18n
      _difficulty=buildDifficultyCombo();
      line1Panel.add(_difficulty.getComboBox());
    }
    // Group size
    {
      line1Panel.add(GuiFactory.buildLabel("Size:")); // I18n
      _size=buildGroupSizeCombo();
      line1Panel.add(_size.getComboBox());
    }
    // Level
    {
      line1Panel.add(GuiFactory.buildLabel("Level:")); // I18n
      _level=buildLevelCombo();
      line1Panel.add(_level.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    return panel;
  }

  private ComboBoxController<Difficulty> buildDifficultyCombo()
  {
    List<Difficulty> values=_cfg.getDifficulties();
    ComboBoxController<Difficulty> ret=EnumUiUtils.buildEnumCombo(values,false);
    ret.setSelectedItem(values.get(0));
    ItemSelectionListener<Difficulty> listener=new ItemSelectionListener<Difficulty>()
    {
      @Override
      public void itemSelected(Difficulty selected)
      {
        _parameters.setDifficulty(selected);
        parametersUpdated();
      }
    };
    ret.addListener(listener);
    return ret;
  }

  private ComboBoxController<GroupSize> buildGroupSizeCombo()
  {
    List<GroupSize> values=_cfg.getGroupSizes();
    ComboBoxController<GroupSize> ret=EnumUiUtils.buildEnumCombo(values,false);
    ret.setSelectedItem(values.get(0));
    ItemSelectionListener<GroupSize> listener=new ItemSelectionListener<GroupSize>()
    {
      @Override
      public void itemSelected(GroupSize selected)
      {
        _parameters.setSize(selected);
        parametersUpdated();
      }
    };
    ret.addListener(listener);
    return ret;
  }

  private ComboBoxController<Integer> buildLevelCombo()
  {
    List<Integer> values=_cfg.getLevels();
    ComboBoxController<Integer> ret=SharedUiUtils.buildIntegerCombo(values,false);
    ret.setSelectedItem(values.get(0));
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer selected)
      {
        _parameters.setLevel(selected.intValue());
        parametersUpdated();
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
    _parameters=null;
    _cfg=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_difficulty!=null)
    {
      _difficulty.dispose();
      _difficulty=null;
    }
    if (_size!=null)
    {
      _size.dispose();
      _size=null;
    }
    if (_level!=null)
    {
      _level.dispose();
      _level=null;
    }
  }
}
