package delta.games.lotro.gui.lore.titles.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.billingGroups.BillingGroupDescription;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.titles.TitleReferencesBuilder;
import delta.games.lotro.lore.xrefs.titles.TitleRole;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller to display references to a title.
 * @author DAM
 */
public class TitleReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param titleID Identifier of the title to show.
   */
  public TitleReferencesDisplayController(NavigatorWindowController parent, int titleID)
  {
    _parent=parent;
    _display=buildDetailsPane(titleID);
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane(int titleID)
  {
    List<Reference<?,TitleRole>> references=getReferences(titleID);
    if (references.size()==0)
    {
      return null;
    }
    String html=getHtml(references);
    JEditorPane editor=buildEditor();
    editor.setText(html);
    editor.setCaretPosition(0);
    return editor;
  }

  private JEditorPane buildEditor()
  {
    JEditorPane editor=new JEditorPane("text/html","");
    editor.setEditable(false);
    editor.setPreferredSize(new Dimension(500,300));
    editor.setOpaque(false);
    HyperlinkListener l=new HyperlinkListener()
    {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e)
      {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
          String reference=e.getDescription();
          PageIdentifier pageId=PageIdentifier.fromString(reference);
          _parent.navigateTo(pageId);
        }
      }
    };
    editor.addHyperlinkListener(l);
    return editor;
  }

  private List<Reference<?,TitleRole>> getReferences(int titleID)
  {
    TitleReferencesBuilder builder=new TitleReferencesBuilder();
    List<Reference<?,TitleRole>> references=builder.inspectTitle(titleID);
    return references;
  }

  private String getHtml(List<Reference<?,TitleRole>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForBillingGroups(sb,references);
    buildHtmlForQuestsAndDeeds(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T> List<Reference<T,TitleRole>> getReferences(List<Reference<?,TitleRole>> references, Class<T> clazz)
  {
    List<Reference<T,TitleRole>> ret=new ArrayList<Reference<T,TitleRole>>();
    for(Reference<?,TitleRole> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((Reference<T,TitleRole>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForBillingGroups(StringBuilder sb, List<Reference<?,TitleRole>> references)
  {
    List<Reference<BillingGroupDescription,TitleRole>> billingGroupsReferences=getReferences(references,BillingGroupDescription.class);
    if (billingGroupsReferences.size()>0)
    {
      for(Reference<BillingGroupDescription,TitleRole> billingGroupsReference : billingGroupsReferences)
      {
        BillingGroupDescription billingGroup=billingGroupsReference.getSource();
        buildHtmlForBillingGroupReference(sb,billingGroup);
      }
    }
  }

  private void buildHtmlForBillingGroupReference(StringBuilder sb, BillingGroupDescription billingGroup)
  {
    String name=billingGroup.getName();
    sb.append("<p>Awarded from account feature: ");
    sb.append("<b>");
    int billingGroupID=billingGroup.getIdentifier();
    PageIdentifier to=ReferenceConstants.getBillingGroupReference(billingGroupID);
    HtmlUtils.printLink(sb,to.getFullAddress(),name);
    sb.append("</b></p>");
  }

  private void buildHtmlForQuestsAndDeeds(StringBuilder sb, List<Reference<?,TitleRole>> references)
  {
    List<Reference<Achievable,TitleRole>> achievableReferences=getReferences(references,Achievable.class);
    if (achievableReferences.size()>0)
    {
      sb.append("<h1>Quests and deeds</h1>");
      for(Reference<Achievable,TitleRole> achievableReference : achievableReferences)
      {
        buildHtmlForAchievableReference(sb,achievableReference.getSource());
      }
    }
  }

  private void buildHtmlForAchievableReference(StringBuilder sb, Achievable achievable)
  {
    sb.append("<p>Reward for ");
    boolean isQuest=(achievable instanceof QuestDescription);
    String type=isQuest?"quest ":"deed ";
    sb.append(type);
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getAchievableReference(achievable);
    HtmlUtils.printLink(sb,to.getFullAddress(),achievable.getName());
    sb.append("</b></p>");
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    _display=null;
  }
}
