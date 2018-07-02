package be.cerclepolytechnique.applicationcp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    View myView;
    private ListView lv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_scrolling, container, false);
        lv = (ListView) getActivity().findViewById(R.id.list_itemnf);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");
        your_array_list.add("foo");
        your_array_list.add("bar");



        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout., your_array_list );

        lv.setAdapter(arrayAdapter);
        return myView;
}
}
