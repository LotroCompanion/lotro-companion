package delta.games.lotro.gui.character.status.housing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.status.housing.AccountHousingData;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.character.status.housing.HouseReference;
import delta.games.lotro.common.id.InternalGameId;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipSummary;
import delta.games.lotro.kinship.KinshipsManager;

/**
 * Panel to display a house status summary.
 * @author DAM
 */
public class CharacterHousingStatusPanelController extends AbstractPanelController
{
  // Data
  private AccountHousingData _data;
  private CharacterFile _character;
  // Controllers
  private List<HouseStatusSummaryPanelController> _houseStatusCtrls;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Current character.
   * @param data Data to show.
   */
  public CharacterHousingStatusPanelController(WindowController parent, CharacterFile character, AccountHousingData data)
  {
    super(parent);
    _data=data;
    _character=character;
    _houseStatusCtrls=new ArrayList<HouseStatusSummaryPanelController>();
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    // Classic house
    HouseReference classicHouse=_data.getClassicHouse();
    if (classicHouse!=null)
    {
      List<HouseReference> houses=new ArrayList<HouseReference>();
      houses.add(classicHouse);
      JPanel classicHousePanel=buildStatusPanel(houses);
      classicHousePanel.setBorder(GuiFactory.buildTitledBorder("Classic house"));
      ret.add(classicHousePanel,c);
      c.gridy++;
    }
    // Kinship house?
    HouseReference kinshipHouse=getKinshipHouse();
    if (kinshipHouse!=null)
    {
      List<HouseReference> houses=new ArrayList<HouseReference>();
      houses.add(kinshipHouse);
      JPanel kinshipHousePanel=buildStatusPanel(houses);
      kinshipHousePanel.setBorder(GuiFactory.buildTitledBorder("Kinship house"));
      ret.add(kinshipHousePanel,c);
      c.gridy++;
    }
    // Premium houses
    List<HouseReference> premiumHouses=_data.getPremiumHouses();
    if (!premiumHouses.isEmpty())
    {
      JPanel premiumHousesPanel=buildStatusPanel(premiumHouses);
      premiumHousesPanel.setBorder(GuiFactory.buildTitledBorder("Premium houses"));
      ret.add(premiumHousesPanel,c);
    }
    return ret;
  }

  private HouseReference getKinshipHouse()
  {
    CharacterSummary summary=_character.getSummary();
    if (summary==null)
    {
      return null;
    }
    InternalGameId kinshipID=summary.getKinshipID();
    if (kinshipID==null)
    {
      return null;
    }
    Kinship kinship=KinshipsManager.getInstance().getKinshipByID(kinshipID.asLong());
    if (kinship==null)
    {
      return null;
    }
    KinshipSummary kinshipSummary=kinship.getSummary();
    HouseAddress address=kinshipSummary.getAddress();
    if (address==null)
    {
      return null;
    }
    HouseReference ref=new HouseReference(address,null);
    return ref;
  }

  private JPanel buildStatusPanel(List<HouseReference> houses)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    for(HouseReference house : houses)
    {
      WindowController parent=getWindowController();
      HouseIdentifier houseId=new HouseIdentifier(_data.getServer(),house.getAddress());
      HouseStatusSummaryPanelController ctrl=new HouseStatusSummaryPanelController(parent,houseId);
      _houseStatusCtrls.add(ctrl);
      ret.add(ctrl.getPanel(),c);
      c.gridy++;
    }
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_houseStatusCtrls!=null)
    {
      for(HouseStatusSummaryPanelController ctrl : _houseStatusCtrls)
      {
        ctrl.dispose();
      }
      _houseStatusCtrls.clear();
      _houseStatusCtrls=null;
    }
  }
}
