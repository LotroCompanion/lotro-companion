package delta.games.lotro.gui.clientImport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.games.lotro.memory.facade.data.ImportStatus;
import delta.games.lotro.memory.facade.data.ImportStatusData;

/**
 * Controller for a panel to display the result/status of an import.
 * @author DAM
 */
public class ImportResultDisplayController implements Disposable
{
  // Child controllers
  private ClientDataDisplayPanelController _clientDataCtrl;
  private CharacterDisplayPanelController _characterCtrl;
  private ExtractionResultsPanelController _extractionCtrl;
  // UI
  private JPanel _panel;
  private JLabel _message;

  /**
   * Constructor.
   */
  public ImportResultDisplayController()
  {
    _panel=buildPanel();
  }
  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }
  
  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Global message
    JPanel messagePanel=buildMessagePanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    ret.add(messagePanel,c);
    // Results
    JPanel resultsPanel=buildResultsPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    ret.add(resultsPanel,c);
    return ret;
  }

  private JPanel buildResultsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Client panel
    _clientDataCtrl=new ClientDataDisplayPanelController();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel clientPanel=_clientDataCtrl.getPanel();
    clientPanel.setBorder(GuiFactory.buildTitledBorder("Client")); // I18n
    ret.add(clientPanel,c);
    // Character panel
    _characterCtrl=new CharacterDisplayPanelController();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel characterPanel=_characterCtrl.getPanel();
    characterPanel.setBorder(GuiFactory.buildTitledBorder("Character")); // I18n
    ret.add(characterPanel,c);
    // Extraction results panel
    _extractionCtrl=new ExtractionResultsPanelController();
    JPanel extractionPanel=_extractionCtrl.getPanel();
    extractionPanel.setBorder(GuiFactory.buildTitledBorder("Extraction")); // I18n
    c=new GridBagConstraints(1,0,1,2,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(extractionPanel,c);
    return ret;
  }

  private JPanel buildMessagePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(GuiFactory.buildLabel("Result: "),c); // I18n
    _message=GuiFactory.buildLabel("");
    c=new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_message,c);
    return ret;
  }

  /**
   * Update UI using the given data.
   * @param data Input data.
   */
  public void updateUi(ImportStatusData data)
  {
    String message=getMessageFromStatusData(data);
    _message.setText(message);
    _clientDataCtrl.updateUi(data);
    _characterCtrl.updateUi(data);
    _extractionCtrl.updateUi(data);
  }

  private String getMessageFromStatusData(ImportStatusData data)
  {
    ImportStatus importStatus=data.getImportStatus();
    if (importStatus==ImportStatus.OFF)
    {
      return "";
    }
    if (importStatus==ImportStatus.FAILED)
    {
      String failureMessage=data.getFailureMessage();
      return failureMessage;
    }
    if (importStatus==ImportStatus.RUNNING)
    {
      return "Import in progress"; // I18n
    }
    if (importStatus==ImportStatus.FINISHED)
    {
      return "Import finished!"; // I18n
    }
    return "?";
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // UI
    _message=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_clientDataCtrl!=null)
    {
      _clientDataCtrl.dispose();
      _clientDataCtrl=null;
    }
    if (_characterCtrl!=null)
    {
      _characterCtrl.dispose();
      _characterCtrl=null;
    }
    if (_extractionCtrl!=null)
    {
      _extractionCtrl.dispose();
      _extractionCtrl=null;
    }
  }
}
