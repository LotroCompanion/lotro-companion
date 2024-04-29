package delta.games.lotro.gui.character.gear;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.gear.CharacterGear;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.character.gear.GearSlots;
import delta.games.lotro.gui.lore.items.ItemInstanceDisplayWindowController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Controller for equipment panel.
 * @author DAM
 */
public class EquipmentDisplayPanelController implements ActionListener
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

  /**
   * Slot seed.
   */
  public static final String SLOT_SEED="slot_";

  private WindowController _parentWindow;
  private JPanel _panel;
  private JLayeredPane _layeredPane;
  private HashMap<GearSlot,Dimension> _iconPositions;
  private CharacterGear _gear;
  private HashMap<GearSlot,JButton> _buttons;
  private HashMap<GearSlot,EquipmentSlotIconController> _icons;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param gear Gear to display.
   */
  public EquipmentDisplayPanelController(WindowController parent, CharacterGear gear)
  {
    _parentWindow=parent;
    _gear=gear;
    _buttons=new HashMap<GearSlot,JButton>();
    _icons=new HashMap<GearSlot,EquipmentSlotIconController>();
    initPositions();
    _panel=buildPanel();
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

    // Bridle
    x=X_COLUMN_1+DELTA_COLUMNS+24+(DELTA_COLUMN_GROUPS)/2;
    y=Y_START+3*DELTA_Y;
    _iconPositions.put(GearSlots.BRIDLE,new Dimension(x,y));
  }

  /**
   * Set the button in the middle of the panel.
   * @param updateButton Button to set.
   */
  public void setButton(JButton updateButton)
  {
    updateButton.setLocation(X_BUTTON,Y_BUTTON);
    updateButton.setSize(updateButton.getPreferredSize());
    _layeredPane.add(updateButton,ICONS_DEPTH);
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
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

    for(GearSlot slot : GearSlot.getAll())
    {
      // Position for item
      Dimension position=_iconPositions.get(slot);
      if (position==null)
      {
        continue;
      }
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
      button.setToolTipText("");
    }
    updateIcons();

    return panel;
  }

  /**
   * Init listeners for item display.
   */
  public void initButtonListeners()
  {
    for(GearSlot slot : GearSlot.getAll())
    {
      JButton button=getButtonForSlot(slot);
      button.addActionListener(this);
    }
  }

  /**
   * Get the button for a given slot.
   * @param slot Slot to use.
   * @return A button or <code>null</code> if not found.
   */
  public JButton getButtonForSlot(GearSlot slot)
  {
    return _buttons.get(slot);
  }

  /**
   * Find the slot for the given button.
   * @param c Source component.
   * @return A slot or <code>null</code> if not found.
   */
  public GearSlot findSlotForButton(Component c)
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

  /**
   * Update contents.
   */
  public void updateIcons()
  {
    for(GearSlot slot : GearSlot.getAll())
    {
      ItemInstance<? extends Item> itemInstance=getItemForSlot(slot);
      EquipmentSlotIconController iconController=_icons.get(slot);
      if (iconController==null)
      {
        continue;
      }
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
    ItemInstance<? extends Item> itemInstance=_gear.getItemForSlot(slot);
    return itemInstance;
  }

  private void handleShowItemInstance(GearSlot slot)
  {
    ItemInstance<? extends Item> item=getItemForSlot(slot);
    if (item!=null)
    {
      ItemInstanceDisplayWindowController ctrl=new ItemInstanceDisplayWindowController(_parentWindow,item);
      ctrl.show();
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
          handleShowItemInstance(slot);
        }
      }
    }
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
    _gear=null;
    _iconPositions.clear();
    _buttons.clear();
    _icons.clear();
  }
}
