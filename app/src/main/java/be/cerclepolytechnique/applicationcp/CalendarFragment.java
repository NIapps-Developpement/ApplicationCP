package be.cerclepolytechnique.applicationcp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CalendarFragment extends Fragment {

    View myView;
    ImageButton button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.calendar_layout, container, false);
        ImageButton button = getActivity().findViewById(R.id.send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(getActivity(), CalendarLogin.class);
                getActivity().startActivity(mainIntent);
            }
        });
        return myView;
    }
}