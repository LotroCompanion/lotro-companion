package delta.games.lotro.gui.lore.nationalities.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.races.NationalityDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.gui.utils.IconAndLinkPanelController;
import delta.games.lotro.gui.utils.SharedPanels;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for a nationality display panel.
 * @author DAM
 */
public class NationalityDisplayPanelController implements NavigablePanelController
{
  // Data
  private NationalityDescription _nationality;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private IconAndLinkPanelController _racePanel;
  private IconAndLinkPanelController _titlePanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param nationality Race to show.
   */
  public NationalityDisplayPanelController(NavigatorWindowController parent, NationalityDescription nationality)
  {
    _parent=parent;
    _nationality=nationality;
  }

  @Override
  public String getTitle()
  {
    return "Nationality: "+_nationality.getName(); // I18n
  }

  @Override
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Top panel
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
    panel.add(topPanel,c);
    // Center
    Component center=buildCenter();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(center,c);
    panel.setPreferredSize(new Dimension(500,500));
    return panel;
  }

  private JPanel buildCenter()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Description
    String description=cleanupText(_nationality.getDescription());
    // Naming guidelines
    String maleNamingGuidelines=cleanupText(_nationality.getNamingGuidelineMale());
    String femaleNamingGuidelines=cleanupText(_nationality.getNamingGuidelineFemale());
    String fullText=description+"<p>"+maleNamingGuidelines+"<p>"+femaleNamingGuidelines;
    JEditorPane display=buildEditorPane(fullText);
    JScrollPane displayPane=GuiFactory.buildScrollPane(display);
    displayPane.setBorder(GuiFactory.buildTitledBorder("Description")); // I18n
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,5,0,0),0,0);
    panel.add(displayPane,c);
    return panel;
  }

  private String cleanupText(String text)
  {
    if (text.startsWith("<li>")) text=text.substring(4);
    if (text.endsWith("</li>")) text=text.substring(0,text.length()-4);
    text=text.replace("#FFFF00","#000000");
    return text;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      /*
      int iconID=_nationality.getIconID();
      ImageIcon icon=LotroIconsManager.getNationalityIcon(iconID);
      JLabel iconLabel=GuiFactory.buildIconLabel(icon);
      panelLine.add(iconLabel);
      */
      // Name
      String name=_nationality.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(28f).deriveFont(Font.BOLD));
      panelLine.add(nameLabel);
      panel.add(panelLine,c);
      y++;
    }
    // Attributes
    // Race
    RaceDescription race=findRace(_nationality);
    if (race!=null)
    {
      _racePanel=SharedPanels.buildRacePanel(_parent,race);
      c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(_racePanel.getPanel(),c);
      y++;
    }
    // Title
    Integer titleID=_nationality.getTitleID();
    if (titleID!=null)
    {
      TitleDescription title=TitlesManager.getInstance().getTitle(titleID.intValue());
      if (title!=null)
      {
        _titlePanel=SharedPanels.buildTitlePanel(_parent,title);
        c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
        panel.add(_titlePanel.getPanel(),c);
        y++;
      }
    }

    // Padding to push everything on left
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.HORIZONTAL;
    c.weightx=1.0;
    c.gridy=0;
    panel.add(paddingPanel,c);

    return panel;
  }

  private JEditorPane buildEditorPane(String input)
  {
    JEditorPane editor=null;
    if ((input!=null) && (input.length()>0))
    {
      editor=GuiFactory.buildHtmlPanel();
      StringBuilder sb=new StringBuilder();
      sb.append("<html><body>");
      sb.append(HtmlUtils.toHtml(input));
      sb.append("</body></html>");
      editor.setText(sb.toString());
    }
    return editor;
  }

  private RaceDescription findRace(NationalityDescription nationality)
  {
    for(RaceDescription race : RacesManager.getInstance().getAll())
    {
      for(NationalityDescription raceNationality : race.getNationalities())
      {
        if (raceNationality==nationality)
        {
          return race;
        }
      }
    }
    return null;
  }

  @Override
  public void dispose()
  {
    // Data
    _nationality=null;
    // Controllers
    _parent=null;
    if (_racePanel!=null)
    {
      _racePanel.dispose();
      _racePanel=null;
    }
    if (_titlePanel!=null)
    {
      _titlePanel.dispose();
      _titlePanel=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
