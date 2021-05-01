package linearInterpolation.model.listener;

import linearInterpolation.model.event.InterpolationUpdateEvent;

import java.util.EventListener;

/**
 * InterpolationUpdateListener is a listener interface for receiving InterpolationUpdateEvent events.
 * The class that is interested in processing an action event
 * implements this interface, and the object created with that
 * class is registered with a component, using the component's
 * addInterpolationUpdateListener method. When the action event
 * occurs, that object's <code>actionPerformed</code> method is
 * invoked.
 */
public interface InterpolationUpdateListener extends EventListener {
    void update(InterpolationUpdateEvent event);
}
