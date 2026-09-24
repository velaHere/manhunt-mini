package org.velas.manhunt.game.timeline;

import org.jetbrains.annotations.NotNull;
import org.velas.manhunt.game.ManhuntManager;
import org.velas.manhunt.game.timeline.event.EventHandlerBundle;
import org.velas.manhunt.game.timeline.event.TimelineEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TimelineEventsRegistry {

    private final @NotNull ManhuntManager gameManager;
    private final List<TimelineEvent> events = new ArrayList<>();

    TimelineEventsRegistry(@NotNull ManhuntManager gameManager){
        this.gameManager=gameManager;
    }

    private void registerAll(TimelineEvent... events){
        this.events.addAll(Arrays.asList(events));
        this.events.sort(Comparator.comparingInt(TimelineEvent::getTriggerTime));
    }

    public TimelineEventsRegistry registerEvents(EventHandlerBundle bundle){
        this.registerAll(
                bundle.dropSummonEvent(),
                bundle.gameStartEvent(),
                bundle.worldGenerationEvent(),
                bundle.worldPreloadEvent(),
                bundle.matchStartBroadcastEvent(),
                bundle.matchStartEvent(),
                bundle.matchHeadStartEvent(),
                bundle.matchPlayingPhaseEvent(),
                bundle.miscellaneousEvent()
        );
        return this;
    }

    public void processTick(int currentTime){
        for (TimelineEvent event : events)
            if (event.shouldTrigger(currentTime)) event.execute();
        this.gameManager.getScoreboardManager().updateSideBar(currentTime);
    }
}
