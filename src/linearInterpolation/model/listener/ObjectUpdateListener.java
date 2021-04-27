package linearInterpolation.model.listener;

import linearInterpolation.model.event.ObjectUpdateEvent;

import java.util.EventListener;

public interface ObjectUpdateListener extends EventListener {
    void update(ObjectUpdateEvent event);
}
