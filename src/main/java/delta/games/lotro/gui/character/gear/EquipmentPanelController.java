package delta.games.lotro.gui.character.gear;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.gear.CharacterGear;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.character.gear.GearSlotContents;
import delta.games.lotro.character.gear.GearSlotUtils;
import delta.games.lotro.character.gear.GearSlots;
import delta.games.lotro.character.storage.StorageUtils;
import delta.games.lotro.character.storage.StoragesIO;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.io.BagsIo;
import delta.games.lotro.character.storage.stash.ItemsStash;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.character.utils.CharacterGearUpdater;
import delta.games.lotro.gui.lore.items.ItemInstanceEditionWindowController;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.chooser.ItemFilterConfiguration;
import delta.games.lotro.gui.lore.items.chooser.ItemFilterController;
import delta.games.lotro.gui.lore.items.chooser.ItemInstanceChooser;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.filters.ItemSlotFilter;
import delta.games.lotro.utils.ContextPropertyNames;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Controller for equipment panel.
 * @author DAM
 */
public class EquipmentPanelController implements ActionListener
{
  private static final int ICON_SIZE=32;
  private static final Integer ICONS_DEPTH=Integer.valueOf(1);

  private static final int ICON_FRAME_SIZE=3;
  private static final int DELTA_X=38;
  private static final int DELTA_Y=45;
  private static final int DELTA_COLUMNS=52;
  private static final int DELTA_COLUMN_GROUPS=100;
  private static final int X_MARGIN=15;
  private static final int Y_MARGIN=20;
  private static final int Y_START=Y_MARGIN+ICON_FRAME_SIZE;
  private static final int X_COLUMN_1=X_MARGIN+ICON_FRAME_SIZE;
  private static final int X_COLUMN_2=X_COLUMN_1+DELTA_COLUMNS;
  private static final int X_COLUMN_3=X_COLUMN_2+DELTA_COLUMN_GROUPS+DELTA_COLUMNS;
  private static final int X_COLUMN_4=X_COLUMN_3+DELTA_COLUMNS;
  private static final int COLUMNS_WIDTH=X_COLUMN_4+ICON_SIZE+ICON_FRAME_SIZE-(X_COLUMN_1-ICON_FRAME_SIZE);
  private static final int ROW_WIDTH=ICON_FRAME_SIZE+7*DELTA_X+ICON_SIZE+ICON_FRAME_SIZE;
  private static final int X_ROW=X_MARGIN+(COLUMNS_WIDTH-ROW_WIDTH)/2;
  private static final int Y_MARGIN_COLUMNS_ROW=25;
  private static final int Y_ROW=Y_START+DELTA_Y*3+ICON_SIZE+ICON_FRAME_SIZE+Y_MARGIN_COLUMNS_ROW;
  private static final int X_BUTTON=X_COLUMN_2+DELTA_COLUMNS+5;
  private static final int Y_BUTTON=Y_START+DELTA_Y*2;

  private static final String UPDATE_COMMAND="update";
  private static final String EDIT_COMMAND="edit";
  private static final String CHOOSE_COMMAND="choose";
  private static final String CHOOSE_FROM_STASH_COMMAND="chooseFromStash";
  private static final String CHOOSE_FROM_BAGS_COMMAND="chooseFromBags";
  private static final String CHOOSE_FROM_SHARED_VAULT_COMMAND="chooseFromSharedVault";
  private static final String REMOVE_COMMAND="remove";
  private static final String COPY_TO_STASH_COMMAND="toStash";
  private static final String SLOT_SEED="slot_";

  private WindowController _parentWindow;
  private JPanel _panel;
  private JLayeredPane _layeredPane;
  private HashMap<GearSlot,Dimension> _iconPositions;
  private CharacterFile _toon;
  private CharacterData _toonData;
  private HashMap<GearSlot,JButton> _buttons;
  private HashMap<GearSlot,EquipmentSlotIconController> _icons;
  private JPopupMenu _contextMenu;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param toon Parent toon.
   * @param toonData Toon data to display.
   */
  public EquipmentPanelController(WindowController parent, CharacterFile toon, CharacterData toonData)
  {
    _parentWindow=parent;
    _toon=toon;
    _toonData=toonData;
    _buttons=new HashMap<GearSlot,JButton>();
    _icons=new HashMap<GearSlot,EquipmentSlotIconController>();
    _contextMenu=buildContextualMenu();
    initPositions();
  }

