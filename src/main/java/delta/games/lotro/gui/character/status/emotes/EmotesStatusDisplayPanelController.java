package delta.games.lotro.gui.character.status.emotes;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.TransparentIcon;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.emotes.EmoteStatus;
import delta.games.lotro.character.status.emotes.EmotesStatusManager;
import delta.games.lotro.character.status.emotes.filters.EmoteStatusFilter;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;
import delta.games.lotro.lore.emotes.EmoteDescription;

/**
 * A panel to display the status of a collection of emotes.
 * @author DAM
 */
public class EmotesStatusDisplayPanelController implements FilterUpdateListener
{
  // Data
  private List<EmoteDescription> _emotes;
  private EmotesStatusManager _status;
  private EmoteStatusFilter _filter;
  // Controllers
  private WindowController _parent;
  private List<IconLinkLabelGadgetsController> _gadgets;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param emotes Emotes to show.
   * @param status Status to show.
   * @param filter Filter to use.
   */
  public EmotesStatusDisplayPanelController(WindowController parent, List<EmoteDescription> emotes, EmotesStatusManager status, EmoteStatusFilter filter)
  {
    _parent=parent;
    _emotes=new ArrayList<EmoteDescription>(emotes);
    Collections.sort(_emotes,new NamedComparator());
    _status=status;
    _filter=filter;
    _gadgets=new ArrayList<IconLinkLabelGadgetsController>();
    _panel=buildPanel();
    updatePanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    initGadgets();
    return ret;
  }

  private void initGadgets()
  {
    for(EmoteDescription emote : _emotes)
    {
      IconLinkLabelGadgetsController ctrl=GadgetsControllersFactory.build(_parent,emote);
      _gadgets.add(ctrl);
      // Configure
      EmoteStatus emoteStatus=_status.get(emote,true);
      boolean known=isKnown(emoteStatus);
      if (!known)
      {
        Icon icon=ctrl.getIcon().getIcon().getIcon();
        Icon otherIcon=new TransparentIcon(icon,0.5f);
        ctrl.getIcon().setIcon(otherIcon);
        ctrl.getLink().setColor(Color.GRAY);
      }
    }
  }

  private boolean isKnown(EmoteStatus status)
  {
    boolean available=status.isAvailable();
    if (available)
    {
      return true;
    }
    EmoteDescription emote=status.getEmote();
    return emote.isAuto();
  }

  private void updatePanel()
  {
    _panel.removeAll();
    int y=0;
    int nbEmotes=_emotes.size();
    for(int i=0;i<nbEmotes;i++)
    {
      EmoteDescription emote=_emotes.get(i);
      EmoteStatus emoteStatus=_status.get(emote,true);
      if (!_filter.accept(emoteStatus))
      {
        continue;
      }
      IconLinkLabelGadgetsController ctrl=_gadgets.get(i);
      IconController icon=ctrl.getIcon();
      HyperLinkController link=ctrl.getLink();
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(icon.getIcon(),c);
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(link.getLabel(),c);
      y++;
    }
    GridBagConstraints c=new GridBagConstraints(2,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    _panel.add(Box.createGlue(),c);
    c=new GridBagConstraints(0,y+1,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    _panel.add(Box.createVerticalGlue(),c);
    _panel.revalidate();
    _panel.repaint();
  }

  @Override
  public void filterUpdated()
  {
    updatePanel();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _emotes=null;
    _status=null;
    _filter=null;
    // Controllers
    _parent=null;
    if (_gadgets!=null)
    {
      for(IconLinkLabelGadgetsController gadgets : _gadgets)
      {
        gadgets.dispose();
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
