package org.velas.manhunt.menusystem;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private final Player owner;

    private String selectedTeam;

    private String selectionTeamOption;

    public String getSelectionTeamOption() {
        return selectionTeamOption;
    }

    public void setSelectionTeamOption(String selectionTeamOption) {
        this.selectionTeamOption = selectionTeamOption;
    }

    public PlayerMenuUtility(Player owner){
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }
}
