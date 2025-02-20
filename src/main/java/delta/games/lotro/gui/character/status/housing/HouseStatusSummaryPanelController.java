package delta.games.lotro.gui.character.status.housing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.housing.House;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.character.status.housing.io.HousingStatusIO;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.housing.HouseDefinition;
import delta.games.lotro.lore.housing.HousingManager;
import delta.games.lotro.lore.housing.HousingSystem;
import delta.games.lotro.lore.housing.Neighborhood;
import delta.games.lotro.lore.housing.NeighborhoodTemplate;

/**
 * Panel to display a house status summary.
 * @author DAM
 */
public class HouseStatusSummaryPanelController extends AbstractPanelController
{
  // Data
  private HouseIdentifier _houseId;
  // UI
  private JLabel _houseIcon;
  private JPanel _houseAddress;
  private JButton _detailsButton;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param houseId House identifier.
   */
  public HouseStatusSummaryPanelController(WindowController parent, HouseIdentifier houseId)
  {
    super(parent);
    _houseId=houseId;
    init();
    setPanel(buildPanel());
  }

  private void init()
  {
    // Icon
    _houseIcon=buildHouseIcon();
    // Address
    _houseAddress=buildAddressPanel();
    // Details
    _detailsButton=GuiFactory.buildButton("Details...");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showHouse();
      }
    };
    _detailsButton.addActionListener(al);
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    ret.add(_houseIcon,c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_houseAddress,c);
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    ret.add(_detailsButton,c);
    return ret;
  }

  private JLabel buildHouseIcon()
  {
    int houseID=_houseId.getAddress().getHouseID();
    HousingManager mgr=HousingSystem.getInstance().getData();
    HouseDefinition house=mgr.getHouse(houseID);
    int iconID=house.getInfo().getIcon32ID();
    ImageIcon icon=LotroIconsManager.getHousingIcon(iconID);
    return GuiFactory.buildIconLabel(icon);
  }

  private MultilineLabel2 buildAddressPanel()
  {
    int houseID=getHouseID();
    HousingManager mgr=HousingSystem.getInstance().getData();
    // Address
    HouseDefinition house=mgr.getHouse(houseID);
    String address=house.getAddress();
    // Neighborhood
    int neightborhoodID=getNeighborhoodID();
    Neighborhood neighborhood=mgr.getNeighborhood(neightborhoodID);
    NeighborhoodTemplate template=neighborhood.getTemplate();
    String neighborhoodStr=neighborhood.getName()+", "+template.getName();

    MultilineLabel2 ret=new MultilineLabel2();
    ret.setText(new String[] {address,neighborhoodStr});
    return ret;
  }

  private int getHouseID()
  {
    return _houseId.getAddress().getHouseID();
  }

  private int getNeighborhoodID()
  {
    return _houseId.getAddress().getNeighborhoodID();
  }

  private void showHouse()
  {
    House house=HousingStatusIO.loadHouse(_houseId);
    if (house==null)
    {
      GuiFactory.showInformationDialog(getPanel(),"House not found!\nUse import to get it.","Warning!");
      return;
    }
    WindowController parent=getWindowController();
    HouseDisplayWindowController w=new HouseDisplayWindowController(parent,house);
    w.bringToFront();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _houseId=null;
    _houseIcon=null;
    _houseAddress=null;
    _detailsButton=null;
  }
}
