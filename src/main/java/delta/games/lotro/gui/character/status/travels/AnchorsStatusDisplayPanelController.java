package delta.games.lotro.gui.character.status.travels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsManager;
import delta.games.lotro.character.skills.TravelSkill;
import delta.games.lotro.character.status.travels.AnchorStatus;
import delta.games.lotro.character.status.travels.AnchorsStatusManager;
import delta.games.lotro.common.enums.TravelLink;
import delta.games.lotro.common.geo.ExtendedPosition;
import delta.games.lotro.common.geo.Position;
import delta.games.lotro.common.geo.PositionUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;
import delta.games.lotro.lore.maps.Area;
import delta.games.lotro.lore.maps.Dungeon;
import delta.games.lotro.lore.maps.Zone;

/**
 * Display anchors for a single character.
 * @author DAM
 */
public class AnchorsStatusDisplayPanelController
{
  // Controllers
  private List<IconLinkLabelGadgetsController> _gadgets;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Paren window.
   * @param statusMgr Status manager.
   */
  public AnchorsStatusDisplayPanelController(WindowController parent, AnchorsStatusManager statusMgr)
  {
    _gadgets=new ArrayList<IconLinkLabelGadgetsController>();
    init(parent,statusMgr);
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void init(WindowController parent, AnchorsStatusManager statusMgr)
  {
    List<TravelSkill> travelSkills=getTravelSkills();
    for(TravelSkill travelSkill : travelSkills)
    {
      TravelLink type=travelSkill.getType();
      AnchorStatus status=statusMgr.get(type,false);
      if (status!=null)
      {
        IconLinkLabelGadgetsController skillGadgets=buildGadgets(parent,travelSkill,status);
        _gadgets.add(skillGadgets);
      }
    }
  }

  private IconLinkLabelGadgetsController buildGadgets(WindowController parent, SkillDescription travelSkill, AnchorStatus status)
  {
    IconLinkLabelGadgetsController skillGadgets=GadgetsControllersFactory.build(parent,travelSkill);
    String newLabel=null;
    // Use name
    String name=status.getName();
    if (name!=null)
    {
      newLabel="Return to "+name; // I18n
    }
    // Use position
    Icon newIcon=null;
    ExtendedPosition extendedPosition=status.getPosition();
    if (extendedPosition!=null)
    {
      Position position=extendedPosition.getPosition();
      Zone zone=extendedPosition.getZone();
      if (zone!=null)
      {
        newIcon=getIcon(zone);
        if (newLabel==null)
        {
          newLabel=travelSkill.getName()+" ("+zone.getName()+")";
        }
      }
      String positionStr=PositionUtils.getLabel(position);
      JLabel positionLabel=skillGadgets.getComplement();
      positionLabel.setText(positionStr);
    }
    // Set new label
    if (newLabel!=null)
    {
      HyperLinkController link=skillGadgets.getLink();
      link.setText(newLabel);
    }
    // Set new icon
    if ((newIcon!=null) && (name!=null))
    {
      skillGadgets.getIcon().setIcon(newIcon);
    }
    return skillGadgets;
  }

  private ImageIcon getIcon(Zone zone)
  {
    if (zone instanceof Area)
    {
      Area area=(Area)zone;
      Integer iconID=area.getIconId();
      if (iconID!=null)
      {
        return LotroIconsManager.getAreaIcon(iconID.intValue());
      }
    }
    else if (zone instanceof Dungeon)
    {
      Dungeon dungeon=(Dungeon)zone;
      ExtendedPosition position=dungeon.getMapPosition();
      if (position!=null)
      {
        Zone parentZone=position.getZone();
        return getIcon(parentZone);
      }
    }
    return null;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int y=0;
    for(IconLinkLabelGadgetsController gadgets : _gadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getIcon().getIcon(),c);
      c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getLink().getLabel(),c);
      y++;
      c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getComplement(),c);
      y++;
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    ret.add(Box.createVerticalGlue(),c);
    return ret;
  }

  private List<TravelSkill> getTravelSkills()
  {
    List<TravelSkill> ret=new ArrayList<TravelSkill>();
    SkillsManager skillsManager=SkillsManager.getInstance();
    for(SkillDescription skill : skillsManager.getAll())
    {
      if (skill instanceof TravelSkill)
      {
        ret.add((TravelSkill)skill);
      }
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    if (_gadgets!=null)
    {
      for(IconLinkLabelGadgetsController gadget : _gadgets)
      {
        gadget.dispose();
      }
      _gadgets=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
