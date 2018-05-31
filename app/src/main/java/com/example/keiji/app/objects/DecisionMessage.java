package com.example.keiji.app.objects;

public class DecisionMessage {
    private boolean votedFor;

    public DecisionMessage(boolean votedFor) {
        this.votedFor = votedFor;
    }

    public boolean isVotedFor() {
        return this.votedFor;
    }
}
