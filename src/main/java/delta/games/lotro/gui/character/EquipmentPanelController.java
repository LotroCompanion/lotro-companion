package delta.games.lotro.gui.character;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterEquipment.SlotContents;
import delta.games.lotro.common.icons.LotroIconsManager;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.gui.utils.ImagePanel;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Controller for equipment panel.
 * @author DAM
 */
public class EquipmentPanelController implements ActionListener
{
  private static final int ICON_SIZE=32;
  private static final String BACKGROUND_IMAGE="/resources/gui/equipment/equipment_background.png";
  private static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";

  private JPanel _panel;
  private CharacterEquipment _equipment;
  private HashMap<EQUIMENT_SLOT,Dimension> _iconPositions;
  
  /**
   * Constructor.
   * @param equipment Equipment to display.
   */
  public EquipmentPanelController(CharacterEquipment equipment)
  {
    _equipment=equipment;
    initPositions();
  }

  private void initPositions()
  {
    _iconPositions=new HashMap<EQUIMENT_SLOT,Dimension>();
    int x=3;
    int y=3;
    _iconPositions.put(EQUIMENT_SLOT.LEFT_EAR,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.NECK,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.LEFT_WRIST,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.LEFT_FINGER,new Dimension(x,y));
    x=59; y=3;
    _iconPositions.put(EQUIMENT_SLOT.RIGHT_EAR,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.POCKET,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.RIGHT_WRIST,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.RIGHT_FINGER,new Dimension(x,y));
    x=297; y=3;
    _iconPositions.put(EQUIMENT_SLOT.HEAD,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.BREAST,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.HANDS,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.LEGS,new Dimension(x,y));
    x=358; y=3;
    _iconPositions.put(EQUIMENT_SLOT.SHOULDER,new Dimension(x,y));
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.BACK,new Dimension(x,y));
    y+=45;
    y+=45;
    _iconPositions.put(EQUIMENT_SLOT.FEET,new Dimension(x,y));

    x=95; y=217;
    _iconPositions.put(EQUIMENT_SLOT.MAIN_MELEE,new Dimension(x,y));
    x=139;
    _iconPositions.put(EQUIMENT_SLOT.OTHER_MELEE,new Dimension(x,y));
    x=183;
    _iconPositions.put(EQUIMENT_SLOT.RANGED,new Dimension(x,y));
    x=225;
    _iconPositions.put(EQUIMENT_SLOT.TOOL,new Dimension(x,y));
    x=269;
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
    }
    return _panel;
  }
  
  private JPanel buildPanel()
  {
    ImageIcon backgroundIcon=IconsManager.getIcon(BACKGROUND_IMAGE);
    JPanel panel=new ImagePanel(backgroundIcon.getImage());
    panel.setLayout(null);
    panel.setBackground(Color.BLACK);

    ItemsManager itemsManager=ItemsManager.getInstance();
    LotroIconsManager iconsManager=LotroIconsManager.getInstance();
    for(EQUIMENT_SLOT slot : EQUIMENT_SLOT.values())
    {
      SlotContents contents=_equipment.getSlotContents(slot,false);
      if (contents!=null)
      {
        String url=contents.getObjectURL();
        String id=itemsManager.idFromURL(url);
        if (id!=null)
        {
          Item item=itemsManager.getItem(id);
          if (item!=null)
          {
            String iconURL=item.getIconURL();
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
                Dimension position=_iconPositions.get(slot);
                button.setBounds(position.width,position.height,ICON_SIZE,ICON_SIZE);
                panel.add(button,null);
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
    
    return panel;
  }

  public void actionPerformed(ActionEvent e)
  {
    String cmd=e.getActionCommand();
    EQUIMENT_SLOT slot=EQUIMENT_SLOT.valueOf(cmd);
    if (slot!=null)
    {
      ItemsManager itemsManager=ItemsManager.getInstance();
      SlotContents contents=_equipment.getSlotContents(slot,false);
      String url=contents.getObjectURL();
      String id=itemsManager.idFromURL(url);
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
    _equipment=null;
    _iconPositions.clear();
  }
}
