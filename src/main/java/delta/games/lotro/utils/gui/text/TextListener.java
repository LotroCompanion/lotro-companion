package delta.games.lotro.utils.gui.text;

/**
 * Text contents listener.
 * @author DAM
 */
public interface TextListener
{
  /**
   * Called when the text of a gadget has changed.
   * @param newText New value.
   */
  void textChanged(String newText);
}
