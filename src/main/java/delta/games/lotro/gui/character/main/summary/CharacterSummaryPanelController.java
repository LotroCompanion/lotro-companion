package delta.games.lotro.gui.character.main.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipsManager;
import delta.games.lotro.lore.maps.Area;
import delta.games.lotro.lore.maps.Dungeon;
import delta.games.lotro.lore.maps.DungeonsManager;
import delta.games.lotro.lore.maps.GeoAreasManager;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.lore.titles.TitlesManager;

/**
 * Panel to show a character summary.
 * @author DAM
 */
public class CharacterSummaryPanelController extends AbstractPanelController
{
  private JLabel _name;
  private NavigationHyperLink _race;
  private JLabel _gender;
  private NavigationHyperLink _nationality;
  private NavigationHyperLink _class;
  private JLabel _kinship;
  private JLabel _level;
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
    _race=new NavigationHyperLink(parent,"",null);
    _gender=GuiFactory.buildLabel("");
    _nationality=new NavigationHyperLink(parent,"",null);
    _class=new NavigationHyperLink(parent,"",null);
    _kinship=GuiFactory.buildLabel("");
    _level=GuiFactory.buildLabel("");
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
    ret.add(_class.getLabel(),cValue);
    cLabel.gridy++;cValue.gridy++;
    // Kinship
    ret.add(GuiFactory.buildLabel("Kinship:"),cLabel);
    ret.add(_kinship,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Level
    ret.add(GuiFactory.buildLabel("Level:"),cLabel);
    ret.add(_level,cValue);
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
      PageIdentifier classPageId=ReferenceConstants.getClassReference(characterClass);
      _class.setText(characterClass.getName());
      _class.setPageIdentifier(classPageId);
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
      _title.setText(title.getName());
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
    if (_class!=null)
    {
      _class.dispose();
      _class=null;
    }
    _kinship=null;
    _level=null;
    if (_title!=null)
    {
      _title.dispose();
      _title=null;
    }
    _positionZone=null;
    _position=null;
  }
}
