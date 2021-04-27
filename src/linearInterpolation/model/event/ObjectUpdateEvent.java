package linearInterpolation.model.event;

import java.util.EventObject;

public class ObjectUpdateEvent extends EventObject {
    public ObjectUpdateEvent(Object source) {
        super(source);
    }
}
