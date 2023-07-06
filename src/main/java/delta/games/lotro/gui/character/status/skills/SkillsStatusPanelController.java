package delta.games.lotro.gui.character.status.skills;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.status.skills.SkillsStatusManager;
import delta.games.lotro.character.status.skills.filters.SkillStatusFilter;
import delta.games.lotro.gui.character.status.skills.filter.SkillStatusFilterController;

/**
 * Controller for a window that show the status of skills.
 * @author DAM
 */
public class SkillsStatusPanelController
{
  // Controllers
  private WindowController _parent;
  private SkillStatusFilterController _filterController;
  private SkillsStatusDisplayPanelController _panelController;
  // Data
  private SkillsStatusManager _status;
  private SkillStatusFilter _filter;
  private List<SkillDescription> _skills;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param skills Skills to show.
   * @param status Status to show.
   */
  public SkillsStatusPanelController(WindowController parent, List<SkillDescription> skills, SkillsStatusManager status)
  {
    _parent=parent;
    _skills=new ArrayList<SkillDescription>(skills);
    _filter=new SkillStatusFilter();
    _status=status;
    _panel=buildPanel();
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Display
    _panelController=new SkillsStatusDisplayPanelController(_parent,_skills,_status,_filter);
    JPanel displayPanel=_panelController.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(displayPanel);
    scroll.setBorder(GuiFactory.buildTitledBorder("Skills")); // I18n
    // Filter
    _filterController=new SkillStatusFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    filterPanel.setBorder(GuiFactory.buildTitledBorder("Filter")); // I18n
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(scroll,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    // Data
    _status=null;
    _filter=null;
    _skills=null;
  }
}
