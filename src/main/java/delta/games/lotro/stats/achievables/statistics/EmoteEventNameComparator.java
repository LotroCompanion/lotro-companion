package delta.games.lotro.stats.achievables.statistics;

import java.util.Comparator;

/**
 * Comparator for EmoteEvent that uses the emote name.
 * @author DAM
 */
public class EmoteEventNameComparator implements Comparator<EmoteEvent>
{
  @Override
  public int compare(EmoteEvent o1, EmoteEvent o2)
  {
    String emote1=o1.getEmote();
    String emote2=o2.getEmote();
    return emote1.compareTo(emote2);
  }
}
