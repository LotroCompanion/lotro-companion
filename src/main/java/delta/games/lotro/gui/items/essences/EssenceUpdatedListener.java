package delta.games.lotro.gui.items.essences;

/**
 * Listener for essence updates.
 * @author DAM
 */
public interface EssenceUpdatedListener
{
  /**
   * Called when the essence managed by the given controller was updated.
   * @param source Source controller.
   */
  void essenceUpdated(SimpleSingleEssenceEditionController source);
}
