package ar.com.sccradiomobile.slidemenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.sponsors.Sponsors;
import ar.com.sccradiomobile.ayuda.AyudaSlideActivity;
import ar.com.sccradiomobile.home.HomePage;
import ar.com.sccradiomobile.livescores.LiveScoresActivity;
import ar.com.sccradiomobile.images.DynamicPhotoListActivity;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.utils.Utils;
import ar.com.sccradiomobile.videos.SCCRadioVideosActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;


public class SlidingMenuFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    public static int checkoutResultcode = 0;
    private ExpandableListView sectionListView;
    private Context context;
    protected SlidingMenu menu = null;

    public SlidingMenu getSlidingMenu() {
        return menu;
    }

    public void setSlidingMenu(SlidingMenu menu) {
        this.menu = menu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity().getApplicationContext();
        List<Section> sectionList = createMenu();

        View view = inflater.inflate(R.layout.slidingmenu_fragment, container, false);
        this.sectionListView = (ExpandableListView) view.findViewById(R.id.slidingmenu_view);
        this.sectionListView.setGroupIndicator(null);

        // NOTICIAS HOME
        ImageView button11 = (ImageView) view.findViewById(R.id.slide_menu_button_home);
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
                Intent mainIntent = new Intent().setClass(context, HomePage.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CATEGORIA, Constants.CATEGORIA_FILAFILO);
                mainIntent.putExtras(bundle);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });


        //acciones para las secciones....
        SectionListAdapter sectionListAdapter = new SectionListAdapter(this.getActivity(), sectionList);
        this.sectionListView.setAdapter(sectionListAdapter);

        this.sectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (Constants.DEBUG) Log.d("sccradio_mobile", "onGroupClick, groupPosition = " + groupPosition);


                return true;
            }
        });

        this.sectionListView.setOnChildClickListener(this);
        this.sectionListView.setDivider(null);
        this.sectionListView.setDividerHeight(0);

        int count = sectionListAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            this.sectionListView.expandGroup(position);
        }

        return view;
    }

    private List<Section> createMenu() {
        List<Section> sectionList = new ArrayList<Section>();

        Section seccionNoticias = new Section(context.getString(R.string.menu_op_1));
        seccionNoticias.addSectionItem(101, context.getString(R.string.menu_op_2));
   //TODO: FINISH THIS SECTION -->   seccionNoticias.addSectionItem(102, "25º ANDRES `PIRU` FILAFILO U-17");
        seccionNoticias.addSectionItem(103, context.getString(R.string.menu_op_3));

        Section seccionMultimedia = new Section(context.getString(R.string.menu_op_4));
        seccionMultimedia.addSectionItem(201, context.getString(R.string.menu_op_5));
        seccionMultimedia.addSectionItem(202, context.getString(R.string.menu_op_6));

        Section seccionGeneral = new Section(context.getString(R.string.menu_op_7));
        seccionGeneral.addSectionItem(301, context.getString(R.string.menu_op_8));
        seccionGeneral.addSectionItem(302, context.getString(R.string.menu_op_9));
        seccionGeneral.addSectionItem(303, context.getString(R.string.menu_op_10));
    //TODO: FINISH THIS SECTION -->     seccionGeneral.addSectionItem(304, context.getString(R.string.menu_op_11));
        seccionGeneral.addSectionItem(305, context.getString(R.string.menu_op_12));

        sectionList.add(seccionNoticias);
        sectionList.add(seccionMultimedia);
        sectionList.add(seccionGeneral);
        return sectionList;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        Intent mainIntent;
        Bundle bundle;
        switch ((int) id) {
            case 101:
                menu.toggle();
                mainIntent = new Intent().setClass(context, HomePage.class);
                bundle = new Bundle();
                bundle.putString(Constants.CATEGORIA, Constants.CATEGORIA_TNA);
                mainIntent.putExtras(bundle);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                break;
            case 102:
                menu.toggle();
                mainIntent = new Intent().setClass(context, HomePage.class);
                bundle = new Bundle();
                bundle.putString(Constants.CATEGORIA, Constants.CATEGORIA_FILAFILO);
                mainIntent.putExtras(bundle);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);

                break;
            case 103:
               menu.toggle();
               mainIntent = new Intent().setClass(context, LiveScoresActivity.class);
               bundle = new Bundle();
               mainIntent.putExtras(bundle);
               mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(mainIntent);

               break;


            case 201:
                menu.toggle();
                if (Utils.isConnected(this.getActivity())) {
                    mainIntent = new Intent().setClass(getActivity(), SCCRadioVideosActivity.class);   //MainVideoActivity es una prueba de concepto de la libreria
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                } else
                    Toast.makeText(this.getActivity(), context.getString(R.string.no_internet_error), Toast.LENGTH_LONG).show();
                break;
            case 202:
                menu.toggle();
                mainIntent = new Intent().setClass(getActivity(), DynamicPhotoListActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                break;

            case 301:
                menu.toggle();
                mainIntent = new Intent().setClass(getActivity(), AyudaSlideActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                break;

            case 302:
                menu.toggle();
                SCCRadioMobileApp.getAppRate().showDialogForced(getActivity());
                break;

            case 303:
                menu.toggle();
                mainIntent = new Intent().setClass(getActivity(), Sponsors.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                break;

            case 304:
                menu.toggle();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage(context.getString(R.string.msg_under_construction));
                  builder.setNeutralButton(R.string.progress_dialog_ok, new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          getActivity().finish();
                      }
                  });

                  builder.setTitle(getString(R.string.progress_dialog_title_msg));
                  builder.create().show();
                break;

            case 305:
                menu.toggle();

                final Intent email = new Intent(android.content.Intent.ACTION_SENDTO);
                String uriText = null;
                try {
                    uriText = "mailto:" + getString(R.string.contact_email)
                            + "?subject=" + getString(R.string.contact_email_subject);
                } catch (Exception error1) {
                    Toast.makeText(this.getActivity(), R.string.toast_unsupported_encoding, Toast.LENGTH_SHORT).show();
                    break;
                }

                email.setData(Uri.parse(uriText));
                try {
                    startActivity(email);
                } catch (Exception error2) {
                    Toast.makeText(this.getActivity(), R.string.toast_contact_no_email, Toast.LENGTH_SHORT).show();
                }

                break;

        }

        return false;
    }


    public static void showAlert(String object, String ok, final Runnable okRunnable, String cancel, final Runnable cancelRunnable, final Activity ctx) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (okRunnable != null)
                    okRunnable.run();
                if (checkoutResultcode == -1)
                    ctx.finish();

            }
        });
        if (cancelRunnable != null) {
            alertDialog.setNegativeButton(cancel, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelRunnable.run();
                }
            });
        }
        alertDialog.setMessage(object);
        if (!ctx.isFinishing()) {
            ctx.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    alertDialog.show();
                }
            });
        }
    }

    public void showAlert(String object, String ok, final Runnable okRunnable, String cancel, final Runnable cancelRunnable) {
        showAlert(object, ok, okRunnable, cancel, cancelRunnable, getActivity());
    }


}