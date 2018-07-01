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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
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
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.storage.ItemsStash;
import delta.games.lotro.gui.items.ItemEditionWindowController;
import delta.games.lotro.gui.items.chooser.ItemChoiceWindowController;
import delta.games.lotro.gui.items.chooser.ItemFilterConfiguration;
import delta.games.lotro.gui.items.chooser.ItemFilterController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for equipment panel.
 * @author DAM
 */
public class EquipmentPanelController implements ActionListener
{
  private static final int ICON_SIZE=32;
  private static final Integer ICONS_DEPTH=Integer.valueOf(1);

  private static final int ICON_FRAME_SIZE=3;
  private static final int DELTA_X=44;
  private static final int DELTA_Y=45;
  private static final int DELTA_COLUMNS=52;
  private static final int DELTA_COLUMN_GROUPS=50;
  private static final int X_MARGIN=27;
  private static final int Y_MARGIN=20;
  private static final int Y_START=Y_MARGIN+ICON_FRAME_SIZE;
  private static final int X_COLUMN_1=X_MARGIN+ICON_FRAME_SIZE;
  private static final int X_COLUMN_2=X_COLUMN_1+DELTA_COLUMNS;
  private static final int X_COLUMN_3=X_COLUMN_2+DELTA_COLUMN_GROUPS+DELTA_COLUMNS;
  private static final int X_COLUMN_4=X_COLUMN_3+DELTA_COLUMNS;
  private static final int COLUMNS_WIDTH=X_COLUMN_4+ICON_SIZE+ICON_FRAME_SIZE-(X_COLUMN_1-ICON_FRAME_SIZE);
  private static final int ROW_WIDTH=ICON_FRAME_SIZE+4*DELTA_X+ICON_SIZE+ICON_FRAME_SIZE;
  private static final int X_ROW=X_MARGIN+(COLUMNS_WIDTH-ROW_WIDTH)/2;
  private static final int Y_MARGIN_COLUMNS_ROW=25;
  private static final int Y_ROW=Y_START+DELTA_Y*3+ICON_SIZE+ICON_FRAME_SIZE+Y_MARGIN_COLUMNS_ROW;

  private static final String EDIT_COMMAND="edit";
  private static final String CHOOSE_COMMAND="choose";
  private static final String CHOOSE_FROM_STASH_COMMAND="chooseFromStash";
  private static final String REMOVE_COMMAND="remove";
  private static final String COPY_TO_STASH_COMMAND="toStash";
  private static final String SLOT_SEED="slot_";

