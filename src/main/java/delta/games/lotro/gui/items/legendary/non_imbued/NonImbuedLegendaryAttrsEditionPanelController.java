package delta.games.lotro.gui.items.legendary.non_imbued;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary.LegendaryConstants;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacyInstance;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacyInstance;

/**
 * Panel to edit the attributes of a non-imbued legendary item instance (upgrades, level, points, legacies).
 * @author DAM
 */
public class NonImbuedLegendaryAttrsEditionPanelController
{
  // Data
  private NonImbuedLegendaryInstanceAttrs _attrs;
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private ComboBoxController<Integer> _upgrades;
  private ComboBoxController<Integer> _level;
  private IntegerEditionController _availablePoints;
  private IntegerEditionController _spentPoints;
  private SingleDefaultNonImbuedLegacyEditionController _defaultLegacyEditor;
  private List<SingleTieredNonImbuedLegacyEditionController> _legacyEditors;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param attrs Attributes to edit.
   * @param constraints Constraints.
   */
  public NonImbuedLegendaryAttrsEditionPanelController(WindowController parent, NonImbuedLegendaryInstanceAttrs attrs, ClassAndSlot constraints)
  {
    _parent=parent;
    _attrs=attrs;
    // Gadgets
    _upgrades=buildUpgradesCombos();
    _level=buildLevelsCombos();
    _availablePoints=buildIntegerEditionController(0,2000);
    _spentPoints=buildIntegerEditionController(0,2000);
    // Legacies
    _legacyEditors=new ArrayList<SingleTieredNonImbuedLegacyEditionController>();
    // - default legacy
    _defaultLegacyEditor=new SingleDefaultNonImbuedLegacyEditionController(parent,constraints);
    // - tiered legacies
    for(int i=0;i<LegendaryConstants.MAX_LEGACIES;i++)
    {
      SingleTieredNonImbuedLegacyEditionController tieredEditor=new SingleTieredNonImbuedLegacyEditionController(_parent,constraints);
      _legacyEditors.add(tieredEditor);
    }
  }

  /**
   * Setup reference data.
   * @param itemLevel Item level of the edited item instance.
   * @param item Reference item.
   */
  public void setReferenceData(int itemLevel, Item item)
  {
    _defaultLegacyEditor.setReferenceData(itemLevel,item);
    for(SingleTieredNonImbuedLegacyEditionController tieredEditor : _legacyEditors)
    {
      tieredEditor.setReferenceData(itemLevel,item);
    }
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
      setUiFromData();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Attributes
    JPanel attributesPanel=buildAttrsPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(attributesPanel,c);
    // Legacies
    JPanel legaciesPanel=buildLegaciesPanel();
    legaciesPanel.setBorder(GuiFactory.buildTitledBorder("Legacies"));
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(legaciesPanel,c);
    return panel;
  }

