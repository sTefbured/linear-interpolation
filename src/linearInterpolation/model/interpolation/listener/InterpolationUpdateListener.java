package linearInterpolation.model.interpolation.listener;

import linearInterpolation.model.interpolation.event.InterpolationUpdateEvent;

import java.util.EventListener;

/**
 * <code>InterpolationUpdateListener</code> is a listener interface for
 * receiving <code>InterpolationUpdateEvent</code> events. The class that
 * is interested in processing an <code>InterpolationEvent</code> implements
 * this interface, and the object created with that class is registered using
 * the <code>addInterpolationUpdateListener</code> method. When the event
 * occurs, that object's <code>interpolationUpdate</code> method is invoked.
 *
 * @author Kotikov S.K.
 */
public interface InterpolationUpdateListener extends EventListener {
    void interpolationUpdated(InterpolationUpdateEvent event);
}
