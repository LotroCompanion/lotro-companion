package delta.games.lotro.gui.character.status.collections.summary;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.status.collections.CollectionStatus;
import delta.games.lotro.character.status.collections.CollectionsStatusManager;
import delta.games.lotro.gui.character.status.collections.CollectionStatusWindowController;

/**
 * Controller for a panel that display the status of a series of collections.
 * @author DAM
 */
public class CollectionsStatusSummaryPanelController
{
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<SingleCollectionStatusGadgetsController> _gadgets;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statusMgr Status to show.
   */
  public CollectionsStatusSummaryPanelController(WindowController parent, CollectionsStatusManager statusMgr)
  {
    _parent=parent;
    _gadgets=new ArrayList<SingleCollectionStatusGadgetsController>();
    _panel=buildPanel(statusMgr);
  }

  private JPanel buildPanel(CollectionsStatusManager statusMgr)
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int y=0;
    List<CollectionStatus> collectionStatuses=statusMgr.getAll();
    for(CollectionStatus collectionStatus : collectionStatuses)
    {
      SingleCollectionStatusGadgetsController gadgets=new SingleCollectionStatusGadgetsController(collectionStatus);
      _gadgets.add(gadgets);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
      ret.add(gadgets.getNameGadget(),c);
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      ret.add(gadgets.getProgressGadget(),c);
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      final CollectionStatus cs=collectionStatus;
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          showCollectionStatusDetails(cs);
        }
      };
      JButton button=gadgets.getDetailsButton();
      button.addActionListener(al);
      ret.add(button,c);
      y++;
    }
    // Push everything towards the top
    JPanel bottom=GuiFactory.buildPanel(new BorderLayout());
    GridBagConstraints c=new GridBagConstraints(0,y,3,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(bottom,c);
    return ret;
  }

  private void showCollectionStatusDetails(CollectionStatus collectionStatus)
  {
    WindowsManager mgr=_parent.getWindowsManager();
    String identifier=CollectionStatusWindowController.getIdentifier(collectionStatus);
    WindowController child=mgr.getWindow(identifier);
    if (child==null)
    {
      child=new CollectionStatusWindowController(_parent,collectionStatus);
      mgr.registerWindow(child);
      child.show();
    }
    else
    {
      child.bringToFront();
    }
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
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
    _parent=null;
    if (_gadgets!=null)
    {
      for(SingleCollectionStatusGadgetsController gadget : _gadgets)
      {
        gadget.dispose();
      }
      _gadgets.clear();
      _gadgets=null;
    }
  }
}