  private WindowController _parentWindow;
  private JPanel _panel;
  private JLayeredPane _layeredPane;
  private HashMap<EQUIMENT_SLOT,Dimension> _iconPositions;
  private CharacterFile _toon;
  private CharacterData _toonData;
  private HashMap<EQUIMENT_SLOT,JButton> _buttons;
  private HashMap<EQUIMENT_SLOT,EquipmentSlotIconController> _icons;
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
    _buttons=new HashMap<EQUIMENT_SLOT,JButton>();
    _icons=new HashMap<EQUIMENT_SLOT,EquipmentSlotIconController>();
    _contextMenu=buildContextualMenu();
    initPositions();
  }

  private void initPositions()
  {
    _iconPositions=new HashMap<EQUIMENT_SLOT,Dimension>();
    int x=X_COLUMN_1;
    int y=Y_START;
    _iconPositions.put(EQUIMENT_SLOT.LEFT_EAR,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.NECK,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.LEFT_WRIST,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.LEFT_FINGER,new Dimension(x,y));
    x=X_COLUMN_2; y=Y_START;
    _iconPositions.put(EQUIMENT_SLOT.RIGHT_EAR,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.POCKET,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.RIGHT_WRIST,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.RIGHT_FINGER,new Dimension(x,y));
    x=X_COLUMN_3; y=Y_START;
    _iconPositions.put(EQUIMENT_SLOT.HEAD,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.BREAST,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.HANDS,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.LEGS,new Dimension(x,y));
    x=X_COLUMN_4; y=Y_START;
    _iconPositions.put(EQUIMENT_SLOT.SHOULDER,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.BACK,new Dimension(x,y));
    y+=DELTA_Y;
    y+=DELTA_Y;
    _iconPositions.put(EQUIMENT_SLOT.FEET,new Dimension(x,y));

    x=X_ROW; y=Y_ROW;
    _iconPositions.put(EQUIMENT_SLOT.MAIN_MELEE,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(EQUIMENT_SLOT.OTHER_MELEE,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(EQUIMENT_SLOT.RANGED,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(EQUIMENT_SLOT.TOOL,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(EQUIMENT_SLOT.CLASS_ITEM,new Dimension(x,y));
  }

  private JPopupMenu buildContextualMenu()
  {
    JPopupMenu popup=new JPopupMenu();
    // Edit...
    JMenuItem edit=new JMenuItem("Edit...");
    edit.setActionCommand(EDIT_COMMAND);
    edit.addActionListener(this);
    popup.add(edit);
    // Choose...
    JMenuItem choose=new JMenuItem("Choose...");
    choose.setActionCommand(CHOOSE_COMMAND);
    choose.addActionListener(this);
    popup.add(choose);
    // Choose from stash...
    JMenuItem choosefromStash=new JMenuItem("Choose from stash...");
    choosefromStash.setActionCommand(CHOOSE_FROM_STASH_COMMAND);
    choosefromStash.addActionListener(this);
    popup.add(choosefromStash);
    // Remove!
    JMenuItem remove=new JMenuItem("Remove");
    remove.setActionCommand(REMOVE_COMMAND);
    remove.addActionListener(this);
    popup.add(remove);
    // Copy to stash
    JMenuItem copyToStash=new JMenuItem("Copy to stash");
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
        EQUIMENT_SLOT slot=findSlotForButton((Component)e.getSource());
        Item item=getItemForSlot(slot);
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
    Dimension d=new Dimension();
    int width=X_COLUMN_4+ICON_SIZE+ICON_FRAME_SIZE+X_MARGIN;
    int height=Y_ROW+ICON_SIZE+ICON_FRAME_SIZE+Y_MARGIN;
    d=new Dimension(width,height);
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

    MouseListener listener=buildRightClickListener();
    for(EQUIMENT_SLOT slot : EQUIMENT_SLOT.values())
    {
      // Position for item
      Dimension position=_iconPositions.get(slot);
      // Add object icon
      // - icon controller
      EquipmentSlotIconController iconController=new EquipmentSlotIconController(slot);
      _icons.put(slot,iconController);
      ImageIcon icon=iconController.getIcon();
      // - button
      JButton button=new JButton(icon);
      button.setBorderPainted(false);
      button.setMargin(new Insets(0,0,0,0));
      button.setBounds(position.width,position.height,ICON_SIZE,ICON_SIZE);
      _layeredPane.add(button,ICONS_DEPTH);
      _buttons.put(slot,button);
      button.setActionCommand(SLOT_SEED+slot.name());
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
    for(EQUIMENT_SLOT slot : EQUIMENT_SLOT.values())
    {
      Item item=getItemForSlot(slot);
      EquipmentSlotIconController iconController=_icons.get(slot);
      iconController.setItem(item);
      JButton button=_buttons.get(slot);
      ImageIcon icon=iconController.getIcon();
      button.setIcon(icon);
      String tooltipText=iconController.getTooltip();
      button.setToolTipText(tooltipText);
    }
  }

  private Item getItemForSlot(EQUIMENT_SLOT slot)
  {
    CharacterEquipment equipment=_toonData.getEquipment();
    Item item=equipment.getItemForSlot(slot);
    return item;
  }

  private void handleEditItem(EQUIMENT_SLOT slot)
  {
    Item item=getItemForSlot(slot);
    if (item!=null)
    {
      ItemEditionWindowController ctrl=new ItemEditionWindowController(_parentWindow,item);
      ctrl.show(true);
      refreshToon();
    }
  }

  private EQUIMENT_SLOT findSlotForButton(Component c)
  {
    for(Map.Entry<EQUIMENT_SLOT,JButton> entry : _buttons.entrySet())
    {
      if (c==entry.getValue())
      {
        return entry.getKey();
      }
    }
    return null;
  }

  private void handleChooseItem(EQUIMENT_SLOT slot)
  {
    ItemsManager itemsManager=ItemsManager.getInstance();
    handleChooseItem(slot,itemsManager);
  }

  private void handleChooseItem(EQUIMENT_SLOT slot, ItemsManager itemsManager)
  {
    Item item=chooseItem(slot,itemsManager);
    if (item!=null)
    {
      CharacterEquipment equipment=_toonData.getEquipment();
      SlotContents contents=equipment.getSlotContents(slot,true);
      Item clonedItem=ItemFactory.clone(item);
      contents.setItem(clonedItem);
      refreshToon();
    }
  }

  private Item chooseItem(EQUIMENT_SLOT slot, ItemsManager itemsManager)
  {
    List<Item> selectedItems=itemsManager.getItems(slot);
    ItemFilterConfiguration cfg=new ItemFilterConfiguration();
    cfg.initFromItems(selectedItems);
    TypedProperties filterProps=_parentWindow.getUserProperties("ItemFilter");
    ItemFilterController filterController=new ItemFilterController(cfg,_toonData,filterProps);
    Filter<Item> filter=filterController.getFilter();
    String id=ItemChoiceWindowController.ITEM_CHOOSER_PROPERTIES_ID+"#"+slot.name();
    TypedProperties props=_parentWindow.getUserProperties(id);
    ItemChoiceWindowController choiceCtrl=new ItemChoiceWindowController(_parentWindow,props,selectedItems,filter,filterController);
    Item ret=choiceCtrl.editModal();
    return ret;
  }

  private void handleRemoveItem(EQUIMENT_SLOT slot)
  {
    CharacterEquipment equipment=_toonData.getEquipment();
    SlotContents contents=equipment.getSlotContents(slot,false);
    if (contents!=null)
    {
      contents.setItem(null);
      contents.setItemId(null);
    }
    refreshToon();
  }

  private void handleCopyToStash(EQUIMENT_SLOT slot)
  {
    CharacterEquipment equipment=_toonData.getEquipment();
    SlotContents contents=equipment.getSlotContents(slot,false);
    if (contents!=null)
    {
      Item item=contents.getItem();
      if (item!=null)
      {
        ItemsStash stash=_toon.getStash();
        Item newItem=ItemFactory.clone(item);
        stash.addItem(newItem);
        Integer stashId=newItem.getStashIdentifier();
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
      EQUIMENT_SLOT slot=EQUIMENT_SLOT.valueOf(cmd);
      if (slot!=null)
      {
        Item currentItem=getItemForSlot(slot);
        if (currentItem!=null)
        {
          handleEditItem(slot);
        }
        else
        {
          handleChooseItem(slot);
        }
      }
    }
    else
    {
      // From contextual menu
      Component invoker=_contextMenu.getInvoker();
      EQUIMENT_SLOT slot=findSlotForButton(invoker);
      if (slot!=null)
      {
        if (EDIT_COMMAND.equals(cmd))
        {
          handleEditItem(slot);
        }
        else if (CHOOSE_COMMAND.equals(cmd))
        {
          handleChooseItem(slot);
        }
        else if (CHOOSE_FROM_STASH_COMMAND.equals(cmd))
        {
          ItemsStash stash=_toon.getStash();
          List<Item> items=stash.getAll();
          ItemsManager itemsManager=new ItemsManager(items);
          handleChooseItem(slot,itemsManager);
        }
        else if (REMOVE_COMMAND.equals(cmd))
        {
          handleRemoveItem(slot);
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
