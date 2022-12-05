package delta.games.lotro.gui.character.virtues;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.stats.CharacterGenerationTools;
import delta.games.lotro.character.stats.CharacterGeneratorMeva;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.character.status.virtues.VirtuesStatus;
import delta.games.lotro.character.status.virtues.io.VirtuesStatusIO;

/**
 * Test for virtues edition panel.
 * @author DAM
 */
public class MainTestVirtuesEditionPanel
{
  /**
   * Basic main method for test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    CharacterGenerationTools tools=new CharacterGenerationTools();
    CharacterGeneratorMeva mevaGenerator=new CharacterGeneratorMeva(tools);
    CharacterData meva=mevaGenerator.buildCharacter();
    VirtuesSet virtues=meva.getVirtues();
    virtues.setSelectedVirtue(null,1);
    CharacterFile toon=CharactersManager.getInstance().getToonById("Landroval","Meva");
    VirtuesStatus status=VirtuesStatusIO.load(toon);
    DefaultDialogController window=new DefaultDialogController(null);
    VirtuesEditionPanelController panelCtrl=new VirtuesEditionPanelController(window,meva.getLevel(),status);
    panelCtrl.setVirtues(virtues);
    window.getDialog().getContentPane().add(panelCtrl.getPanel());
    window.pack();
    window.show();
  }
}
