package org.velas.manhunt.avatar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.velas.manhunt.player.ManhuntPlayer;

public enum AvatarType {
    ASSASSIN {
        @Override
        public @NotNull Avatar createNewAvatar(@NotNull ManhuntPlayer manhuntPlayer) {
            return new Assassin(manhuntPlayer);
        }
    },
    SWORDSMAN {
        @Override
        public @NotNull Avatar createNewAvatar(@NotNull ManhuntPlayer manhuntPlayer) {
            return new Swordsman(manhuntPlayer);
        }
    },
    DEFAULT {
        @Override
        public Avatar createNewAvatar(@NotNull ManhuntPlayer manhuntPlayer) {
            return null;
        }
    };

    public abstract @Nullable Avatar createNewAvatar(@NotNull ManhuntPlayer manhuntPlayer);
}
