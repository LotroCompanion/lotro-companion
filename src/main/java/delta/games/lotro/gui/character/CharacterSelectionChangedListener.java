package delta.games.lotro.gui.character;

/**
 * Listener for character selection changes.
 * @author DAM
 */
public interface CharacterSelectionChangedListener
{
  /**
   * Called when the selected status of a toon changed.
   * @param toonId Identifier of the selected toon.
   * @param selected New selection state.
   */
  public void selectionChanged(String toonId, boolean selected);
}
