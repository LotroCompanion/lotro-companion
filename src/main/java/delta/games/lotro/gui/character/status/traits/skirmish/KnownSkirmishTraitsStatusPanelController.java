package delta.games.lotro.gui.character.status.traits.skirmish;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.traits.skirmish.SkirmishTraitsStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.enums.TraitNature;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;

/**
 * Panel to show slotted skirmish traits.
 * @author DAM
 */
public class KnownSkirmishTraitsStatusPanelController extends AbstractPanelController
{
  // Data
  private SkirmishTraitsStatus _status;
  // Controllers
  private List<IconLinkLabelGadgetsController> _controllers;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public KnownSkirmishTraitsStatusPanelController(WindowController parent, SkirmishTraitsStatus status)
  {
    super(parent);
    _status=status;
    _controllers=new ArrayList<IconLinkLabelGadgetsController>();
    setPanel(buildPanel(parent,status));
  }

  private JPanel buildPanel(WindowController parent, SkirmishTraitsStatus status)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    Map<TraitNature,List<TraitDescription>> allKnownTraits=getKnownTraits();
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    for(TraitNature nature : SkirmishTraitStatusUiUtils.getOrderedTraitNature())
    {
      JPanel naturePanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      List<TraitDescription> knownTraits=allKnownTraits.get(nature);
      if (knownTraits==null)
      {
        knownTraits=new ArrayList<TraitDescription>();
      }
      int y=0;
      for(TraitDescription trait : knownTraits)
      {
        int rank=status.getTraitRank(trait.getIdentifier());
        IconLinkLabelGadgetsController gadgets=GadgetsControllersFactory.build(parent,trait,rank);
        _controllers.add(gadgets);
        // Icon
        int top=(y==0)?5:0;
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(top,5,5,5),0,0);
        naturePanel.add(gadgets.getIcon().getIcon(),c);
        // Label
        c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(top,0,5,5),0,0);
        naturePanel.add(gadgets.getLink().getLabel(),c);
        y++;
      }
      JPanel padding=GuiFactory.buildPanel(null);
      GridBagConstraints c=new GridBagConstraints(2,y,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
      naturePanel.add(padding,c);
      JScrollPane scroll=GuiFactory.buildScrollPane(naturePanel);
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      tabbedPane.add(nature.getLabel(),scroll);
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,5,5,5),0,0);
    panel.add(tabbedPane,c);
    return panel;
  }

  private Map<TraitNature,List<TraitDescription>> getKnownTraits()
  {
    TraitsManager mgr=TraitsManager.getInstance();
    Map<TraitNature,List<TraitDescription>> map=new HashMap<TraitNature,List<TraitDescription>>();
    for(Integer traitId : _status.getManagedTraits())
    {
      TraitDescription trait=mgr.getTrait(traitId.intValue());
      if (trait==null)
      {
        continue;
      }
      TraitNature nature=trait.getNature();
      List<TraitDescription> list=map.get(nature);
      if (list==null)
      {
        list=new ArrayList<TraitDescription>();
        map.put(nature,list);
      }
      list.add(trait);
    }
    for(List<TraitDescription> list : map.values())
    {
      Collections.sort(list,new NamedComparator());
    }
    return map;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _status=null;
    if (_controllers!=null)
    {
      for(IconLinkLabelGadgetsController ctrl : _controllers)
      {
        ctrl.dispose();
      }
      _controllers.clear();
      _controllers=null;
    }
  }
}
