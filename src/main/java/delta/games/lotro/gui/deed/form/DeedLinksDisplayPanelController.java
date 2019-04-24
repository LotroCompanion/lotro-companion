package delta.games.lotro.gui.deed.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedProxy;

/**
 * Controller for a panel to display deed links.
 * @author DAM
 */
public class DeedLinksDisplayPanelController
{
  // Data
  private DeedDescription _deed;
  private List<String> _labels;
  // GUI
  private JPanel _panel;
  // Controllers
  private DeedDisplayWindowController _parent;
  private List<HyperLinkController> _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param deed Deed to edit.
   */
  public DeedLinksDisplayPanelController(DeedDisplayWindowController parent, DeedDescription deed)
  {
    _parent=parent;
    _deed=deed;
    _labels=new ArrayList<String>();
    _links=new ArrayList<HyperLinkController>();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    buildLinks();
    return buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int nbLinks=_links.size();
    int y=0;
    for(int i=0;i<nbLinks;i++)
    {
      int top=(i>0)?5:0;
      int bottom=(i<nbLinks-1)?0:5;
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(top,5,bottom,5),0,0);
      panel.add(GuiFactory.buildLabel(_labels.get(i)),c);
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(top,0,bottom,0),0,0);
      panel.add(_links.get(i).getLabel(),c);
      y++;
    }
    return panel;
  }

  private void buildLinks()
  {
    List<DeedProxy> parents=_deed.getParentDeedProxies().getDeedProxies();
    for(DeedProxy parent : parents)
    {
      buildController("Parent:",parent);
    }
    buildController("Previous:",_deed.getPreviousDeedProxy());
    buildController("Next:",_deed.getNextDeedProxy());
    List<DeedProxy> children=_deed.getChildDeedProxies().getDeedProxies();
    for(DeedProxy child : children)
    {
      buildController("Child:",child);
    }
  }

  private void buildController(String label, DeedProxy proxy)
  {
    if (proxy!=null)
    {
      String name=proxy.getName();
      final DeedDescription deed=proxy.getObject();
      ActionListener listener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          _parent.setDeed(deed);
        }
      };
      LocalHyperlinkAction action=new LocalHyperlinkAction(name,listener);
      HyperLinkController controller=new HyperLinkController(action);
      _labels.add(label);
      _links.add(controller);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _labels.clear();
    _deed=null;
    // Controllers
    _parent=null;
    for(HyperLinkController link : _links)
    {
      link.dispose();
    }
    _links.clear();
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
