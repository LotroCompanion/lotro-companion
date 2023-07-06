package delta.games.lotro.gui.character.status.emotes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.emotes.EmotesStatusManager;
import delta.games.lotro.character.status.emotes.filters.EmoteStatusFilter;
import delta.games.lotro.gui.character.status.emotes.filter.EmoteStatusFilterController;
import delta.games.lotro.lore.emotes.EmoteDescription;

/**
 * Controller for a window that show the status of emotes.
 * @author DAM
 */
public class EmotesStatusPanelController
{
  // Controllers
  private WindowController _parent;
  private EmoteStatusFilterController _filterController;
  private EmotesStatusDisplayPanelController _panelController;
  // Data
  private EmotesStatusManager _status;
  private EmoteStatusFilter _filter;
  private List<EmoteDescription> _emotes;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param emotes Emotes to show.
   * @param status Status to show.
   */
  public EmotesStatusPanelController(WindowController parent, List<EmoteDescription> emotes, EmotesStatusManager status)
  {
    _parent=parent;
    _emotes=new ArrayList<EmoteDescription>(emotes);
    _filter=new EmoteStatusFilter();
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
    _panelController=new EmotesStatusDisplayPanelController(_parent,_emotes,_status,_filter);
    JPanel displayPanel=_panelController.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(displayPanel);
    scroll.setBorder(GuiFactory.buildTitledBorder("Emotes")); // I18n
    // Filter
    _filterController=new EmoteStatusFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    filterPanel.setBorder(GuiFactory.buildTitledBorder("Filter")); // I18n
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
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
    _emotes=null;
  }
}
