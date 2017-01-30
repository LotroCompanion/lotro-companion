package delta.games.lotro.gui.character;

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

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.gui.items.ItemChoiceWindowController;
import delta.games.lotro.gui.items.ItemEditionWindowController;
import delta.games.lotro.gui.items.ItemFilterController;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemFactory;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for equipment panel.
 * @author DAM
 */
public class EquipmentPanelController implements ActionListener
{
  private static final int ICON_SIZE=32;
  private static final String BACKGROUND_ICONS_SEED="/resources/gui/equipment/";
  private static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";
  private static final Integer BACKGROUND_ICONS_DEPTH=Integer.valueOf(0);
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
  private static final String REMOVE_COMMAND="remove";

  private WindowController _parentWindow;
  private JPanel _panel;
  private JLayeredPane _layeredPane;
  private HashMap<EQUIMENT_SLOT,Dimension> _iconPositions;
  private CharacterData _toon;
  private HashMap<EQUIMENT_SLOT,JButton> _buttons;
  private JPopupMenu _contextMenu;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param toon Toon to display.
   */
  public EquipmentPanelController(WindowController parent, CharacterData toon)
  {
    _parentWindow=parent;
    _toon=toon;
    _buttons=new HashMap<EQUIMENT_SLOT,JButton>();
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
    JMenuItem edit=new JMenuItem("Edit...");
    edit.setActionCommand(EDIT_COMMAND);
    edit.addActionListener(this);
    popup.add(edit);
    JMenuItem choose=new JMenuItem("Choose...");
    choose.setActionCommand(CHOOSE_COMMAND);
    choose.addActionListener(this);
    popup.add(choose);
    JMenuItem remove=new JMenuItem("Remove");
    remove.setActionCommand(REMOVE_COMMAND);
    remove.addActionListener(this);
    popup.add(remove);
    return popup;
  }

  private MouseListener buildRightClickListener()
  {
    class PopClickListener extends MouseAdapter
    {
      public void mousePressed(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }

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
      //magouille();
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

    ImageIcon icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);

    MouseListener listener=buildRightClickListener();
    for(EQUIMENT_SLOT slot : EQUIMENT_SLOT.values())
    {
      // Position for item
      Dimension position=_iconPositions.get(slot);
      // Add background icon
      String iconPath=BACKGROUND_ICONS_SEED+slot.name()+".png";
      ImageIcon backgroundIcon=IconsManager.getIcon(iconPath);
      JButton backgroundIconButton=new JButton(backgroundIcon);
      backgroundIconButton.setBorderPainted(false);
      backgroundIconButton.setMargin(new Insets(0,0,0,0));
      backgroundIconButton.setBounds(position.width-ICON_FRAME_SIZE,position.height-ICON_FRAME_SIZE,ICON_SIZE+6,ICON_SIZE+6);
      _layeredPane.add(backgroundIconButton,BACKGROUND_ICONS_DEPTH);
      // Add object icon
      JButton button=new JButton(icon);
      button.setBorderPainted(false);
      button.setMargin(new Insets(0,0,0,0));
      button.setBounds(position.width,position.height,ICON_SIZE,ICON_SIZE);
      _layeredPane.add(button,ICONS_DEPTH);
      _buttons.put(slot,button);
      button.setActionCommand(slot.name());
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
      ImageIcon icon=null;
      String tooltipText=null;
      Item item=getItemForSlot(slot);
      if (item!=null)
      {
        String iconId=item.getProperty(ItemPropertyNames.ICON_ID);
        String backgroundIconId=item.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
        icon=IconsManager.getItemIcon(iconId,backgroundIconId);
        String dump=item.dump();
        tooltipText="<html>"+dump.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
      }
      if (icon==null)
      {
        icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
      }
      JButton button=_buttons.get(slot);
      button.setIcon(icon);
      button.setToolTipText(tooltipText);
    }
  }

  private Item getItemForSlot(EQUIMENT_SLOT slot)
  {
    CharacterEquipment equipment=_toon.getEquipment();
    SlotContents contents=equipment.getSlotContents(slot,false);
    Item item=null;
    if (contents!=null)
    {
      item=contents.getItem();
      if (item==null)
      {
        Integer id=contents.getItemId();
        if (id!=null)
        {
          ItemsManager itemsManager=ItemsManager.getInstance();
          item=itemsManager.getItem(id);
          if (item!=null)
          {
            item=ItemFactory.clone(item);
          }
          contents.setItem(item);
        }
      }
    }
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
    Item item=getItemForSlot(slot);
    item=chooseItem(slot,item);
    if (item!=null)
    {
      CharacterEquipment equipment=_toon.getEquipment();
      SlotContents contents=equipment.getSlotContents(slot,true);
      Item clonedItem=ItemFactory.clone(item);
      contents.setItem(clonedItem);
      refreshToon();
    }
  }

  private Item chooseItem(EQUIMENT_SLOT slot, Item currentItem)
  {
    // TODO use unique instance (do not build one each time)
    ItemSelection selection=new ItemSelection();
    List<Item> items=selection.getItems(_toon,slot);
    ItemNameFilter filter=new ItemNameFilter();
    ItemFilterController filterController=new ItemFilterController(filter);
    ItemChoiceWindowController choiceCtrl=new ItemChoiceWindowController(_parentWindow,items,filter,filterController);
    choiceCtrl.show(true);
    Item ret=choiceCtrl.getSelectedItem();
    choiceCtrl.dispose();
    return ret;
  }

  private void handleRemoveItem(EQUIMENT_SLOT slot)
  {
    CharacterEquipment equipment=_toon.getEquipment();
    SlotContents contents=equipment.getSlotContents(slot,false);
    if (contents!=null)
    {
      contents.setItem(null);
      contents.setItemId(null);
    }
    refreshToon();
  }

  public void actionPerformed(ActionEvent e)
  {
    String cmd=e.getActionCommand();
    if ((EDIT_COMMAND.equals(cmd)) || (CHOOSE_COMMAND.equals(cmd)) || (REMOVE_COMMAND.equals(cmd)))
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
        else if (REMOVE_COMMAND.equals(cmd))
        {
          handleRemoveItem(slot);
        }
      }
    }
    else
    {
      // Straight click
      EQUIMENT_SLOT slot=EQUIMENT_SLOT.valueOf(cmd);
      if (slot!=null)
      {
        Item item=getItemForSlot(slot);
        if (item!=null)
        {
          handleEditItem(slot);
        }
        else
        {
          handleChooseItem(slot);
        }
      }
    }
  }

  private void refreshToon()
  {
    updateIcons();
    // Broadcast equipment update event...
    CharacterEvent event=new CharacterEvent(null,_toon);
    CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
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
    _iconPositions.clear();
    _buttons.clear();
  }
}
