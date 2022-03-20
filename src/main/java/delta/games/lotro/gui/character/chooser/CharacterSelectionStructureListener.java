package delta.games.lotro.gui.character.chooser;

/**
 * Listener for character selection structure change events.
 * @author DAM
 */
public interface CharacterSelectionStructureListener
{
  /**
   * Called when the structure of the toon selection changed.
   * @param event Event details.
   */
  public void selectionStructureChanged(CharacterSelectionStructureChangeEvent event);
}
