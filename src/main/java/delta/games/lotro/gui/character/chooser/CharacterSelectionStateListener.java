package delta.games.lotro.gui.character.chooser;

/**
 * Listener for character selection state events.
 * @author DAM
 */
public interface CharacterSelectionStateListener
{
  /**
   * Called when the selected state of a toon changed.
   * @param toonId Identifier of the selected toon.
   * @param selected New selection state.
   */
  public void selectionStateChanged(String toonId, boolean selected);
}
