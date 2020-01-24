package com.aht.business.kirti.pasitupusi.model.updates.data;

import com.aht.business.kirti.pasitupusi.model.updates.enums.UpdateType;

public class VersionDetail {

    int currentVersion;

    int minimumVersion;

    UpdateType updateType;

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }

    public int getMinimumVersion() {
        return minimumVersion;
    }

    public void setMinimumVersion(int minimumVersion) {
        this.minimumVersion = minimumVersion;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }
}
