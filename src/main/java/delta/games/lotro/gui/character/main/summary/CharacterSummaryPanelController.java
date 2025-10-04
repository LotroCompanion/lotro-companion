package delta.games.lotro.gui.character.main.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.details.CharacterDetails;
import delta.games.lotro.character.races.NationalityDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.geo.Position;
import delta.games.lotro.common.geo.PositionUtils;
import delta.games.lotro.common.id.InternalGameId;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.titles.TitleUiUtils;
import delta.games.lotro.gui.lore.titles.TitleUiUtils.TitleRenderingFormat;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipsManager;
import delta.games.lotro.lore.maps.Area;
import delta.games.lotro.lore.maps.Dungeon;
import delta.games.lotro.lore.maps.DungeonsManager;
import delta.games.lotro.lore.maps.GeoAreasManager;
import delta.games.lotro.lore.pvp.RankScaleKeys;
import delta.games.lotro.lore.pvp.RanksManager;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Panel to show a character summary.
 * @author DAM
 */
public class CharacterSummaryPanelController extends AbstractPanelController
{
  private JLabel _name;
  private JLabel _surname;
  private NavigationHyperLink _race;
  private JLabel _gender;
  private NavigationHyperLink _nationality;
  private JLabel _class;
  private JLabel _kinship;
  private JLabel _level;
  private JLabel _pvpRank;
  private NavigationHyperLink _title;
  private JLabel _positionZone;
  private JLabel _position;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public CharacterSummaryPanelController(WindowController parent)
  {
    super(parent);
    _name=GuiFactory.buildLabel("");
    _surname=GuiFactory.buildLabel("");
    _race=new NavigationHyperLink(parent,"",null);
    _gender=GuiFactory.buildLabel("");
    _nationality=new NavigationHyperLink(parent,"",null);
    _class=GuiFactory.buildLabel("");
    _kinship=GuiFactory.buildLabel("");
    _level=GuiFactory.buildLabel("");
    _pvpRank=GuiFactory.buildLabel("");
    _title=new NavigationHyperLink(parent,"",null);
    _positionZone=GuiFactory.buildLabel("");
    _position=GuiFactory.buildLabel("");
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints cLabel=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,0,0),0,0);
    GridBagConstraints cValue=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,0,5),0,0);
    // Name
    ret.add(GuiFactory.buildLabel("Name:"),cLabel);
    ret.add(_name,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Surname
    ret.add(GuiFactory.buildLabel("Surname:"),cLabel);
    ret.add(_surname,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Race
    ret.add(GuiFactory.buildLabel("Race:"),cLabel);
    ret.add(_race.getLabel(),cValue);
    cLabel.gridy++;cValue.gridy++;
    // Gender
    ret.add(GuiFactory.buildLabel("Gender:"),cLabel);
    ret.add(_gender,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Nationality
    ret.add(GuiFactory.buildLabel("Nationality:"),cLabel);
    ret.add(_nationality.getLabel(),cValue);
    cLabel.gridy++;cValue.gridy++;
    // Class
    ret.add(GuiFactory.buildLabel("Class:"),cLabel);
    ret.add(_class,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Kinship
    ret.add(GuiFactory.buildLabel("Kinship:"),cLabel);
    ret.add(_kinship,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Level
    ret.add(GuiFactory.buildLabel("Level:"),cLabel);
    ret.add(_level,cValue);
    cLabel.gridy++;cValue.gridy++;
    // PVP rank
    ret.add(GuiFactory.buildLabel("PVP Rank:"),cLabel);
    ret.add(_pvpRank,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Title
    ret.add(GuiFactory.buildLabel("Title:"),cLabel);
    ret.add(_title.getLabel(),cValue);
    cLabel.gridy++;cValue.gridy++;
    // Position
    ret.add(GuiFactory.buildLabel("Position:"),cLabel);
    ret.add(_positionZone,cValue);
    cLabel.gridy++;cValue.gridy++;
    ret.add(_position,cValue);
    cValue.gridy++;
    // Glue
    GridBagConstraints c=new GridBagConstraints(0,cValue.gridy,3,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(Box.createGlue(),c);
    return ret;
  }

  /**
   * Set the data to show.
   * @param summary Summary to show.
   * @param details Details to show.
   */
  public void setSummary(CharacterSummary summary, CharacterDetails details)
  {
    // Name
    String name=summary.getName();
    _name.setText(name);
    _surname.setText(summary.getSurname());
    // Race
    RaceDescription race=summary.getRace();
    if (race!=null)
    {
      PageIdentifier racePageId=ReferenceConstants.getRaceReference(race);
      _race.setText(race.getName());
      _race.setPageIdentifier(racePageId);
    }
    else
    {
      _race.setText("");
    }
    // Gender
    CharacterSex gender=summary.getCharacterSex();
    String genderLabel=(gender!=null)?gender.getLabel():"?";
    _gender.setText(genderLabel);
    // Nationality
    NationalityDescription nationality=summary.getNationality();
    if (nationality!=null)
    {
      PageIdentifier nationalityPageId=ReferenceConstants.getNationalityReference(nationality);
      _nationality.setText(nationality.getName());
      _nationality.setPageIdentifier(nationalityPageId);
    }
    else
    {
      _nationality.setText("");
    }
    // Class
    ClassDescription characterClass=summary.getCharacterClass();
    if (characterClass!=null)
    {
      _class.setText(characterClass.getName());
    }
    else
    {
      _class.setText("");
    }
    // Kinship
    InternalGameId kinshipID=summary.getKinshipID();
    if (kinshipID!=null)
    {
      Kinship kinship=KinshipsManager.getInstance().getKinshipByID(kinshipID.asLong());
      if (kinship!=null)
      {
        _kinship.setText(kinship.getName());
      }
      else
      {
        _kinship.setText("");
      }
    }
    // Level
    int level=summary.getLevel();
    _level.setText(String.valueOf(level));
    // PVP Rank
    Integer rankCode=summary.getRankCode();
    String rank=RanksManager.getInstance().getRankLabel(rankCode,RankScaleKeys.RENOWN);
    if (rank!=null)
    {
      rank=ContextRendering.render(this,rank)+" ("+rankCode+")";
    }
    else
    {
      rank="?";
    }
    _pvpRank.setText(rank);
    // Title
    Integer titleID=details.getCurrentTitleId();
    TitleDescription title=null;
    if (titleID!=null)
    {
      title=TitlesManager.getInstance().getTitle(titleID.intValue());
    }
    if (title!=null)
    {
      PageIdentifier titlePageId=ReferenceConstants.getTitleReference(title.getIdentifier());
      String titleName=TitleUiUtils.renderTitle(this,title,TitleRenderingFormat.MINIMAL);
      _title.setText(titleName);
      _title.setPageIdentifier(titlePageId);
    }
    else
    {
      _title.setText("");
    }
    // Position
    String positionZone=getPositionZone(details);
    _positionZone.setText(positionZone);
    String position=getPosition(details);
    _position.setText(position);
  }

  private String getPositionZone(CharacterDetails details)
  {
    String locationName=null;
    Integer dungeonID=details.getDungeonID();
    if (dungeonID!=null)
    {
      DungeonsManager dungeonsManager=DungeonsManager.getInstance();
      Dungeon dungeon=dungeonsManager.getDungeonById(dungeonID.intValue());
      if (dungeon!=null)
      {
        locationName=dungeon.getName();
      }
    }
    if (locationName==null)
    {
      Integer areaID=details.getAreaID();
      if (areaID!=null)
      {
        GeoAreasManager geoAreasManager=GeoAreasManager.getInstance();
        Area area=geoAreasManager.getAreaById(areaID.intValue());
        if (area!=null)
        {
          locationName=area.getName();
        }
      }
    }
    return (locationName!=null)?locationName:"?";
  }

  private String getPosition(CharacterDetails details)
  {
    Position position=details.getPosition();
    if (position!=null)
    {
      String positionStr=PositionUtils.getLabel(position);
      return positionStr;
    }
    return "?";
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _name=null;
    if (_race!=null)
    {
      _race.dispose();
      _race=null;
    }
    _gender=null;
    if (_nationality!=null)
    {
      _nationality.dispose();
      _nationality=null;
    }
    _class=null;
    _kinship=null;
    _level=null;
    _pvpRank=null;
    if (_title!=null)
    {
      _title.dispose();
      _title=null;
    }
    _positionZone=null;
    _position=null;
  }
}
