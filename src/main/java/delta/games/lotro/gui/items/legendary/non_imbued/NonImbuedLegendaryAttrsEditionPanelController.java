package delta.games.lotro.gui.items.legendary.non_imbued;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
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
  private List<SingleNonImbuedLegacyEditionController<?>> _legacyEditors;

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
    polyfillAttrs(attrs);
    // Gadgets
    _upgrades=buildUpgradesCombos();
    _level=buildLevelsCombos();
    _availablePoints=buildIntegerEditionController(0,2000);
    _spentPoints=buildIntegerEditionController(0,2000);
    // Legacies
    _legacyEditors=new ArrayList<SingleNonImbuedLegacyEditionController<?>>();
    // - default legacy
    DefaultNonImbuedLegacyInstance defaultLegacy=attrs.getDefaultLegacy();
    SingleDefaultNonImbuedLegacyEditionController editor=new SingleDefaultNonImbuedLegacyEditionController(_parent,defaultLegacy,constraints);
    _legacyEditors.add(editor);
    // - tiered legacies
    List<TieredNonImbuedLegacyInstance> legacies=attrs.getLegacies();
    int nbLegacies=legacies.size();
    for(int i=0;i<nbLegacies;i++)
    {
      TieredNonImbuedLegacyInstance legacy=legacies.get(i);
      SingleTieredNonImbuedLegacyEditionController tieredEditor=new SingleTieredNonImbuedLegacyEditionController(_parent,legacy,constraints);
      _legacyEditors.add(tieredEditor);
    }
  }

  private void polyfillAttrs(NonImbuedLegendaryInstanceAttrs attrs)
  {
    int nbLegacies=attrs.getLegacies().size();
    for(int i=nbLegacies;i<LegendaryConstants.MAX_LEGACIES;i++)
    {
      TieredNonImbuedLegacyInstance legacy=new TieredNonImbuedLegacyInstance();
      attrs.addLegacy(legacy);
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Attributes
    JPanel attributesPanel=buildAttrsPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(attributesPanel,c);
    // Legacies
    JPanel legaciesPanel=buildLegaciesPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(legaciesPanel,c);
    return panel;
  }

  private JPanel buildAttrsPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new FlowLayout(FlowLayout.LEFT));
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int y=0;
    // Headers
    {
      // - line 1
      GridBagConstraints c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      Component valueStrut=Box.createHorizontalStrut(200);
      panel.add(valueStrut,c);
      y++;
      // - line 2
      int x=1;
      c=new GridBagConstraints(x,0,1,2,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
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
    int nbEditors=_legacyEditors.size();
    for(int i=0;i<nbEditors;i++)
    {
      int x=0;
      SingleNonImbuedLegacyEditionController<?> editor=_legacyEditors.get(i);
      // Icon
      JLabel icon=editor.getIcon();
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(icon,c);
      x++;
      // Value
      MultilineLabel2 valueLabel=editor.getValueLabel();
      c=new GridBagConstraints(x,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(valueLabel,c);
      x++;
      // Current rank
      IntegerEditionController currentRank=editor.getCurrentRankEditionController();
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,0),0,0);
      panel.add(currentRank.getTextField(),c);
      x++;
      // Tier
      if (editor instanceof SingleTieredNonImbuedLegacyEditionController)
      {
        SingleTieredNonImbuedLegacyEditionController tieredEditor=(SingleTieredNonImbuedLegacyEditionController)editor;
        ComboBoxController<Integer> tierCombo=tieredEditor.getTierCombo();
        c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        panel.add(tierCombo.getComboBox(),c);
      }
      x++;
      // Button
      JButton chooseButton=editor.getChooseButton();
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(chooseButton,c);
      x++;
      y++;
    }
    return panel;
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
    for(SingleNonImbuedLegacyEditionController<?> editor : _legacyEditors)
    {
      editor.setUiFromLegacy();
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
