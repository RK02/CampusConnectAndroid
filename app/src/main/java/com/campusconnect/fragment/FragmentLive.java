package com.campusconnect.fragment;

/**
 * Created by RK on 17-09-2015.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.campusconnect.supportClasses.Live_infoActivity;
import com.campusconnect.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp1 on 21-01-2015.
 */
public class FragmentLive extends Fragment {

    RecyclerView live;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_live_to_be_deleted,container,false);
/*
        live = (RecyclerView) v.findViewById(R.id.rv_live);
        live.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        live.setLayoutManager(llm);
        live.setItemAnimator(new DefaultItemAnimator());
        LiveAdapterActivity l = new LiveAdapterActivity(
                createList_l(2));
        live.setAdapter(l); */

        return v;
    }

    private List<Live_infoActivity> createList_l(int size) {
        List<Live_infoActivity> result = new ArrayList<Live_infoActivity>();
        for (int i = 1; i <= size; i++) {
            Live_infoActivity ci = new Live_infoActivity();

            result.add(ci);
        }
        return result;
    }

}
