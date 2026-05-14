package delta.games.lotro.gui.lore.portraitFrames.form;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.portraitFrames.PortraitFrameDescription;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.portraitFrames.PortraitFrameReferencesBuilder;
import delta.games.lotro.lore.xrefs.portraitFrames.PortraitFrameRole;

/**
 * Controller for a nationality display panel.
 * @author DAM
 */
public class PortraitFrameDisplayPanelController extends AbstractPanelController implements NavigablePanelController
{
  // Data
  private PortraitFrameDescription _portraitFrame;
  // Controllers
  private List<NavigationHyperLink> _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param portraitFrame Portrait frame to show.
   */
  public PortraitFrameDisplayPanelController(NavigatorWindowController parent, PortraitFrameDescription portraitFrame)
  {
    super(parent);
    _portraitFrame=portraitFrame;
    setPanel(build());
  }

  @Override
  public String getTitle()
  {
    return "Portrait frame: "+_portraitFrame.getName(); // I18n
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Name line
    {
      String name=_portraitFrame.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(28f).deriveFont(Font.BOLD));
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,0,5),0,0);
      panel.add(nameLabel,c);
      y++;
    }
    // Icon
    {
      ImageIcon icon=LotroIconsManager.getPortraitFrameIcon(_portraitFrame.getIconName());
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,0,5),0,0);
      JLabel iconLabel=GuiFactory.buildIconLabel(icon);
      panel.add(iconLabel,c);
      y++;
    }
    // Attributes
    {
      String attrLine=buildAttributesLine(_portraitFrame);
      JLabel attrLabel=GuiFactory.buildLabel(attrLine);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(attrLabel,c);
      y++;
    }
    // Item references
    JPanel references=buildReferencesPanel();
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    references.setBorder(GuiFactory.buildTitledBorder("Providers"));
    panel.add(references,c);
    y++;
    return panel;
  }

  private JPanel buildReferencesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    _links=new ArrayList<NavigationHyperLink>();
    List<Reference<Item,PortraitFrameRole>> refs=new PortraitFrameReferencesBuilder().inspectPortraitFrame(_portraitFrame.getCode());
    for(Reference<Item,PortraitFrameRole> ref : refs)
    {
      Item item=ref.getSource();
      NavigationHyperLink link=ItemUiTools.buildItemLink(getParentWindowController(),item);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(link.getLabel(),c);
      _links.add(link);
      y++;
    }
    return panel;
  }

  private String buildAttributesLine(PortraitFrameDescription portraitFrame)
  {
    StringBuilder sb=new StringBuilder();
    boolean freeps=portraitFrame.isForFreeps();
    if (freeps)
    {
      sb.append("For free peoples");
    }
    boolean creeps=portraitFrame.isForCreeps();
    if (creeps)
    {
      if (sb.length()>0)
      {
        sb.append(" / ");
      }
      sb.append("For creeps");
    }
    boolean isPVP=portraitFrame.isForPvpCharacters();
    if (isPVP)
    {
      if (sb.length()>0)
      {
        sb.append(" / ");
      }
      sb.append("For PVP characters");
    }
    return sb.toString();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _portraitFrame=null;
    // Controllers
    if (_links!=null)
    {
      for(NavigationHyperLink link : _links)
      {
        link.dispose();
      }
      _links=null;
    }
  }
}
