package delta.games.lotro.gui.items.legendary.non_imbued;

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
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacyInstance;

/**
 * Panel to edit the attributed of a non-imbued legendary item instance.
 * @author DAM
 */
public class NonImbuedAttrsEditionPanelController
{
  // Data
  //private NonImbuedLegendaryAttrs _attrs;
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<SingleTieredNonImbuedLegacyEditionController> _legacyEditors;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param attrs Attributes to edit.
   * @param constraints Constraints.
   */
  public NonImbuedAttrsEditionPanelController(WindowController parent, NonImbuedLegendaryAttrs attrs, ClassAndSlot constraints)
  {
    _parent=parent;
    //_attrs=attrs;
    polyfillAttrs(attrs);
    _legacyEditors=new ArrayList<SingleTieredNonImbuedLegacyEditionController>();
    List<TieredNonImbuedLegacyInstance> legacies=attrs.getLegacies();
    int nbLegacies=legacies.size();
    for(int i=0;i<nbLegacies;i++)
    {
      TieredNonImbuedLegacyInstance legacy=legacies.get(i);
      SingleTieredNonImbuedLegacyEditionController editor=new SingleTieredNonImbuedLegacyEditionController(_parent,legacy,constraints);
      _legacyEditors.add(editor);
    }
  }

  private void polyfillAttrs(NonImbuedLegendaryAttrs attrs)
  {
    int nbLegacies=attrs.getLegacies().size();
    for(int i=nbLegacies;i<7;i++)
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
      update();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    JPanel legaciesPanel=buildLegaciesPanel();
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
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
      SingleTieredNonImbuedLegacyEditionController editor=_legacyEditors.get(i);
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
      ComboBoxController<Integer> tierCombo=editor.getTierCombo();
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(tierCombo.getComboBox(),c);
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
    for(SingleTieredNonImbuedLegacyEditionController editor : _legacyEditors)
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
    //_attrs=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
    if (_legacyEditors!=null)
    {
      for(SingleTieredNonImbuedLegacyEditionController editor : _legacyEditors)
      {
        editor.dispose();
      }
      _legacyEditors.clear();
      _legacyEditors=null;
    }
  }
}
