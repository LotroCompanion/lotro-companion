package delta.games.lotro.gui.character;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.common.icons.LotroIconsManager;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

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

  private JPanel _panel;
  private JLayeredPane _layeredPane;
  private HashMap<EQUIMENT_SLOT,Dimension> _iconPositions;
  private CharacterFile _toon;
  private HashMap<EQUIMENT_SLOT,JButton> _buttons;
  
  /**
   * Constructor.
   * @param toon Toon to display.
   */
  public EquipmentPanelController(CharacterFile toon)
  {
    _toon=toon;
    _buttons=new HashMap<EQUIMENT_SLOT,JButton>();
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
    }
    update();
    
    return panel;
  }

  /**
   * Update contents.
   */
  public void update()
  {
    if (_layeredPane!=null)
    {
      Collection<JButton> buttons=_buttons.values();
      for(JButton button : buttons)
      {
        _layeredPane.remove(button);
        button.removeActionListener(this);
      }
      _buttons.clear();
    }
    Character c=_toon.getLastCharacterInfo();
    CharacterEquipment equipment=c.getEquipment();
    ItemsManager itemsManager=ItemsManager.getInstance();
    LotroIconsManager iconsManager=LotroIconsManager.getInstance();

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
      
      SlotContents contents=equipment.getSlotContents(slot,false);
      if (contents!=null)
      {
        String url=contents.getObjectURL();
        Integer id=itemsManager.idFromURL(url);
        if (id!=null)
        {
          String iconURL=null;
          Item item=itemsManager.getItem(id);
          if (item!=null)
          {
            iconURL=item.getIconURL();
          }
          else
          {
            iconURL=contents.getIconURL();
            item=new Item();
            item.setIdentifier(id.intValue());
            item.setIconURL(iconURL);
            itemsManager.writeItemFile(item);
          }
          if (iconURL!=null)
          {
            File f=iconsManager.getIconFile(iconURL);
            ImageIcon icon=null;
            if (f.length()>0)
            {
              icon=IconsManager.getIcon(f);
            }
            if (icon==null)
            {
              icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
            }
            if (icon!=null)
            {
              JButton button=new JButton(icon);
              button.setBorderPainted(false);
              button.setMargin(new Insets(0,0,0,0));
              button.setBounds(position.width,position.height,ICON_SIZE,ICON_SIZE);
              _layeredPane.add(button,ICONS_DEPTH);
              _buttons.put(slot,button);
              button.setActionCommand(slot.name());
              button.addActionListener(this);

              String dump=item.dump();
              button.setToolTipText(dump);
            }
          }
        }
      }
    }
  }

  public void actionPerformed(ActionEvent e)
  {
    String cmd=e.getActionCommand();
    EQUIMENT_SLOT slot=EQUIMENT_SLOT.valueOf(cmd);
    if (slot!=null)
    {
      ItemsManager itemsManager=ItemsManager.getInstance();
      Character c=_toon.getLastCharacterInfo();
      CharacterEquipment equipment=c.getEquipment();
      SlotContents contents=equipment.getSlotContents(slot,false);
      String url=contents.getObjectURL();
      Integer id=itemsManager.idFromURL(url);
      if (id!=null)
      {
        Item item=itemsManager.getItem(id);
        if (item!=null)
        {
          item.dump();
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
    _toon=null;
    _iconPositions.clear();
    _buttons.clear();
  }
}
