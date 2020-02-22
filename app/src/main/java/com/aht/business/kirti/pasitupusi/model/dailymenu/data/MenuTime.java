package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

public class MenuTime {

    private boolean breakfast;

    private boolean lunch;

    private boolean dinner;

    public MenuTime() {
        this(false, false, false);
    }

    public MenuTime(boolean breakfast, boolean lunch, boolean dinner) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public void setBreakfast(boolean breakfast) {
        this.breakfast = breakfast;
    }

    public boolean isLunch() {
        return lunch;
    }

    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }

    public boolean isDinner() {
        return dinner;
    }

    public void setDinner(boolean dinner) {
        this.dinner = dinner;
    }
}
