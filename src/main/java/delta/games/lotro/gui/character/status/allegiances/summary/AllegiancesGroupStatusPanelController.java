package delta.games.lotro.gui.character.status.allegiances.summary;

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
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.character.status.allegiances.AllegiancesStatusManager;
import delta.games.lotro.common.enums.AllegianceGroup;
import delta.games.lotro.gui.character.status.allegiances.form.AllegianceStatusWindowController;
import delta.games.lotro.lore.allegiances.AllegianceDescription;
import delta.games.lotro.lore.allegiances.AllegiancesManager;

/**
 * Controller for a panel that display the status for a group of allegiances.
 * @author DAM
 */
public class AllegiancesGroupStatusPanelController
{
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<SingleAllegianceGadgetsController> _gadgets;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param group Group to show.
   * @param statusMgr Status to show.
   */
  public AllegiancesGroupStatusPanelController(WindowController parent, AllegianceGroup group, AllegiancesStatusManager statusMgr)
  {
    _parent=parent;
    _gadgets=new ArrayList<SingleAllegianceGadgetsController>();
    init(group,statusMgr);
    _panel=buildPanel();
    _panel.setBorder(GuiFactory.buildTitledBorder(group.getLabel()));
  }

  private void init(AllegianceGroup group, AllegiancesStatusManager statusMgr)
  {
    AllegiancesManager mgr=AllegiancesManager.getInstance();
    List<AllegianceDescription> allegiances=mgr.getAllegiances(group);
    for(AllegianceDescription allegiance : allegiances)
    {
      AllegianceStatus status=statusMgr.get(allegiance,true);
      SingleAllegianceGadgetsController gadgets=new SingleAllegianceGadgetsController(status);
      _gadgets.add(gadgets);
    }
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(SingleAllegianceGadgetsController gadgets : _gadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      ret.add(gadgets.getIcon(),c);
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
      ret.add(gadgets.getNameGadget(),c);
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      ret.add(gadgets.getStateGadget(),c);
      c=new GridBagConstraints(3,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      final AllegianceStatus status=gadgets.getStatus();
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          showAllegianceStatusDetails(status);
        }
      };
      JButton button=gadgets.getDetailsButton();
      button.addActionListener(al);
      ret.add(button,c);
      y++;
    }
    return ret;
  }

  private void showAllegianceStatusDetails(AllegianceStatus status)
  {
    WindowsManager mgr=_parent.getWindowsManager();
    AllegianceDescription allegiance=status.getAllegiance();
    String identifier=AllegianceStatusWindowController.getIdentifier(allegiance);
    WindowController child=mgr.getWindow(identifier);
    if (child==null)
    {
      child=new AllegianceStatusWindowController(_parent,status);
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
      for(SingleAllegianceGadgetsController gadget : _gadgets)
      {
        gadget.dispose();
      }
      _gadgets.clear();
      _gadgets=null;
    }
  }
}
