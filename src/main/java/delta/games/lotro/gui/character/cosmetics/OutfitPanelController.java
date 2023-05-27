package delta.games.lotro.gui.character.cosmetics;

import java.awt.BorderLayout;
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
import delta.games.lotro.character.cosmetics.Outfit;
import delta.games.lotro.character.cosmetics.OutfitElement;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.character.gear.GearSlots;
import delta.games.lotro.gui.character.gear.EquipmentSlotIconController;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a panel to display an outfit.
 * @author DAM
 */
public class OutfitPanelController implements ActionListener
{
  private static final int ICON_SIZE=32;
  private static final Integer ICONS_DEPTH=Integer.valueOf(1);

  private static final int ICON_FRAME_SIZE=3;
  private static final int DELTA_X=38;
  private static final int DELTA_Y=45;
  private static final int DELTA_COLUMNS=52;
  private static final int X_MARGIN=15;
  private static final int Y_MARGIN=20;
  private static final int Y_START=Y_MARGIN+ICON_FRAME_SIZE;
  private static final int X_COLUMN_1=X_MARGIN+ICON_FRAME_SIZE;
  private static final int X_COLUMN_2=X_COLUMN_1+DELTA_COLUMNS+30;
  private static final int COLUMNS_WIDTH=X_COLUMN_2+ICON_SIZE+ICON_FRAME_SIZE-(X_COLUMN_1-ICON_FRAME_SIZE);
  private static final int ROW_WIDTH=ICON_FRAME_SIZE+2*DELTA_X+ICON_SIZE+ICON_FRAME_SIZE;
  private static final int X_ROW=X_MARGIN+(COLUMNS_WIDTH-ROW_WIDTH)/2;
  private static final int Y_MARGIN_COLUMNS_ROW=25;
  private static final int Y_ROW1=Y_START+DELTA_Y*3+ICON_SIZE+ICON_FRAME_SIZE+Y_MARGIN_COLUMNS_ROW;
  private static final int Y_ROW2=Y_ROW1+DELTA_Y;

  private static final String SLOT_SEED="slot_";

  // Controllers
  private WindowController _parentWindow;
  private Map<GearSlot,EquipmentSlotIconController> _icons;
  // UI
  private JPanel _panel;
  private JLayeredPane _layeredPane;
  private Map<GearSlot,Dimension> _iconPositions;
  private Map<GearSlot,JButton> _buttons;
  // Data
  private Outfit _outfit;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param outfit Outfit.
   */
  public OutfitPanelController(WindowController parent, Outfit outfit)
  {
    _parentWindow=parent;
    _outfit=outfit;
    _buttons=new HashMap<GearSlot,JButton>();
    _icons=new HashMap<GearSlot,EquipmentSlotIconController>();
    initPositions();
  }

  private void initPositions()
  {
    _iconPositions=new HashMap<GearSlot,Dimension>();
    int x=X_COLUMN_1;
    int y=Y_START;
    _iconPositions.put(GearSlots.HEAD,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.SHOULDER,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.BACK,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.BREAST,new Dimension(x,y));
    x=X_COLUMN_2; y=Y_START;
    _iconPositions.put(GearSlots.HANDS,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.LEGS,new Dimension(x,y));
    y+=DELTA_Y;
    _iconPositions.put(GearSlots.FEET,new Dimension(x,y));

    x=X_ROW; y=Y_ROW1;
    _iconPositions.put(GearSlots.MAIN_HAND_AURA,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.OFF_HAND_AURA,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.RANGED_AURA,new Dimension(x,y));
    x=X_ROW; y=Y_ROW2;
    _iconPositions.put(GearSlots.MAIN_MELEE,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.OTHER_MELEE,new Dimension(x,y));
    x+=DELTA_X;
    _iconPositions.put(GearSlots.RANGED,new Dimension(x,y));
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
    int width=X_COLUMN_2+ICON_SIZE+ICON_FRAME_SIZE+X_MARGIN;
    int height=Y_ROW2+ICON_SIZE+ICON_FRAME_SIZE+Y_MARGIN;
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
      button.addActionListener(this);
      button.setToolTipText("");
    }
    updateIcons();

    return panel;
  }

  private boolean showVisibilityIcon(GearSlot slot)
  {
    if (slot==GearSlots.BREAST) return false;
    if (slot==GearSlots.LEGS) return false;
    if (slot==GearSlots.MAIN_HAND_AURA) return false;
    if (slot==GearSlots.OFF_HAND_AURA) return false;
    if (slot==GearSlots.RANGED_AURA) return false;
    return true;
  }

  /**
   * Update contents.
   */
  private void updateIcons()
  {
    for(GearSlot slot : GearSlot.getAll())
    {
      EquipmentSlotIconController iconController=_icons.get(slot);
      if (iconController==null)
      {
        continue;
      }
      Item item=null;
      OutfitElement element=_outfit.getSlot(slot);
      if (element!=null)
      {
        item=element.getItem();
      }
      Boolean visibilityIcon=null;
      boolean showVisibilityIcon=showVisibilityIcon(slot);
      if (showVisibilityIcon)
      {
        visibilityIcon=Boolean.valueOf(_outfit.isSlotVisible(slot));
      }
      iconController.setItem(item,visibilityIcon);
      JButton button=_buttons.get(slot);
      Icon icon=iconController.getIcon();
      button.setIcon(icon);
      String tooltipText=iconController.getTooltip();
      button.setToolTipText(tooltipText);
    }
  }

  private Item getItemForSlot(GearSlot slot)
  {
    OutfitElement element=_outfit.getSlot(slot);
    if (element!=null)
    {
      return element.getItem();
    }
    return null;
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
        Item currentItem=getItemForSlot(slot);
        if (currentItem!=null)
        {
          ItemUiTools.showItemForm(_parentWindow,currentItem);
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
    _iconPositions.clear();
    _buttons.clear();
    _icons.clear();
    _outfit=null;
  }
}
