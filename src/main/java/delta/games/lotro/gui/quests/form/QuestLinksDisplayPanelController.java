package delta.games.lotro.gui.quests.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.utils.Proxy;

/**
 * Controller for a panel to display quest links.
 * @author DAM
 */
public class QuestLinksDisplayPanelController
{
  private static final Logger LOGGER=Logger.getLogger(QuestLinksDisplayPanelController.class);

  // Data
  private QuestDescription _quest;
  private List<String> _labels;
  // GUI
  private JPanel _panel;
  // Controllers
  private QuestDisplayWindowController _parent;
  private List<HyperLinkController> _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param quest Quest to show.
   */
  public QuestLinksDisplayPanelController(QuestDisplayWindowController parent, QuestDescription quest)
  {
    _parent=parent;
    _quest=quest;
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
    List<Proxy<Achievable>> prerequisites=_quest.getPrerequisites();
    for(Proxy<Achievable> prerequisite : prerequisites)
    {
      buildController("Prerequisite:",prerequisite);
    }
    buildController("Next:",_quest.getNextQuest());
  }

  private void buildController(String label, Proxy<Achievable> proxy)
  {
    if (proxy!=null)
    {
      final Achievable achievable=proxy.getObject();
      if (achievable instanceof QuestDescription)
      {
        final QuestDescription quest=(QuestDescription)achievable;
        String name=quest.getName();
        ActionListener listener=new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            _parent.setQuest(quest);
          }
        };
        LocalHyperlinkAction action=new LocalHyperlinkAction(name,listener);
        HyperLinkController controller=new HyperLinkController(action);
        _labels.add(label);
        _links.add(controller);
      }
      else
      {
        LOGGER.warn("Achievable not managed: "+achievable);
      }
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _labels.clear();
    _quest=null;
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
