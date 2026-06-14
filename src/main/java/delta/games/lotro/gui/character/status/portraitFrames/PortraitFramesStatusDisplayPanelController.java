package delta.games.lotro.gui.character.status.portraitFrames;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.portraitFrames.PortraitFramesStatus;
import delta.games.lotro.character.status.portraitFrames.filters.PortraitFramesStatusFilter;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.lore.portraitFrames.PortraitFrameDescription;
import delta.games.lotro.lore.portraitFrames.PortraitFramesManager;

/**
 * A panel to display the status of portrait frames.
 * @author DAM
 */
public class PortraitFramesStatusDisplayPanelController extends AbstractPanelController implements FilterUpdateListener
{
  // Data
  private List<PortraitFrameDescription> _portraitFrames;
  private PortraitFramesStatus _status;
  private PortraitFramesStatusFilter _filter;
  // Controllers
  private List<NavigationHyperLink> _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   * @param filter Filter to use.
   */
  public PortraitFramesStatusDisplayPanelController(WindowController parent, PortraitFramesStatus status, PortraitFramesStatusFilter filter)
  {
    super(parent);
    _status=status;
    _filter=filter;
    _portraitFrames=getPortraitFrames();
    Collections.sort(_portraitFrames,new NamedComparator());
    _links=new ArrayList<NavigationHyperLink>();
    setPanel(buildPanel());
    updatePanel();
  }

  private static List<PortraitFrameDescription> getPortraitFrames()
  {
    List<PortraitFrameDescription> ret=new ArrayList<PortraitFrameDescription>();
    for(PortraitFrameDescription portrait : PortraitFramesManager.getInstance().getAll())
    {
      if (portrait.isForFreeps())
      {
        ret.add(portrait);
      }
    }
    return ret;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    initGadgets();
    return ret;
  }

  private void initGadgets()
  {
    WindowController parent=getParentWindowController();
    for(PortraitFrameDescription portraitFrame : _portraitFrames)
    {
      PageIdentifier ref=ReferenceConstants.getPortraitFrameReference(portraitFrame.getCode());
      String text=portraitFrame.getName();
      NavigationHyperLink controller=new NavigationHyperLink(parent,text,ref);
      _links.add(controller);
      // Configure
      boolean known=_status.isUnlocked(portraitFrame);
      if (!known)
      {
        controller.setColor(Color.GRAY);
      }
    }
  }

  private void updatePanel()
  {
    JPanel panel=getPanel();
    panel.removeAll();
    int y=0;
    int nbPortraitFrames=_portraitFrames.size();
    for(int i=0;i<nbPortraitFrames;i++)
    {
      PortraitFrameDescription portraitFrame=_portraitFrames.get(i);
      if (!accept(portraitFrame))
      {
        continue;
      }
      NavigationHyperLink link=_links.get(i);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(link.getLabel(),c);
      y++;
    }
    GridBagConstraints c=new GridBagConstraints(2,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    c=new GridBagConstraints(0,y+1,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createVerticalGlue(),c);
    panel.revalidate();
    panel.repaint();
  }

  private boolean accept(PortraitFrameDescription portraitFrame)
  {
    Boolean unlockedState=_filter.getSelectedState();
    if (unlockedState!=null)
    {
      boolean state=_status.isUnlocked(portraitFrame);
      if (unlockedState.booleanValue()!=state)
      {
        return false;
      }
    }
    return _filter.accept(portraitFrame);
  }

  @Override
  public void filterUpdated()
  {
    updatePanel();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _portraitFrames=null;
    _status=null;
    _filter=null;
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
