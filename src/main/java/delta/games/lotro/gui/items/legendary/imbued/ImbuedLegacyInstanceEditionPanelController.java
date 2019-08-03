package delta.games.lotro.gui.items.legendary.imbued;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.lore.items.legendary.LegendaryConstants;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacyInstance;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegendaryAttrs;

/**
 * Panel to edit the legacies of an imbued legendary item instance.
 * @author DAM
 */
public class ImbuedLegacyInstanceEditionPanelController
{
  // Data
  private ImbuedLegendaryAttrs _attrs;
  // GUI
  private JPanel _panel;
  private JLabel _levels;
  // Controllers
  private WindowController _parent;
  private List<SingleImbuedLegacyEditionController> _editors;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param attrs Attributes to edit.
   * @param constraints Constraints.
   */
  public ImbuedLegacyInstanceEditionPanelController(WindowController parent, ImbuedLegendaryAttrs attrs, ClassAndSlot constraints)
  {
    _parent=parent;
    _attrs=attrs;
    polyfillAttrs(attrs);
    _editors=new ArrayList<SingleImbuedLegacyEditionController>();
    int nbLegacies=attrs.getNumberOfLegacies();
    for(int i=0;i<nbLegacies;i++)
    {
      ImbuedLegacyInstance legacy=attrs.getLegacy(i);
      SingleImbuedLegacyEditionController editor=new SingleImbuedLegacyEditionController(_parent,legacy,constraints);
      _editors.add(editor);
    }
  }

  private void polyfillAttrs(ImbuedLegendaryAttrs attrs)
  {
    int nbLegacies=attrs.getNumberOfLegacies();
    for(int i=nbLegacies;i<=LegendaryConstants.MAX_LEGACIES;i++)
    {
      ImbuedLegacyInstance legacy=new ImbuedLegacyInstance();
      attrs.addLegacy(legacy);
    }
  }

  private void updateLevels()
  {
    int currentLevels=_attrs.getTotalTiers();
    int maxLevels=_attrs.getMaxTotalTiers();
    String label="Levels (current / max): "+currentLevels+" / "+maxLevels;
    _levels.setText(label);
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
      update();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    _levels=GuiFactory.buildLabel("? / ?");
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_levels,c);
    JPanel legaciesPanel=buildLegaciesPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(legaciesPanel,c);
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
      c=new GridBagConstraints(2,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(buildCenteredLabel("Levels"),c);
      y++;
      // - line 2
      int x=1;
      c=new GridBagConstraints(x,0,1,2,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(buildCenteredLabel("Stats"),c);
      panel.add(valueStrut,c);
      x++;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(buildCenteredLabel("Current"),c);
      x++;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(buildCenteredLabel("Max"),c);
      x++;
      y++;
    }
    ItemSelectionListener<Integer> levelsListener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer item)
      {
        updateLevels();
      }
    };
    // Rows
    int nbEditors=_editors.size();
    for(int i=0;i<nbEditors;i++)
    {
      int x=0;
      SingleImbuedLegacyEditionController editor=_editors.get(i);
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
      // Current level
      ComboBoxController<Integer> currentLevelCombo=editor.getCurrentLevelCombo();
      currentLevelCombo.addListener(levelsListener);
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,0),0,0);
      panel.add(currentLevelCombo.getComboBox(),c);
      x++;
      // Max level
      ComboBoxController<Integer> maxLevelCombo=editor.getMaxLevelCombo();
      maxLevelCombo.addListener(levelsListener);
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(maxLevelCombo.getComboBox(),c);
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

  /**
   * Update UI from the managed data.
   */
  private void update()
  {
    for(SingleImbuedLegacyEditionController editor : _editors)
    {
      editor.setUiFromLegacy();
    }
    updateLevels();
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
    _levels=null;
    // Controllers
    _parent=null;
    if (_editors!=null)
    {
      for(SingleImbuedLegacyEditionController editor : _editors)
      {
        editor.dispose();
      }
      _editors.clear();
      _editors=null;
    }
  }
}
