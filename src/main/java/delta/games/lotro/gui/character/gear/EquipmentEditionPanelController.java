package delta.games.lotro.gui.character.gear;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import delta.games.lotro.character.gear.PreclusionUtils;
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
import delta.games.lotro.utils.ContextPropertyNames;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Controller a panel to edit the equipment of a character.
 * @author DAM
 */
public class EquipmentEditionPanelController implements ActionListener
{
  private static final Logger LOGGER=LoggerFactory.getLogger(EquipmentEditionPanelController.class);

  private static final String UPDATE_COMMAND="update";
  private static final String EDIT_COMMAND="edit";
  private static final String CHOOSE_COMMAND="choose";
  private static final String CHOOSE_FROM_STASH_COMMAND="chooseFromStash";
  private static final String CHOOSE_FROM_BAGS_COMMAND="chooseFromBags";
  private static final String CHOOSE_FROM_SHARED_VAULT_COMMAND="chooseFromSharedVault";
  private static final String REMOVE_COMMAND="remove";
  private static final String COPY_TO_STASH_COMMAND="toStash";

  private WindowController _parentWindow;
  private EquipmentDisplayPanelController _displayPanel;
  private CharacterFile _toon;
  private CharacterData _toonData;
  private JPopupMenu _contextMenu;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param toon Parent toon.
   * @param toonData Toon data to display.
   */
  public EquipmentEditionPanelController(WindowController parent, CharacterFile toon, CharacterData toonData)
  {
    _parentWindow=parent;
    _toon=toon;
    _toonData=toonData;
    _contextMenu=buildContextualMenu();
    _displayPanel=new EquipmentDisplayPanelController(parent,toonData.getEquipment());
    updatePanel();
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
        GearSlot slot=_displayPanel.findSlotForButton((Component)e.getSource());
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
    return _displayPanel.getPanel();
  }

  private void updatePanel()
  {
    JButton updateButton=GuiFactory.buildButton("Update");
    updateButton.setToolTipText("Update gear using the current items database"); // I18n
    updateButton.setActionCommand(UPDATE_COMMAND);
    updateButton.addActionListener(this);
    _displayPanel.setButton(updateButton);

    MouseListener listener=buildRightClickListener();
    for(GearSlot slot : GearSlot.getAll())
    {
      JButton button=_displayPanel.getButtonForSlot(slot);
      if (button==null)
      {
        continue;
      }
      button.addActionListener(this);
      button.addMouseListener(listener);
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
      CharacterGearUpdater updater=new CharacterGearUpdater(_toon,_toonData);
      updater.updateItem(itemInstance);
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
    CharacterGear equipment=_toonData.getEquipment();
    boolean precluded=PreclusionUtils.slotIsPrecluded(equipment,slot);
    if (precluded)
    {
      LOGGER.info("Cannot use this precluded slot: {}",slot);
      return null;
    }
    List<ItemInstance<? extends Item>> selectedInstances=filter(itemInstances,slot);
    selectedInstances=PreclusionUtils.filterItemInstances(selectedInstances,equipment.getEquippedSlots());
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.forStashFilter();

    TypedProperties filterProps=_parentWindow.getUserProperties(propsId);
    ItemFilterController filterController=new ItemFilterController(cfg,_toonData.getSummary(),filterProps);
    CharacterFile currentToon=_parentWindow.getContextProperty(ContextPropertyNames.CHARACTER_FILE,CharacterFile.class);
    filterController.configure(currentToon);

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
    for(ItemInstance<? extends Item> itemInstance : itemInstances)
    {
      Item item=itemInstance.getReference();
      EquipmentLocation location=item.getEquipmentLocation();
      boolean ok=select(location,slot);
      if (ok)
      {
        selectedInstances.add(itemInstance);
      }
    }
    return selectedInstances;
  }

  private boolean select(EquipmentLocation location, GearSlot slot)
  {
    GearSlot[] allowedSlots=GearSlotUtils.getSlots(location);
    int size=allowedSlots.length;
    if (size==0)
    {
      return false;
    }
    if (size==1)
    {
      return (allowedSlots[0]==slot);
    }
    for(GearSlot allowedSlot : allowedSlots)
    {
      if (allowedSlot==slot)
      {
        return true;
      }
    }
    return false;
  }

  private Item chooseItem(GearSlot slot)
  {
    CharacterGear equipment=_toonData.getEquipment();
    boolean precluded=PreclusionUtils.slotIsPrecluded(equipment,slot);
    if (precluded)
    {
      LOGGER.info("Cannot use this precluded slot: {}",slot);
      return null;
    }
    List<Item> selectedItems=ItemsManager.getInstance().getItems(slot);
    selectedItems=PreclusionUtils.filterItems(selectedItems,equipment.getEquippedSlots());
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.initFromItems(selectedItems);
    cfg.forItemFilter();

    TypedProperties filterProps=_parentWindow.getUserProperties("ItemFilter");
    ItemFilterController filterController=new ItemFilterController(cfg,_toonData.getSummary(),filterProps);
    CharacterFile currentToon=_parentWindow.getContextProperty(ContextPropertyNames.CHARACTER_FILE,CharacterFile.class);
    filterController.configure(currentToon);
    filterController.setSlot(slot);

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
    if (cmd.startsWith(EquipmentDisplayPanelController.SLOT_SEED))
    {
      cmd=cmd.substring(EquipmentDisplayPanelController.SLOT_SEED.length());
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
      CharacterGearUpdater updater=new CharacterGearUpdater(_toon,_toonData);
      updater.updateGear();
      refreshToon();
    }
    else
    {
      // From contextual menu
      Component invoker=_contextMenu.getInvoker();
      GearSlot slot=_displayPanel.findSlotForButton(invoker);
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

  /**
   * Update display.
   */
  public void updateDisplay()
  {
    _displayPanel.updateIcons();
  }

  private void refreshToon()
  {
    // Broadcast equipment update event...
    CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,_toon,_toonData);
    EventsManager.invokeEvent(event);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_displayPanel!=null)
    {
      _displayPanel.dispose();
      _displayPanel=null;
    }
    _parentWindow=null;
    _toon=null;
    _toonData=null;
    _contextMenu=null;
  }
}
