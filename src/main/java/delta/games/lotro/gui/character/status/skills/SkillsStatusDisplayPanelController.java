package delta.games.lotro.gui.character.status.skills;

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
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.status.skills.SkillStatus;
import delta.games.lotro.character.status.skills.SkillsStatusManager;
import delta.games.lotro.character.status.skills.filters.SkillStatusFilter;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;

/**
 * A panel to display the status of a collection of skills.
 * @author DAM
 */
public class SkillsStatusDisplayPanelController implements FilterUpdateListener
{
  // Data
  private List<SkillDescription> _skills;
  private SkillsStatusManager _status;
  private SkillStatusFilter _filter;
  // Controllers
  private WindowController _parent;
  private List<IconLinkLabelGadgetsController> _gadgets;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param skills Skills to show.
   * @param status Status to show.
   * @param filter Filter to use.
   */
  public SkillsStatusDisplayPanelController(WindowController parent, List<SkillDescription> skills, SkillsStatusManager status, SkillStatusFilter filter)
  {
    _parent=parent;
    _skills=new ArrayList<SkillDescription>(skills);
    Collections.sort(_skills,new NamedComparator());
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
    for(SkillDescription skill : _skills)
    {
      IconLinkLabelGadgetsController ctrl=GadgetsControllersFactory.build(_parent,skill);
      _gadgets.add(ctrl);
      // Configure
      SkillStatus skillStatus=_status.get(skill,true);
      boolean available=skillStatus.isAvailable();
      if (!available)
      {
        Icon icon=ctrl.getIcon().getIcon().getIcon();
        Icon otherIcon=new TransparentIcon(icon,0.5f);
        ctrl.getIcon().setIcon(otherIcon);
        ctrl.getLink().setColor(Color.GRAY);
      }
    }
  }

  private void updatePanel()
  {
    _panel.removeAll();
    int y=0;
    int nbSkills=_skills.size();
    for(int i=0;i<nbSkills;i++)
    {
      SkillDescription skill=_skills.get(i);
      SkillStatus skillStatus=_status.get(skill,true);
      if (!_filter.accept(skillStatus))
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
    _skills=null;
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
