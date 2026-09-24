package org.velas.manhunt.game.timeline.event;

public record EventHandlerBundle(
        BaseDropSummonEvent dropSummonEvent,
        BaseManhuntGameStartEvent gameStartEvent,
        BaseWorldGenerationEvent worldGenerationEvent,
        BaseWorldPreloadEvent worldPreloadEvent,
        BaseMatchStartBroadcastEvent matchStartBroadcastEvent,
        BaseMatchStartEvent matchStartEvent,
        BaseMatchHeadStartEvent matchHeadStartEvent,
        BaseMatchPlayingPhaseEvent matchPlayingPhaseEvent,
        BaseMiscellaneousEvent miscellaneousEvent
) {
}
