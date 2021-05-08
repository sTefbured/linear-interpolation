package linearInterpolation.model.interpolation.event;

import linearInterpolation.model.interpolation.Interpolation;

import java.util.EventObject;

/**
 * <code>InterpolationUpdateEvent</code> is an event which indicates
 * that target <code>Interpolation</code> object has changed.
 *
 * @author Kotikov S.G.
 * @see Interpolation
 */
public class InterpolationUpdateEvent extends EventObject {
    /**
     * Creates an event with defined source object.
     *
     * @param source source of the event.
     */
    public InterpolationUpdateEvent(Interpolation source) {
        super(source);
    }

    /**
     * @return source <code>Interpolation</code> object of the event.
     */
    @Override
    public Interpolation getSource() {
        return (Interpolation) super.getSource();
    }
}
