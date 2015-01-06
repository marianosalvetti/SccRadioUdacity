package ar.com.sccradiomobile.ayuda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.utils.Utils;
import com.actionbarsherlock.app.SherlockFragment;

import java.io.InputStream;

public class AyudaSlidePageFragment extends SherlockFragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static AyudaSlidePageFragment create(int pageNumber) {
        AyudaSlidePageFragment fragment = new AyudaSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public AyudaSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.ayuda_fragment_screen_slide_page, container, false);

        TextView textViewTitle = (TextView) rootView.findViewById(android.R.id.text1);
        TextView textView = (TextView) rootView.findViewById(android.R.id.text2);
        ImageView imagen = (ImageView) rootView.findViewById(R.id.home_imagen);

        InputStream iFile = null;
        String strFile;

        switch (mPageNumber) {
            case 0:
                iFile = getResources().openRawResource(R.raw.ayuda_1);
                strFile = Utils.inputStreamToString(iFile);
                textViewTitle.setText("Titulo 1");
                imagen.setImageResource(R.drawable.help_1);
                break;

            case 1:
                iFile = getResources().openRawResource(R.raw.ayuda_2);
                strFile = Utils.inputStreamToString(iFile);
                textViewTitle.setText("Titulo 2");
                imagen.setImageResource(R.drawable.help_2);
                break;

            case 2:
                iFile = getResources().openRawResource(R.raw.ayuda_3);
                strFile = Utils.inputStreamToString(iFile);
                textViewTitle.setText("Titulo 3");
                imagen.setImageResource(R.drawable.help_3);
                break;

            case 3:
                iFile = getResources().openRawResource(R.raw.ayuda_4);
                strFile = Utils.inputStreamToString(iFile);
                textViewTitle.setText("Titulo 4");
                imagen.setImageResource(R.drawable.help_1);
                break;

            case 4:
                iFile = getResources().openRawResource(R.raw.ayuda_5);

                strFile = Utils.inputStreamToString(iFile);
                textViewTitle.setText("Contacto");
                imagen.setImageResource(R.drawable.help_2);
                break;

            default:
                iFile = getResources().openRawResource(R.raw.ayuda_1);
                strFile = Utils.inputStreamToString(iFile);
                textViewTitle.setText("Sponsors de la Liga Nacional");
                imagen.setImageResource(R.drawable.help_3);
                break;
        }

        textView.setText(strFile);

        return rootView;
    }


    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
