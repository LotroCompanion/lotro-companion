package delta.games.lotro.gui.house;

import delta.games.lotro.character.status.housing.House;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.common.enums.HouseType;
import delta.games.lotro.lore.housing.HouseDefinition;
import delta.games.lotro.lore.housing.HousingManager;
import delta.games.lotro.lore.housing.HousingSystem;
import delta.games.lotro.lore.housing.Neighborhood;
import delta.games.lotro.lore.housing.NeighborhoodTemplate;

/**
 * Entry in a table of houses.
 * @author DAM
 */
public class HouseTableEntry
{
  private House _house;
  private String _server;
  private String _neighborhoodTemplate;
  private String _neighborhood;
  private String _address;
  private HouseType _type;

  /**
   * Constructor.
   * @param house Wrapped house.
   */
  public HouseTableEntry(House house)
  {
    _house=house;
    init();
  }

  private void init()
  {
    HouseIdentifier id=_house.getIdentifier();
    _server=id.getServer();
    HouseAddress address=id.getAddress();
    HousingManager mgr=HousingSystem.getInstance().getData();
    int neighborhoodID=address.getNeighborhoodID();
    Neighborhood neighborhoodDefinition=mgr.getNeighborhood(neighborhoodID);
    _neighborhood=neighborhoodDefinition.getName();
    NeighborhoodTemplate neighborhoodTemplate=neighborhoodDefinition.getTemplate();
    _neighborhoodTemplate=neighborhoodTemplate.getName();
    int houseID=address.getHouseID();
    HouseDefinition houseDefinition=mgr.getHouse(houseID);
    _address=houseDefinition.getAddress();
    _type=houseDefinition.getHouseType();
  }

  /**
   * Get the wrapped house.
   * @return a house.
   */
  public House getHouse()
  {
    return _house;
  }

  /**
   * Get the server name.
   * @return a server name.
   */
  public String getServer()
  {
    return _server;
  }

  /**
   * Get the neighborhood template (homestead).
   * @return the neighborhood template (homestead) name.
   */
  public String getNeighborhoodTemplate()
  {
    return _neighborhoodTemplate;
  }

  /**
   * Get the neighborhood name.
   * @return the neighborhood name.
   */
  public String getNeighborhood()
  {
    return _neighborhood;
  }

  /**
   * Get the address.
   * @return the address.
   */
  public String getAddress()
  {
    return _address;
  }

  /**
   * Get the house type.
   * @return the house type.
   */
  public HouseType getType()
  {
    return _type;
  }
}
