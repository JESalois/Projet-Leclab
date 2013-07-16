package event;

/**
 * Enum representing different events possible in execution. 
 * @author Philippe, Jean-Etienne
 */
public enum LeclabEvent {
    NEW_EXPERIMENTATION_ORDER, SITTING_STATE_CHANGED,
    //Changes within an experimentation
    PARAMETER_ADDED, PARAMETER_REMOVED, BLOCK_ADDED, BLOCK_REMOVED,
    EXPERIMENTATION_CLEARED, EXPERIMENTATION_LOADED,
    //Changes within a block
    BLOCK_NAME_CHANGED, TRIAL_REMOVED, TRIAL_ADDED, BLOCK_PRACTICE_STATE_CHANGED,
    BLOCK_RETROACTION_STATE_CHANGED,
    //Changes withing a trial
    TRIAL_WORDS_CHANGED
}
