package com.aht.business.kirti.pasitupusi.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<PageData> mText = Transformations.map(mIndex, new Function<Integer, PageData>() {
        @Override
        public PageData apply(Integer input) {
            PageData pageData = new PageData();
            pageData.setId(SectionsPagerAdapter.TAB_TITLES[input-1]);
            pageData.setText("Hello world from section: " + input);
            return pageData;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<PageData> getText() {
        return mText;
    }


}