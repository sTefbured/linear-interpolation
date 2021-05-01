package linearInterpolation.model.event;

import java.util.EventObject;

/**
 * InterpolationUpdateEvent is an event which indicates that target interpolation object has been changed.
 * Changes are defined in Interpolation class.
 *
 * @author Kotikov S.G.
 * @see linearInterpolation.model.Interpolation
 */
// TODO: REMOVE MAYBE, ignored in all listener classes
public class InterpolationUpdateEvent extends EventObject {
    public InterpolationUpdateEvent(Object source) {
        super(source);
    }
}
