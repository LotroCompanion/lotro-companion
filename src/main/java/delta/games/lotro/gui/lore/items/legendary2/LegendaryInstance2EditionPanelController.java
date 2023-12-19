package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.Component;
import java.awt.FlowLayout;
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
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.gear.CharacterGear;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.lore.items.legendary2.traceries.TraceriesConstraintsMgr;
import delta.games.lotro.gui.lore.items.legendary2.traceries.TraceryUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;
import delta.games.lotro.lore.items.legendary2.LegendaryInstanceAttrs2;
import delta.games.lotro.lore.items.legendary2.SocketEntry;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.SocketsSetup;
import delta.games.lotro.lore.items.legendary2.SocketsSetupInstance;
import delta.games.lotro.lore.items.legendary2.TraceriesManager;
import delta.games.lotro.lore.items.legendary2.Tracery;
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
  private int _minLevel;
  // GUI
  private JPanel _panel;
  private JTextField _name;
  private JLabel _itemLevelLabel;
  private JLabel _minLevelLabel;
  private JLabel _nextReforgeLabel;
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
    // Character level
    Integer characterLevel=parent.getContextProperty(ContextPropertyNames.CHARACTER_LEVEL,Integer.class);
    _characterLevel=(characterLevel!=null)?characterLevel.intValue():Config.getInstance().getMaxCharacterLevel();
    // Item level
    Integer itemLevel=item.getEffectiveItemLevel();
    _itemLevel=(itemLevel!=null)?itemLevel.intValue():1;
    _itemLevelLabel=GuiFactory.buildLabel(String.valueOf(_itemLevel));
    // Min level
    Integer minLevel=item.getEffectiveMinLevel();
    _minLevel=(minLevel!=null)?minLevel.intValue():1;
    _minLevelLabel=GuiFactory.buildLabel(String.valueOf(_minLevel));
    // Next reforge
    _nextReforgeLabel=GuiFactory.buildLabel("");
    updateNextReforgeLabel();

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
      SocketEntryInstance clone=new SocketEntryInstance(entry);
      SingleTraceryEditionController editor=new SingleTraceryEditionController(_parent,clone,_characterLevel,_itemLevel);
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
    // Buttons
    JPanel buttonsPanel=buildButtonsPanel();
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(buttonsPanel,c);
    // Traceries
    _traceriesPanel=GuiFactory.buildPanel(new GridBagLayout());
    _traceriesPanel.setBorder(GuiFactory.buildTitledBorder("Traceries"));
    fillTraceriesPanel();
    c=new GridBagConstraints(0,2,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_traceriesPanel,c);
    initListeners();
    return panel;
  }

  private JPanel buildButtonsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    JButton reforgeButton=buildReforgeButton();
    ret.add(reforgeButton);
    JButton minButton=buildMinButton();
    ret.add(minButton);
    JButton maxButton=buildMaxButton();
    ret.add(maxButton);
    return ret;
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
    // Min level
    c=new GridBagConstraints(0,2,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Min Level:"),c);
    c=new GridBagConstraints(1,2,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    ret.add(_minLevelLabel,c);
    // Next reforge
    c=new GridBagConstraints(0,3,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Next reforge:"),c);
    c=new GridBagConstraints(1,3,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    ret.add(_nextReforgeLabel,c);
    return ret;
  }

  private JButton buildReforgeButton()
  {
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
    return reforgeButton;
  }

  private void doReforge()
  {
    int reforgeLevel=_characterLevel;
    int itemLevel=LegendarySystem2.getInstance().getItemLevelForCharacterLevel(reforgeLevel);
    int maxItemLevel=LotroCoreConfig.getInstance().getMaxItemLevelForLI();
    int nextItemLevel=Math.min(itemLevel,maxItemLevel);
    if (nextItemLevel==_itemLevel)
    {
      return;
    }
    _itemLevel=nextItemLevel;
    _itemLevelLabel.setText(String.valueOf(nextItemLevel));
    _minLevel=reforgeLevel;
    _minLevelLabel.setText(String.valueOf(_minLevel));
    updateNextReforgeLabel();
    for(SingleTraceryEditionController editor : _editors)
    {
      editor.setLIItemLevel(_itemLevel);
      editor.setUiFromTracery();
    }
    fillTraceriesPanel();
    updateWindow();
  }

  private void updateNextReforgeLabel()
  {
    String text=buildNextReforgeLabel();
    _nextReforgeLabel.setText(text);
  }

  private String buildNextReforgeLabel()
  {
    int maxLevel=Config.getInstance().getMaxCharacterLevel();
    for(int level=_minLevel;level<=maxLevel;level++)
    {
      int itemLevel=LegendarySystem2.getInstance().getItemLevelForCharacterLevel(level);
      int maxItemLevel=LotroCoreConfig.getInstance().getMaxItemLevelForLI();
      int nextItemLevel=Math.min(itemLevel,maxItemLevel);
      if (nextItemLevel>_itemLevel)
      {
        return "at "+level+" (item level "+nextItemLevel+")";
      }
    }
    return "none";
  }

  private JButton buildMinButton()
  {
    JButton minButton=GuiFactory.buildButton("Min");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doMin();
      }
    };
    minButton.addActionListener(al);
    return minButton;
  }

  private JButton buildMaxButton()
  {
    JButton maxButton=GuiFactory.buildButton("Max");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doMax();
      }
    };
    maxButton.addActionListener(al);
    return maxButton;
  }

  private void doMin()
  {
    for(SingleTraceryEditionController editor : _editors)
    {
      editor.minimizeLevel();
      editor.setUiFromTracery();
    }
    updateWindow();
  }

  private void doMax()
  {
    for(SingleTraceryEditionController editor : _editors)
    {
      editor.maximizeLevel();
      editor.setUiFromTracery();
    }
    updateWindow();
  }

  private void fillTraceriesPanel()
  {
    _traceriesPanel.removeAll();
    int y=0;
    // Headers
    {
      int x=1;
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
      _traceriesPanel.add(buildCenteredLabel("Stats"),c);
      x++;
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
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
      JButton icon=editor.getIcon();
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
      _traceriesPanel.add(icon,c);
      x++;
      // Value
      MultilineLabel2 valueLabel=editor.getValueLabel();
      c=new GridBagConstraints(x,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      _traceriesPanel.add(valueLabel,c);
      x++;
      // Item level
      ComboBoxController<Integer> currentLevelCombo=editor.getCurrentLevelCombo();
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,5,0),0,0);
      _traceriesPanel.add(currentLevelCombo.getComboBox(),c);
      x++;
      // Button 'Choose'
      JButton chooseButton=editor.getChooseButton();
      if (chooseButton!=null)
      {
        c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,5,0),0,0);
        _traceriesPanel.add(chooseButton,c);
      }
      x++;
      // Button 'Delete'
      JButton deleteButton=editor.getDeleteButton();
      if (deleteButton!=null)
      {
        c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
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
            handleChooseTracery(editor);
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

  private void handleChooseTracery(SingleTraceryEditionController editor)
  {
    List<Tracery> availableTraceries=getAvailableTraceries(editor);
    boolean done=editor.handleChooseTracery(availableTraceries);
    if (done)
    {
      updateWindow();
    }
  }

  private List<Tracery> getAvailableTraceries(SingleTraceryEditionController editor)
  {
    SocketEntryInstance data=editor.getData();
    SocketEntry template=data.getTemplate();
    SocketType type=template.getType();
    List<Tracery> traceries=TraceriesManager.getInstance().getTracery(type,_characterLevel,_itemLevel);

    List<Tracery> availableTraceries=new ArrayList<Tracery>();
    TraceriesConstraintsMgr mgr=buildTraceryChooserContext(editor);
    for(Tracery tracery : traceries)
    {
      if (mgr.canBeUsed(tracery))
      {
        availableTraceries.add(tracery);
      }
    }
    return availableTraceries;
  }

  private TraceriesConstraintsMgr buildTraceryChooserContext(SingleTraceryEditionController currentEditor)
  {
    CharacterData characterData=_parent.getContextProperty(ContextPropertyNames.CHARACTER_DATA,CharacterData.class);
    CharacterGear gear=characterData.getEquipment();
    GearSlot slot=_parent.getContextProperty(ContextPropertyNames.EQUIMENT_SLOT,GearSlot.class);
    TraceriesConstraintsMgr otherLIsMgr=TraceryUtils.build(gear,slot);
    TraceriesConstraintsMgr ret=new TraceriesConstraintsMgr(otherLIsMgr);
    for(SingleTraceryEditionController editor : _editors)
    {
      if (editor==currentEditor)
      {
        continue;
      }
      boolean enabled=editor.isEnabled(_itemLevel);
      if (enabled)
      {
        Tracery tracery=editor.getData().getTracery();
        ret.use(tracery);
      }
    }
    return ret;
  }

  private JLabel buildCenteredLabel(String text)
  {
    JLabel label=GuiFactory.buildLabel(text);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    return label;
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
    Item reference=itemInstance.getReference();
    // Item level
    Integer refItemLevel=reference.getItemLevel();
    Integer itemLevel=Integer.valueOf(_itemLevel);
    if (Objects.equals(itemLevel,refItemLevel)) itemLevel=null;
    itemInstance.setItemLevel(itemLevel);
    itemInstance.updateAutoStats();
    // Minimum level
    Integer refMinLevel=reference.getMinLevel();
    Integer minLevel=Integer.valueOf(_minLevel);
    if (Objects.equals(minLevel,refMinLevel)) minLevel=null;
    itemInstance.setMinLevel(minLevel);
    // Legendary name
    String legendaryName=_name.getText();
    attrs.setLegendaryName(legendaryName);
    // Traceries
    SocketsSetupInstance setup=attrs.getSocketsSetup();
    int nbSlots=setup.getSocketsCount();
    for(int i=0;i<nbSlots;i++)
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
    _itemLevelLabel=null;
    _minLevelLabel=null;
    _nextReforgeLabel=null;
    _traceriesPanel=null;
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
