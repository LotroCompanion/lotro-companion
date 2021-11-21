package delta.games.lotro.gui.character.status.allegiances.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.allegiances.AllegianceDescription;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for a panel to show and allegiance status.
 * @author DAM
 */
public class AllegianceStatusFormController
{
  // Data
  private AllegianceStatus _status;
  private AllegianceDescription _allegiance;
  // UI
  private JPanel _panel;
  // Controllers
  private AllegianceStatusRewardsPanelController _rewards;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param allegianceStatus Allegiance status to show.
   */
  public AllegianceStatusFormController(WindowController parent, AllegianceStatus allegianceStatus)
  {
    _status=allegianceStatus;
    _allegiance=_status.getAllegiance();
    _rewards=new AllegianceStatusRewardsPanelController(parent,allegianceStatus);
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
    // Summary panel
    JPanel summaryPanel=buildAllegianceSummaryPanel();
    // Rewards panel
    JPanel rewardsPanel=_rewards.getPanel();
    rewardsPanel.setBorder(GuiFactory.buildTitledBorder("Rewards"));

    // Assembly
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(summaryPanel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(rewardsPanel,c);

    return ret;
  }

  private JPanel buildAllegianceSummaryPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Allegiance icon
    GridBagConstraints c=new GridBagConstraints(0,0,1,3,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(buildIconGadget(),c);
    // Name
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    ret.add(buildNameGadget(),c);
    // Description
    c=new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,5),0,0);
    ret.add(buildDescriptionGadget(),c);
    // Travel skill
    JPanel travelSkillPanel=buildTravelSkillPanel();
    if (travelSkillPanel!=null)
    {
      c=new GridBagConstraints(1,2,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,5),0,0);
      ret.add(travelSkillPanel,c);
    }
    return ret;
  }

  private JPanel buildTravelSkillPanel()
  {
    SkillDescription skill=_allegiance.getTravelSkill();
    if (skill==null)
    {
      return null;
    }
    // Icon
    int iconID=skill.getIconId();
    ImageIcon icon=LotroIconsManager.getSkillIcon(iconID);
    JLabel skillIconLabel=GuiFactory.buildIconLabel(icon);
    // Text
    JLabel skillName=GuiFactory.buildLabel(skill.getName());
    // Result panel;
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
    ret.add(skillIconLabel,c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(skillName,c);
    return ret;
  }

  private JLabel buildNameGadget()
  {
    String name=_allegiance.getName();
    return GuiFactory.buildLabel(name,28.0f);
  }
 
  private JLabel buildIconGadget()
  {
    int iconID=_allegiance.getIconId();
    Image image=LotroIconsManager.getAllegianceImage(iconID);
    JLabel ret=GuiFactory.buildIconLabel(new ImageIcon(image));
    return ret;
  }

  private JComponent buildDescriptionGadget()
  {
    JEditorPane ret=GuiFactory.buildHtmlPanel();
    String html="<html><body>"+HtmlUtils.toHtml(_allegiance.getDescription())+"</body></html>";
    ret.setText(html);
    ret.setCaretPosition(0);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _allegiance=null;
    _status=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
