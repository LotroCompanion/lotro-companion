package delta.games.lotro.gui.character.status.housing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.housing.House;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.gui.lore.housing.form.HouseSummaryDisplayPanelController;
import delta.games.lotro.house.HouseEntry;

/**
 * Controller for a window to show a house and its contents.
 * @author DAM
 */
public class HouseDisplayWindowController extends DefaultWindowController
{
  private static final String IDENTIFIER_SEED="HOUSE-";

  // Data
  private House _house;
  // Controllers
  private HouseSummaryDisplayPanelController _houseSummary;
  private HouseContentsDisplayPanelController _interiorController;
  private HouseContentsDisplayPanelController _exteriorController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param house House to show.
   */
  public HouseDisplayWindowController(WindowController parent, House house)
  {
    super(parent);
    _house=house;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    String title=buildTitle();
    frame.setTitle(title);
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(950,700);
    return frame;
  }

  private String buildTitle()
  {
    HouseEntry entry=new HouseEntry(_house.getIdentifier());
    return "House: "+entry.getDisplayName();
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return getWindowIdentifier(_house.getIdentifier());
  }

  /**
   * Get an identifier for a house window.
   * @param id House identifier.
   * @return A window identifier.
   */
  public static String getWindowIdentifier(HouseIdentifier id)
  {
    String server=id.getServer();
    HouseAddress address=id.getAddress();
    int neighborhoodID=address.getNeighborhoodID();
    int houseID=address.getHouseID();
    return IDENTIFIER_SEED+neighborhoodID+"-"+houseID+"@"+server;
  }

  @Override
  protected JPanel buildContents()
  {
    initControllers();
    return buildUi();
  }

  private JPanel buildUi()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary
    JPanel topPanel=_houseSummary.getPanel();
    // Contents
    JTabbedPane tab=GuiFactory.buildTabbedPane();
    tab.add("Interior",_interiorController.getPanel());
    tab.add("Exterior",_exteriorController.getPanel());
    tab.setBorder(GuiFactory.buildTitledBorder("House contents"));

    // Assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,0,5,0),0,0);
    panel.add(topPanel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tab,c);
    return panel;
  }

  private void initControllers()
  {
    _houseSummary=new HouseSummaryDisplayPanelController(this,_house.getIdentifier().getAddress());
    // Interior
    _interiorController=new HouseContentsDisplayPanelController(this,_house.getInterior());
    // Exterior
    _exteriorController=new HouseContentsDisplayPanelController(this,_house.getExterior());
  }

  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    // Data
    _house=null;
    // Controllers
    if (_houseSummary!=null)
    {
      _houseSummary.dispose();
      _houseSummary=null;
    }
    if (_interiorController!=null)
    {
      _interiorController.dispose();
      _interiorController=null;
    }
    if (_exteriorController!=null)
    {
      _exteriorController.dispose();
      _exteriorController=null;
    }
  }
}
