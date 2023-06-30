package delta.games.lotro.gui.character.status.reputation.synopsis;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test for the reputation synopsis.
 * @author DAM
 */
public class MainTestReputationSynopsis
{
  private void doIt()
  {
    CharactersManager mgr=CharactersManager.getInstance();
    List<CharacterFile> allToons=mgr.getAllToons();
    DefaultWindowController w=new DefaultWindowController();
    ReputationSynopsisTableController table=new ReputationSynopsisTableController(w,null);
    table.setToons(allToons);
    JTable jtable=table.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(jtable);
    w.getFrame().add(scroll);
    w.getFrame().pack();
    w.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestReputationSynopsis().doIt();
  }
}