  private void initPositions()
  {
    _iconPositions=new HashMap<GearSlot,Dimension>();
    int x=X_COLUMN_1;
    int y=Y_START;
    _iconPositions.put(GearSlots.LEFT_EAR,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.NECK,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.LEFT_WRIST,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.LEFT_FINGER,new Dimension(x,y));
    x=X_COLUMN_2; y=Y_START;
    _iconPositions.put(GearSlots.RIGHT_EAR,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.POCKET,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.RIGHT_WRIST,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.RIGHT_FINGER,new Dimension(x,y));
    x=X_COLUMN_3; y=Y_START;
    _iconPositions.put(GearSlots.HEAD,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.BREAST,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.HANDS,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.LEGS,new Dimension(x,y));
    x=X_COLUMN_4; y=Y_START;
    _iconPositions.put(GearSlots.SHOULDER,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.BACK,new Dimension(x,y));
    y+=DELTA_Y;
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.FEET,new Dimension(x,y));

    x=X_ROW; y=Y_ROW;
    _iconPositions.put(GearSlots.MAIN_MELEE,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.MAIN_HAND_AURA,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.OTHER_MELEE,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.OFF_HAND_AURA,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.RANGED,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.RANGED_AURA,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.TOOL,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.CLASS_ITEM,new Dimension(x,y));
  }

  private JPopupMenu buildContextualMenu()
  {
    JPopupMenu popup=new JPopupMenu();
    // Edit...
    JMenuItem edit=new JMenuItem("Edit..."); // I18n
    edit.setActionCommand(EDIT_COMMAND);
    edit.addActionListener(this);
    popup.add(edit);
    // Choose...
    JMenuItem choose=new JMenuItem("Choose..."); // I18n
    choose.setActionCommand(CHOOSE_COMMAND);
    choose.addActionListener(this);
    popup.add(choose);
    // Choose from bags...
    JMenuItem choosefromBags=new JMenuItem("Choose from bags..."); // I18n
    choosefromBags.setActionCommand(CHOOSE_FROM_BAGS_COMMAND);
    choosefromBags.addActionListener(this);
    popup.add(choosefromBags);
    // Choose from sharedVault...
    JMenuItem choosefromSharedVault=new JMenuItem("Choose from shared vault..."); // I18n
    choosefromSharedVault.setActionCommand(CHOOSE_FROM_SHARED_VAULT_COMMAND);
    choosefromSharedVault.addActionListener(this);
    popup.add(choosefromSharedVault);
    // Choose from stash...
    JMenuItem choosefromStash=new JMenuItem("Choose from stash..."); // I18n
    choosefromStash.setActionCommand(CHOOSE_FROM_STASH_COMMAND);
    choosefromStash.addActionListener(this);
    popup.add(choosefromStash);
    // Remove!
    JMenuItem remove=new JMenuItem("Remove"); // I18n
    remove.setActionCommand(REMOVE_COMMAND);
    remove.addActionListener(this);
    popup.add(remove);
    // Copy to stash
    JMenuItem copyToStash=new JMenuItem("Copy to stash"); // I18n
    copyToStash.setActionCommand(COPY_TO_STASH_COMMAND);
    copyToStash.addActionListener(this);
    popup.add(copyToStash);
    return popup;
  }

  private MouseListener buildRightClickListener()
  {
    class PopClickListener extends MouseAdapter
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }

      private void doPop(MouseEvent e)
      {
        GearSlot slot=findSlotForButton((Component)e.getSource());
        ItemInstance<? extends Item> item=getItemForSlot(slot);
        _contextMenu.getComponent(0).setEnabled(item!=null);
        _contextMenu.show(e.getComponent(),e.getX(),e.getY());
      }
    }
    return new PopClickListener();
  }

    /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private Dimension computeDimensions()
  {
    int width=X_COLUMN_4+ICON_SIZE+ICON_FRAME_SIZE+X_MARGIN;
    int height=Y_ROW+ICON_SIZE+ICON_FRAME_SIZE+Y_MARGIN;
    Dimension d=new Dimension(width,height);
    return d;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(null);
    _layeredPane=new JLayeredPane();
    panel.add(_layeredPane,BorderLayout.CENTER);
    Dimension d=computeDimensions();
    panel.setPreferredSize(d);
    panel.setMinimumSize(d);
    _layeredPane.setSize(d);

    JButton updateButton=GuiFactory.buildButton("Update");
    updateButton.setToolTipText("Update gear using the current items database"); // I18n
    updateButton.setBounds(X_BUTTON,Y_BUTTON,85,30);
    updateButton.setSize(updateButton.getPreferredSize());
    _layeredPane.add(updateButton,ICONS_DEPTH);
    updateButton.setActionCommand(UPDATE_COMMAND);
    updateButton.addActionListener(this);

    MouseListener listener=buildRightClickListener();
    for(GearSlot slot : GearSlot.getAll())
    {
      // Position for item
      Dimension position=_iconPositions.get(slot);
      // Add object icon
      // - icon controller
      EquipmentSlotIconController iconController=new EquipmentSlotIconController(slot);
      _icons.put(slot,iconController);
      Icon icon=iconController.getIcon();
      // - button
      JButton button=new JButton(icon);
      button.setBorderPainted(false);
      button.setMargin(new Insets(0,0,0,0));
      button.setBounds(position.width,position.height,ICON_SIZE,ICON_SIZE);
      _layeredPane.add(button,ICONS_DEPTH);
      _buttons.put(slot,button);
      button.setActionCommand(SLOT_SEED+slot.getKey());
      button.addActionListener(this);
      button.addMouseListener(listener);
      button.setToolTipText("");
    }
    updateIcons();

    return panel;
  }

  /**
   * Update contents.
   */
  private void updateIcons()
  {
    for(GearSlot slot : GearSlot.getAll())
    {
      ItemInstance<? extends Item> itemInstance=getItemForSlot(slot);
      EquipmentSlotIconController iconController=_icons.get(slot);
      iconController.setItem(itemInstance);
      JButton button=_buttons.get(slot);
      Icon icon=iconController.getIcon();
      button.setIcon(icon);
      String tooltipText=iconController.getTooltip();
      button.setToolTipText(tooltipText);
    }
  }

  private ItemInstance<? extends Item> getItemForSlot(GearSlot slot)
  {
    CharacterGear equipment=_toonData.getEquipment();
    ItemInstance<? extends Item> itemInstance=equipment.getItemForSlot(slot);
    return itemInstance;
  }

  private void handleEditItemInstance(GearSlot slot)
  {
    ItemInstance<? extends Item> item=getItemForSlot(slot);
    if (item!=null)
    {
      ItemInstance<? extends Item> editedItem=ItemFactory.cloneInstance(item);
      ItemInstanceEditionWindowController ctrl=new ItemInstanceEditionWindowController(_parentWindow,_toonData.getSummary(),editedItem);
      ctrl.setContextProperty(ContextPropertyNames.EQUIMENT_SLOT,slot);
      ItemInstance<? extends Item> resultItem=ctrl.editModal();
      if (resultItem!=null)
      {
        editedItem.setWearer(_toonData.getSummary());
        CharacterGear equipment=_toonData.getEquipment();
        equipment.getSlotContents(slot,true).setItem(editedItem);
        refreshToon();
      }
    }
  }

  private GearSlot findSlotForButton(Component c)
  {
    for(Map.Entry<GearSlot,JButton> entry : _buttons.entrySet())
    {
      if (c==entry.getValue())
      {
        return entry.getKey();
      }
    }
    return null;
  }

  private void handleChooseItemInstanceFromStash(GearSlot slot)
  {
    ItemInstance<? extends Item> itemInstance=chooseItemInstanceFromStash(slot);
    if (itemInstance!=null)
    {
      CharacterGear equipment=_toonData.getEquipment();
      GearSlotContents contents=equipment.getSlotContents(slot,true);
      ItemInstance<? extends Item> instance=ItemFactory.cloneInstance(itemInstance);
      instance.setWearer(_toonData.getSummary());
      contents.setItem(instance);
      refreshToon();
    }
  }

  private void handleChooseItemInstanceFromBags(GearSlot slot)
  {
    ItemInstance<? extends Item> itemInstance=chooseItemInstanceFromBags(slot);
    if (itemInstance!=null)
    {
      CharacterGear equipment=_toonData.getEquipment();
      GearSlotContents contents=equipment.getSlotContents(slot,true);
      ItemInstance<? extends Item> instance=ItemFactory.cloneInstance(itemInstance);
      instance.setWearer(_toonData.getSummary());
      contents.setItem(instance);
      refreshToon();
    }
  }

  private void handleChooseItemInstanceFromSharedVault(GearSlot slot)
  {
    ItemInstance<? extends Item> itemInstance=chooseItemInstanceFromSharedVault(slot);
    if (itemInstance!=null)
    {
      CharacterGear equipment=_toonData.getEquipment();
      GearSlotContents contents=equipment.getSlotContents(slot,true);
      ItemInstance<? extends Item> instance=ItemFactory.cloneInstance(itemInstance);
      instance.setWearer(_toonData.getSummary());
      contents.setItem(instance);
      refreshToon();
    }
  }

  private void handleChooseItemInstance(GearSlot slot)
  {
    Item item=chooseItem(slot);
    if (item!=null)
    {
      CharacterGear equipment=_toonData.getEquipment();
      GearSlotContents contents=equipment.getSlotContents(slot,true);
      ItemInstance<? extends Item> itemInstance=ItemFactory.buildInstance(item);
      itemInstance.setWearer(_toonData.getSummary());
      contents.setItem(itemInstance);
      refreshToon();
    }
  }

  private ItemInstance<? extends Item> chooseItemInstanceFromStash(GearSlot slot)
  {
    ItemsStash stash=_toon.getStash();
    List<ItemInstance<? extends Item>> items=stash.getAll();
    return chooseItemInstance(items,slot,"ItemFilter_Stash");
  }

  private ItemInstance<? extends Item> chooseItemInstanceFromBags(GearSlot slot)
  {
    BagsManager bags=BagsIo.load(_toon);
    List<ItemInstance<? extends Item>> items=bags.getAllItemInstances();
    return chooseItemInstance(items,slot,"ItemFilter_Bags");
  }

  private ItemInstance<? extends Item> chooseItemInstanceFromSharedVault(GearSlot slot)
  {
    Vault sharedVault=StoragesIO.loadSharedVault(_toon);
    if (sharedVault==null)
    {
      return null;
    }
    List<ItemInstance<? extends Item>> items=StorageUtils.getVaultItems(sharedVault);
    return chooseItemInstance(items,slot,"ItemFilter_SharedVault");
  }

  private ItemInstance<? extends Item> chooseItemInstance(List<ItemInstance<? extends Item>> itemInstances, GearSlot slot, String propsId)
  {
    List<ItemInstance<? extends Item>> selectedInstances=filter(itemInstances,slot);
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.forStashFilter();
    TypedProperties filterProps=_parentWindow.getUserProperties(propsId);
    ItemFilterController filterController=new ItemFilterController(cfg,_toonData.getSummary(),filterProps);
    Filter<Item> filter=filterController.getFilter();
    String id=ItemChooser.ITEM_INSTANCE_CHOOSER_PROPERTIES_ID+"#"+slot.getKey();
    TypedProperties props=_parentWindow.getUserProperties(id);
    ObjectChoiceWindowController<ItemInstance<? extends Item>> chooser=ItemInstanceChooser.buildChooser(_parentWindow,props,selectedInstances,filter,filterController);
    ItemInstance<? extends Item> ret=chooser.editModal();
    return ret;
  }

  private List<ItemInstance<? extends Item>> filter(List<ItemInstance<? extends Item>> itemInstances, GearSlot slot)
  {
    List<ItemInstance<? extends Item>> selectedInstances=new ArrayList<ItemInstance<? extends Item>>();
    ItemSlotFilter filter=new ItemSlotFilter(slot);
    for(ItemInstance<? extends Item> itemInstance : itemInstances)
    {
      Item item=itemInstance.getReference();
      if (filter.accept(item))
      {
        selectedInstances.add(itemInstance);
      }
    }
    return selectedInstances;
  }

  private Item chooseItem(GearSlot slot)
  {
    EquipmentLocation location=GearSlotUtils.getEquipmentSlot(slot);
    List<Item> selectedItems=ItemsManager.getInstance().getItems(location);
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.initFromItems(selectedItems);
    cfg.forItemFilter();
    TypedProperties filterProps=_parentWindow.getUserProperties("ItemFilter");
    ItemFilterController filterController=new ItemFilterController(cfg,_toonData.getSummary(),filterProps);
    Filter<Item> filter=filterController.getFilter();
    String id=ItemChooser.ITEM_CHOOSER_PROPERTIES_ID+"#"+slot.getKey();
    TypedProperties props=_parentWindow.getUserProperties(id);
    ObjectChoiceWindowController<Item> chooser=ItemChooser.buildChooser(_parentWindow,props,selectedItems,filter,filterController);
    Item ret=chooser.editModal();
    return ret;
  }

  private void handleRemoveItemInstance(GearSlot slot)
  {
    CharacterGear equipment=_toonData.getEquipment();
    GearSlotContents contents=equipment.getSlotContents(slot,false);
    if (contents!=null)
    {
      contents.setItem(null);
    }
    refreshToon();
  }

  private void handleCopyToStash(GearSlot slot)
  {
    CharacterGear equipment=_toonData.getEquipment();
    GearSlotContents contents=equipment.getSlotContents(slot,false);
    if (contents!=null)
    {
      ItemInstance<? extends Item> item=contents.getItem();
      if (item!=null)
      {
        ItemsStash stash=_toon.getStash();
        ItemInstance<? extends Item> newItemInstance=ItemFactory.cloneInstance(item);
        newItemInstance.setWearer(null);
        stash.addItem(newItemInstance);
        Integer stashId=newItemInstance.getStashIdentifier();
        item.setStashIdentifier(stashId);
        _toon.saveStash();
        // Broadcast stash update event...
        CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_STASH_UPDATED,_toon,null);
        EventsManager.invokeEvent(event);
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    String cmd=e.getActionCommand();
    if (cmd.startsWith(SLOT_SEED))
    {
      cmd=cmd.substring(SLOT_SEED.length());
      // Straight click
      GearSlot slot=GearSlot.getByKey(cmd);
      if (slot!=null)
      {
        ItemInstance<? extends Item> currentItem=getItemForSlot(slot);
        if (currentItem!=null)
        {
          handleEditItemInstance(slot);
        }
        else
        {
          handleChooseItemInstance(slot);
        }
      }
    }
    else if (cmd.equals(UPDATE_COMMAND))
    {
      CharacterGearUpdater updater=new CharacterGearUpdater();
      updater.updateGear(_toonData);
      refreshToon();
    }
    else
    {
      // From contextual menu
      Component invoker=_contextMenu.getInvoker();
      GearSlot slot=findSlotForButton(invoker);
      if (slot!=null)
      {
        if (EDIT_COMMAND.equals(cmd))
        {
          handleEditItemInstance(slot);
        }
        else if (CHOOSE_COMMAND.equals(cmd))
        {
          handleChooseItemInstance(slot);
        }
        else if (CHOOSE_FROM_STASH_COMMAND.equals(cmd))
        {
          handleChooseItemInstanceFromStash(slot);
        }
        else if (CHOOSE_FROM_BAGS_COMMAND.equals(cmd))
        {
          handleChooseItemInstanceFromBags(slot);
        }
        else if (CHOOSE_FROM_SHARED_VAULT_COMMAND.equals(cmd))
        {
          handleChooseItemInstanceFromSharedVault(slot);
        }
        else if (REMOVE_COMMAND.equals(cmd))
        {
          handleRemoveItemInstance(slot);
        }
        else if (COPY_TO_STASH_COMMAND.equals(cmd))
        {
          handleCopyToStash(slot);
        }
      }
    }
  }

  private void refreshToon()
  {
    updateIcons();
    // Broadcast equipment update event...
    CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,_toon,_toonData);
    EventsManager.invokeEvent(event);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_layeredPane!=null)
    {
      _layeredPane.removeAll();
      _layeredPane=null;
    }
    _parentWindow=null;
    _toon=null;
    _toonData=null;
    _iconPositions.clear();
    _buttons.clear();
    _icons.clear();
  }
}