  private JPanel buildAttrsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // Upgrades
    panel.add(GuiFactory.buildLabel("Upgrades:"));
    panel.add(_upgrades.getComboBox());
    // Level
    panel.add(GuiFactory.buildLabel("Level:"));
    panel.add(_level.getComboBox());
    // Points
    panel.add(GuiFactory.buildLabel("Available:"));
    panel.add(_availablePoints.getTextField());
    panel.add(GuiFactory.buildLabel("Spent:"));
    panel.add(_spentPoints.getTextField());
    return panel;
  }

  private JPanel buildLegaciesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Headers
    {
      int x=1;
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(buildCenteredLabel("Stats"),c);
      x++;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(buildCenteredLabel("Rank"),c);
      x++;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(buildCenteredLabel("Tier"),c);
      x++;
      y++;
    }
    // Rows
    addLegacyLine(_defaultLegacyEditor,panel,y);
    y++;
    int nbEditors=_legacyEditors.size();
    for(int i=0;i<nbEditors;i++)
    {
      SingleTieredNonImbuedLegacyEditionController editor=_legacyEditors.get(i);
      addLegacyLine(editor,panel,y);
      y++;
    }
    return panel;
  }

  private void addLegacyLine(SingleNonImbuedLegacyEditionController<?> editor, JPanel panel, int y)
  {
    int x=0;
    // Icon
    JLabel icon=editor.getIcon();
    GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(icon,c);
    x++;
    // Value
    MultilineLabel2 valueLabel=editor.getValueLabel();
    c=new GridBagConstraints(x,y,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(valueLabel,c);
    x++;
    // Current rank
    ComboBoxController<Integer> currentRank=editor.getCurrentRankEditionController();
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,0),0,0);
    panel.add(currentRank.getComboBox(),c);
    x++;
    // Tier
    if (editor instanceof SingleTieredNonImbuedLegacyEditionController)
    {
      SingleTieredNonImbuedLegacyEditionController tieredEditor=(SingleTieredNonImbuedLegacyEditionController)editor;
      ComboBoxController<Integer> tierCombo=tieredEditor.getTierCombo();
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(tierCombo.getComboBox(),c);
      x++;
      // Button
      JButton chooseButton=tieredEditor.getChooseButton();
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(chooseButton,c);
      x++;
    }
  }

  private JLabel buildCenteredLabel(String text)
  {
    JLabel label=GuiFactory.buildLabel(text);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    return label;
  }

  private ComboBoxController<Integer> buildUpgradesCombos()
  {
    ComboBoxController<Integer> ret=new ComboBoxController<Integer>();
    ret.addItem(Integer.valueOf(0),"None");
    ret.addItem(Integer.valueOf(1),"1 Star");
    ret.addItem(Integer.valueOf(2),"2 Stars");
    ret.addItem(Integer.valueOf(3),"3 Stars");
    return ret;
  }

  private ComboBoxController<Integer> buildLevelsCombos()
  {
    ComboBoxController<Integer> ret=new ComboBoxController<Integer>();
    for(int i=1;i<=LegendaryConstants.MAX_LEVEL;i++)
    {
      ret.addItem(Integer.valueOf(i),String.valueOf(i));
    }
    return ret;
  }

  private IntegerEditionController buildIntegerEditionController(int min, int max)
  {
    JTextField textField=GuiFactory.buildTextField("");
    IntegerEditionController editor=new IntegerEditionController(textField,3);
    editor.setValueRange(Integer.valueOf(min),Integer.valueOf(max));
    return editor;
  }

  /**
   * Update UI from the managed data.
   */
  private void setUiFromData()
  {
    // Attributes
    int nbUpgrades=_attrs.getNbUpgrades();
    _upgrades.selectItem(Integer.valueOf(nbUpgrades));
    int level=_attrs.getLegendaryItemLevel();
    _level.selectItem(Integer.valueOf(level));
    _availablePoints.setValue(Integer.valueOf(_attrs.getPointsLeft()));
    _spentPoints.setValue(Integer.valueOf(_attrs.getPointsSpent()));
    // Legacies
    DefaultNonImbuedLegacyInstance defaultLegacy=_attrs.getDefaultLegacy();
    _defaultLegacyEditor.setLegacyInstance(defaultLegacy);
    List<TieredNonImbuedLegacyInstance> legacies=_attrs.getLegacies();
    for(int i=0;i<LegendaryConstants.MAX_LEGACIES;i++)
    {
      SingleTieredNonImbuedLegacyEditionController editor=_legacyEditors.get(i);
      TieredNonImbuedLegacyInstance legacy=legacies.get(i);
      editor.setLegacyInstance(legacy);
    }
  }

  /**
   * Extract data from UI to the given storage.
   * @param attrs Storage to use.
   */
  public void getData(NonImbuedLegendaryInstanceAttrs attrs)
  {
    // Attributes
    // - upgrades
    Integer nbUpgrades=_upgrades.getSelectedItem();
    attrs.setNbUpgrades(nbUpgrades.intValue());
    // - level
    Integer level=_level.getSelectedItem();
    attrs.setLegendaryItemLevel(level.intValue());
    // - spent points
    Integer spentPoints=_spentPoints.getValue();
    if (spentPoints!=null)
    {
      attrs.setPointsSpent(spentPoints.intValue());
    }
    // - available points
    Integer availablePoints=_availablePoints.getValue();
    if (availablePoints!=null)
    {
      attrs.setPointsLeft(availablePoints.intValue());
    }
    // Legacies
    _defaultLegacyEditor.getData(attrs.getDefaultLegacy());
    List<TieredNonImbuedLegacyInstance> legacies=_attrs.getLegacies();
    for(int i=0;i<LegendaryConstants.MAX_LEGACIES;i++)
    {
      SingleTieredNonImbuedLegacyEditionController editor=_legacyEditors.get(i);
      TieredNonImbuedLegacyInstance legacy=legacies.get(i);
      editor.getData(legacy);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _attrs=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
    if (_upgrades!=null)
    {
      _upgrades.dispose();
      _upgrades=null;
    }
    if (_level!=null)
    {
      _level.dispose();
      _level=null;
    }
    if (_availablePoints!=null)
    {
      _availablePoints.dispose();
      _availablePoints=null;
    }
    if (_spentPoints!=null)
    {
      _spentPoints.dispose();
      _spentPoints=null;
    }
    if (_defaultLegacyEditor!=null)
    {
      _defaultLegacyEditor.dispose();
      _defaultLegacyEditor=null;
    }
    if (_legacyEditors!=null)
    {
      for(SingleNonImbuedLegacyEditionController<?> editor : _legacyEditors)
      {
        editor.dispose();
      }
      _legacyEditors.clear();
      _legacyEditors=null;
    }
  }
}
