package com.bitdubai.fermat_pip_api.all_definition.event_manager.events;

import com.bitdubai.fermat_pip_api.all_definition.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.agent.timeout_notifier.interfaces.TimeOutAgent;

/**
 * Created by rodrigo on 4/4/16.
 */
public class TimeOutReachedEvent extends AbstractFermatPipEvent {
    private TimeOutAgent timeOutAgent;

    public TimeOutReachedEvent() {
        super(EventType.TIMEOUT_REACHED);
    }

    public TimeOutAgent getTimeOutAgent() {
        return timeOutAgent;
    }

    public void setTimeOutAgent(TimeOutAgent timeOutAgent) {
        this.timeOutAgent = timeOutAgent;
    }
}
