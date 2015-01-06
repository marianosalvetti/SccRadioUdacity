package ar.com.sccradiomobile.livescores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Mariano Salvetti
 *
 * Desde este Adapter manejaremos las 3 pantallas/tabs referidas a los Detalles, Estadisticas y Jugadas
 * de cada partido que es seleccionado.
 */
public class ViewPagerPartidosAdapter extends FragmentStatePagerAdapter {

    private final int PAGES = 3;
    private int partidoId;
    private String local;
    private String visitante;

    public ViewPagerPartidosAdapter(FragmentManager fm, int partidoId, String local, String visitante) {
        super(fm);
       this.partidoId = partidoId;
        this.local = local;
        this.visitante = visitante;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return  DetallePartidoFragment.newInstance(partidoId,local,visitante);
            case 1:
                return  DetalleEstadisticasPartidoFragment.newInstance(partidoId,local,visitante);
            case 2:
                return DetalleJugadasPartidoFragment.newInstance(partidoId,local,visitante);
            default:
                throw new IllegalArgumentException("The item position should be less or equal to:" + PAGES);
        }
    }

    @Override
    public int getCount() {
        return PAGES;
    }
}