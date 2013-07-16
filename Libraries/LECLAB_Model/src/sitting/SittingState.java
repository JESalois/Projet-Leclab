package sitting;

/**
 * States use by the sitting manager. Part of the state design pattern.
 * @author Jean-Etienne Salois
 */
public enum SittingState{
   WAITING_ON_SUPERVISOR, 
   START_OF_BLOCK,
   PLAYING_VISUAL_DISTRACTOR,
   PLAYING_TACTILE_DISTRACTOR,
   PLAYING_TRIAL_VIDEO,
   WAITING_USER_INPUT,
   END_OF_SITTING,
   START_OF_TRIAL,
   START_OF_PRACTICE
}
