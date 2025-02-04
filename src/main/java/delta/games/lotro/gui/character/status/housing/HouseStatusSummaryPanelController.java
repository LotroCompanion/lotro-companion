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
import delta.games.lotro.character.status.housing.HouseAddress;
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
  private HouseAddress _address;
  // UI
  private JLabel _houseIcon;
  private JPanel _houseAddress;
  private JButton _detailsButton;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param address House address.
   */
  public HouseStatusSummaryPanelController(WindowController parent, HouseAddress address)
  {
    super(parent);
    _address=address;
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
    int houseID=_address.getHouseID();
    HousingManager mgr=HousingSystem.getInstance().getData();
    HouseDefinition house=mgr.getHouse(houseID);
    int iconID=house.getInfo().getIcon32ID();
    ImageIcon icon=LotroIconsManager.getHousingIcon(iconID);
    return GuiFactory.buildIconLabel(icon);
  }

  private MultilineLabel2 buildAddressPanel()
  {
    int houseID=_address.getHouseID();
    HousingManager mgr=HousingSystem.getInstance().getData();
    // Address
    HouseDefinition house=mgr.getHouse(houseID);
    String address=house.getAddress();
    // Neighborhood
    int neightborhoodID=_address.getNeighborhoodID();
    Neighborhood neighborhood=mgr.getNeighborhood(neightborhoodID);
    NeighborhoodTemplate template=neighborhood.getTemplate();
    String neighborhoodStr=neighborhood.getName()+", "+template.getName();

    MultilineLabel2 ret=new MultilineLabel2();
    ret.setText(new String[] {address,neighborhoodStr});
    return ret;
  }

  private void showHouse()
  {
    // TODO Show house window
    System.out.println("Show house: "+_address);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _address=null;
    _houseIcon=null;
    _houseAddress=null;
    _detailsButton=null;
  }
}
