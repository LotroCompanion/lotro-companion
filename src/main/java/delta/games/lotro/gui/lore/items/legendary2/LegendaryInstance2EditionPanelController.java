package delta.games.lotro.gui.lore.items.legendary2;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryConstants;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;
import delta.games.lotro.lore.items.legendary2.LegendaryInstanceAttrs2;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.SocketsSetup;
import delta.games.lotro.lore.items.legendary2.SocketsSetupInstance;

/**
 * Panel to edit the traceries of an instance of legendary item (reloaded).
 * @author DAM
 */
public class LegendaryInstance2EditionPanelController
{
  // GUI
  private JPanel _panel;
  private JTextField _name;
  // Controllers
  private WindowController _parent;
  private List<SingleTraceryEditionController> _editors;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param item Item to edit.
   */
  public LegendaryInstance2EditionPanelController(WindowController parent, ItemInstance<? extends Item> item)
  {
    _parent=parent;
    LegendaryInstance2 legendaryInstance=(LegendaryInstance2)item;
    LegendaryInstanceAttrs2 attrs=legendaryInstance.getLegendaryAttributes();
    // Name
    _name=GuiFactory.buildTextField("");
    String legendaryName=attrs.getLegendaryName();
    _name.setText(legendaryName);
    _editors=new ArrayList<SingleTraceryEditionController>();
    SocketsSetupInstance setupInstance=attrs.getSocketsSetup();
    SocketsSetup setup=setupInstance.getSetupTemplate();
    int nbSockets=setup.getSocketsCount();
    for(int i=0;i<nbSockets;i++)
    {
      SocketEntryInstance entry=setupInstance.getEntry(i);
      SingleTraceryEditionController editor=new SingleTraceryEditionController(_parent,entry);
      _editors.add(editor);
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
    JPanel attributesPanel=buildAttributesPanel();
    attributesPanel.setBorder(GuiFactory.buildTitledBorder("Attributes"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(attributesPanel,c);
    // Traceries
    JPanel traceriesPanel=buildTraceriesPanel();
    traceriesPanel.setBorder(GuiFactory.buildTitledBorder("Traceries"));
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(traceriesPanel,c);
    return panel;
  }

  private JPanel buildAttributesPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Inscription
    // - label
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Inscription:"),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    // - editor
    ret.add(_name,c);
    return ret;
  }

  private JPanel buildTraceriesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Headers
    {
      int x=1;
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(buildCenteredLabel("Stats"),c);
      x++;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(buildCenteredLabel("Item Level"),c);
      x++;
      y++;
      Component valueStrut=Box.createHorizontalStrut(200);
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(valueStrut,c);
    }
    y++;
    // Rows
    int nbEditors=_editors.size();
    for(int i=0;i<nbEditors;i++)
    {
      int x=0;
      SingleTraceryEditionController editor=_editors.get(i);
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
      // Item level
      ComboBoxController<Integer> currentLevelCombo=editor.getCurrentLevelCombo();
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,0),0,0);
      panel.add(currentLevelCombo.getComboBox(),c);
      x++;
      // Button 'Choose'
      JButton chooseButton=editor.getChooseButton();
      if (chooseButton!=null)
      {
        c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        panel.add(chooseButton,c);
      }
      x++;
      // Button 'Delete'
      JButton deleteButton=editor.getDeleteButton();
      if (deleteButton!=null)
      {
        c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        panel.add(deleteButton,c);
      }
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
  private void setUiFromData()
  {
    for(SingleTraceryEditionController editor : _editors)
    {
      editor.setUiFromTracery();
    }
  }

  /**
   * Extract data from UI to the given storage.
   * @param attrs Storage to use.
   */
  public void getData(LegendaryInstanceAttrs2 attrs)
  {
    // Legendary name
    String legendaryName=_name.getText();
    attrs.setLegendaryName(legendaryName);
    // Traceries
    SocketsSetupInstance setup=attrs.getSocketsSetup();
    for(int i=0;i<LegendaryConstants.MAX_LEGACIES+1;i++)
    {
      SingleTraceryEditionController editor = _editors.get(i);
      SocketEntryInstance entry=setup.getEntry(i);
      editor.getData(entry);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _name=null;
    // Controllers
    _parent=null;
    if (_editors!=null)
    {
      for(SingleTraceryEditionController editor : _editors)
      {
        editor.dispose();
      }
      _editors.clear();
      _editors=null;
    }
  }
}
