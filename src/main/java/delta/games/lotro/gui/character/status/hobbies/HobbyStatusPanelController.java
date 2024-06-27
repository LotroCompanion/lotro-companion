package delta.games.lotro.gui.character.status.hobbies;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.status.hobbies.HobbyStatus;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.titles.TitleUiUtils;
import delta.games.lotro.gui.lore.titles.TitleUiUtils.TitleRenderingFormat;
import delta.games.lotro.gui.utils.IconAndLinkPanelController;
import delta.games.lotro.gui.utils.SharedPanels;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.titles.TitleDescription;

/**
 * Controller for a panel to display the status of a single hobby.
 * @author DAM
 */
public class HobbyStatusPanelController
{
  // UI
  private JPanel _panel;
  // Controllers
  private IconAndLinkPanelController _hobby;
  private HyperLinkController _title;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public HobbyStatusPanelController(WindowController parent, HobbyStatus status)
  {
    _panel=buildPanel(parent,status);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel(WindowController parent, HobbyStatus status)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Hobby icon and name
    HobbyDescription hobby=status.getHobby();
    _hobby=SharedPanels.buildHobbyPanel(parent,hobby);
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_hobby.getPanel(),c);
    // Proficiency
    int proficiency=status.getValue();
    String proficiencyValue=(proficiency>=0)?L10n.getString(proficiency):"?";
    JLabel proficiencyLabel=GuiFactory.buildLabel("Proficiency: "+proficiencyValue); // I18n
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
    ret.add(proficiencyLabel,c);
    // Title
    c=new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0);
    TitleDescription title=hobby.getTitleForProficiency(proficiency);
    if (title!=null)
    {
      PageIdentifier titlePageId=ReferenceConstants.getTitleReference(title.getIdentifier());
      String titleName=TitleUiUtils.renderTitle(parent,title,TitleRenderingFormat.MINIMAL);
      _title=new NavigationHyperLink(parent,titleName,titlePageId);
      ret.add(_title.getLabel(),c);
    }
    else
    {
      ret.add(GuiFactory.buildLabel("-"),c);
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_hobby!=null)
    {
      _hobby.dispose();
      _hobby=null;
    }
    if (_title!=null)
    {
      _title.dispose();
      _title=null;
    }
  }
}
