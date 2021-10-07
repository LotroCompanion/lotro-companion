package delta.games.lotro.gui.lore.items.legendary2.traceries.chooser;

import java.util.List;

import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.lore.items.legendary2.TraceriesManager;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.lore.items.legendary2.filters.TraceryTierFilter;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * @author dm
 */
public class MainTestTraceriesChooser
{
  private Tracery chooseTracery()
  {
    LotroEnum<SocketType> typesMgr=LotroEnumsRegistry.getInstance().get(SocketType.class);
    SocketType type=typesMgr.getEntry(10);
    List<Tracery> traceries=TraceriesManager.getInstance().getTracery(type);
    Filter<Tracery> filter=new TraceryTierFilter();
    TypedProperties props=new TypedProperties();
    ObjectChoiceWindowController<Tracery> chooser=TraceryChooser.buildChooser(null,props,traceries,filter,null);
    Tracery ret=chooser.editModal();
    System.out.println(ret);
    return ret;
  }

  private void doIt()
  {
    chooseTracery();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestTraceriesChooser().doIt();
  }
}
