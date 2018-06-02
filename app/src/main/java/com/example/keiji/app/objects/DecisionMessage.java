package com.example.keiji.app.objects;

import java.io.Serializable;

public class DecisionMessage implements Serializable {
    private boolean votedFor;

    public DecisionMessage(boolean votedFor) {
        this.votedFor = votedFor;
    }

    public boolean isVotedFor() {
        return this.votedFor;
    }
}
