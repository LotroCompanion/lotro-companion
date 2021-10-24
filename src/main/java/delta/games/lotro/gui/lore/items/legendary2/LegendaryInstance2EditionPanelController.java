package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.Config;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryConstants;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;
import delta.games.lotro.lore.items.legendary2.LegendaryInstanceAttrs2;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.SocketsSetup;
import delta.games.lotro.lore.items.legendary2.SocketsSetupInstance;
import delta.games.lotro.lore.items.legendary2.global.LegendarySystem2;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * Panel to edit the traceries of an instance of legendary item (reloaded).
 * @author DAM
 */
public class LegendaryInstance2EditionPanelController
{
  // Data
  private int _characterLevel;
  private int _itemLevel;
  // GUI
  private JPanel _panel;
  private ComboBoxController<Integer> _reforgeLevelCombo;
  private JTextField _name;
  private JLabel _itemLevelLabel;
  private JPanel _traceriesPanel;
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
    Integer characterLevel=parent.getContextProperty(ContextPropertyNames.CHARACTER_LEVEL,Integer.class);
    _characterLevel=(characterLevel!=null)?characterLevel.intValue():Config.getInstance().getMaxCharacterLevel();
    Integer itemLevel=item.getEffectiveItemLevel();
    _itemLevel=(itemLevel!=null)?itemLevel.intValue():1;
    _itemLevelLabel=GuiFactory.buildLabel(String.valueOf(_itemLevel));
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
      SingleTraceryEditionController editor=new SingleTraceryEditionController(_parent,entry,_characterLevel,_itemLevel);
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
      updateEditorsFromData();
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
    // Reforge
    JPanel reforgePanel=buildReforgePanel();
    reforgePanel.setBorder(GuiFactory.buildTitledBorder("Reforge"));
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(reforgePanel,c);
    // Traceries
    _traceriesPanel=GuiFactory.buildPanel(new GridBagLayout());
    _traceriesPanel.setBorder(GuiFactory.buildTitledBorder("Traceries"));
    fillTraceriesPanel();
    c=new GridBagConstraints(0,2,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_traceriesPanel,c);
    initListeners();
    return panel;
  }

  private JPanel buildAttributesPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Name
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Inscription:"),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    ret.add(_name,c);
    // Item level
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Item Level:"),c);
    c=new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    ret.add(_itemLevelLabel,c);
    return ret;
  }

  private JPanel buildReforgePanel()
  {
    _reforgeLevelCombo=buildLevelCombo();
    JButton reforgeButton=GuiFactory.buildButton("Reforge");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doReforge();
      }
    };
    reforgeButton.addActionListener(al);
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Character level:"),c);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    ret.add(_reforgeLevelCombo.getComboBox(),c);
    c=new GridBagConstraints(2,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,0,5,5),0,0);
    ret.add(reforgeButton,c);
    return ret;
  }

  private ComboBoxController<Integer> buildLevelCombo()
  {
    List<Integer> levels=new ArrayList<Integer>();
    for(int i=1;i<=_characterLevel;i++)
    {
      levels.add(Integer.valueOf(i));
    }
    ComboBoxController<Integer> ctrl=SharedUiUtils.buildIntegerCombo(levels,false);
    ctrl.selectItem(Integer.valueOf(_characterLevel));
    return ctrl;
  }

  private void doReforge()
  {
    int reforgeLevel=_reforgeLevelCombo.getSelectedItem().intValue();
    int itemLevel=LegendarySystem2.getInstance().getItemLevelForCharacterLevel(reforgeLevel);
    _itemLevel=itemLevel;
    _itemLevelLabel.setText(String.valueOf(itemLevel));
    for(SingleTraceryEditionController editor : _editors)
    {
      editor.setLIItemLevel(_itemLevel);
      editor.setUiFromTracery();
    }
    fillTraceriesPanel();
    updateWindow();
  }

  private void fillTraceriesPanel()
  {
    _traceriesPanel.removeAll();
    int y=0;
    // Headers
    {
      int x=1;
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      _traceriesPanel.add(buildCenteredLabel("Stats"),c);
      x++;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      _traceriesPanel.add(buildCenteredLabel("Item Level"),c);
      x++;
      y++;
      Component valueStrut=Box.createHorizontalStrut(200);
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      _traceriesPanel.add(valueStrut,c);
    }
    y++;
    // Rows
    int nbEditors=_editors.size();
    for(int i=0;i<nbEditors;i++)
    {
      int x=0;
      SingleTraceryEditionController editor=_editors.get(i);
      boolean enabled=editor.isEnabled(_itemLevel);
      if (!enabled)
      {
        continue;
      }
      // Icon
      JLabel icon=editor.getIcon();
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      _traceriesPanel.add(icon,c);
      x++;
      // Value
      MultilineLabel2 valueLabel=editor.getValueLabel();
      c=new GridBagConstraints(x,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      _traceriesPanel.add(valueLabel,c);
      x++;
      // Item level
      ComboBoxController<Integer> currentLevelCombo=editor.getCurrentLevelCombo();
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,0),0,0);
      _traceriesPanel.add(currentLevelCombo.getComboBox(),c);
      x++;
      // Button 'Choose'
      JButton chooseButton=editor.getChooseButton();
      if (chooseButton!=null)
      {
        c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        _traceriesPanel.add(chooseButton,c);
      }
      x++;
      // Button 'Delete'
      JButton deleteButton=editor.getDeleteButton();
      if (deleteButton!=null)
      {
        c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
        _traceriesPanel.add(deleteButton,c);
      }
      x++;
      y++;
    }
  }

  private void initListeners()
  {
    int nbEditors=_editors.size();
    for(int i=0;i<nbEditors;i++)
    {
      final SingleTraceryEditionController editor=_editors.get(i);
      // Choose button
      {
        JButton chooseButton=editor.getChooseButton();
        ActionListener listener=new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            boolean done=editor.handleChooseTracery();
            if (done)
            {
              updateWindow();
            }
          }
        };
        chooseButton.addActionListener(listener);
      }
      // Delete button
      JButton deleteButton=editor.getDeleteButton();
      {
        ActionListener listener=new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            editor.handleDeleteTracery();
            updateWindow();
          }
        };
        deleteButton.addActionListener(listener);
      }
      // Level chooser
      ItemSelectionListener<Integer> currentLevelListener=new ItemSelectionListener<Integer>()
      {
        @Override
        public void itemSelected(Integer item)
        {
          if (item!=null)
          {
            editor.handleCurrentLevelUpdate(item.intValue());
            updateWindow();
          }
        }
      };
      ComboBoxController<Integer> currentLevel=editor.getCurrentLevelCombo();
      currentLevel.addListener(currentLevelListener);
    }
  }

  private JLabel buildCenteredLabel(String text)
  {
    JLabel label=GuiFactory.buildLabel(text);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    return label;
  }

  /**
   * Update editors from the managed data.
   */
  private void updateEditorsFromData()
  {
    for(SingleTraceryEditionController editor : _editors)
    {
      editor.setLIItemLevel(_itemLevel);
      editor.setUiFromTracery();
    }
    updateWindow();
  }

  private void updateWindow()
  {
    if (_parent!=null)
    {
      _parent.getWindow().pack();
    }
  }

  /**
   * Check input.
   * @return <code>true</code> if OK, <code>false</code> otherwise.
   */
  public boolean checkInput()
  {
    for(SingleTraceryEditionController editor : _editors)
    {
      boolean checkInput=editor.checkInput();
      if (!checkInput)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Extract data from UI to the given storage.
   * @param itemInstance Item instance.
   * @param attrs Storage to use.
   */
  public void getData(ItemInstance<? extends Item> itemInstance, LegendaryInstanceAttrs2 attrs)
  {
    // Item level
    Item reference=itemInstance.getReference();
    Integer refItemLevel=reference.getItemLevel();
    Integer itemLevel=Integer.valueOf(_itemLevel);
    if (Objects.equals(itemLevel,refItemLevel)) itemLevel=null;
    itemInstance.setItemLevel(itemLevel);
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
