package delta.games.lotro.utils.gui.text;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Controller to handle dynamic text change in a text field.
 * @author DAM
 */
public class DynamicTextEditionController
{
  private JTextField _textField;
  private TextListener _listener;
  private DocumentListener _docListener;

  /**
   * Constructor.
   * @param textField Text field to use.
   * @param listener Listener to setup.
   */
  public DynamicTextEditionController(JTextField textField, TextListener listener)
  {
    _textField=textField;
    _listener=listener;
    setup();
  }

  private void setup()
  {
    _docListener=new DocumentListener()
    {
      public void removeUpdate(DocumentEvent e)
      {
        doIt();
      }

      public void insertUpdate(DocumentEvent e)
      {
        doIt();
      }

      public void changedUpdate(DocumentEvent e)
      {
        doIt();
      }

      private void doIt()
      {
        String text=_textField.getText();
        _listener.textChanged(text);
      }
    };
    _textField.getDocument().addDocumentListener(_docListener);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_textField!=null)
    {
      _textField.getDocument().removeDocumentListener(_docListener);
      _textField=null;
    }
    _docListener=null;
    _listener=null;
  }
}
