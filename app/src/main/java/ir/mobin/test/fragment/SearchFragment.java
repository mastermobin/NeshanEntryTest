package ir.mobin.test.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.neshan.core.LngLat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.mobin.test.adapter.RecentSearchAdapter;
import ir.mobin.test.adapter.SearchAdapter;
import ir.mobin.test.adapter.ShortcutAdapter;
import ir.mobin.test.helper.SearchDatabase;
import ir.mobin.test.helper.WebServiceHelper;
import ir.mobin.test.model.Place;
import ir.mobin.test.model.RecentSearch;
import ir.mobin.test.model.Result;
import ir.mobin.test.model.Shortcut;
import ir.mobin.test.R;

public class SearchFragment extends Fragment implements ShortcutAdapter.ShortcutListener, TextWatcher, SearchAdapter.PlaceListener {

    private EditText etSearch;
    private TextView tvSimulate;
    private RecyclerView rvShortcut, rvResult, rvRecent;
    private ImageView ivBack;
    private SearchDatabase searchDatabase;
    private SearchActions searchActions;

    private WebServiceHelper webServiceHelper;

    private long counter = 0;
    private LngLat focus;
    private SearchListener searchListener;
    private SearchAdapter searchAdapter;
    private RecentSearchAdapter recentSearchAdapter;

    public static SearchFragment newInstance(LngLat pos, SearchListener searchListener, SearchActions searchActions) {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        fragment.setFocus(pos);
        fragment.setSearchActions(searchActions);
        fragment.setSearchListener(searchListener);
        return fragment;
    }

    public void setSearchActions(SearchActions searchActions) {
        this.searchActions = searchActions;
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public void setFocus(LngLat focus) {
        this.focus = focus;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rvShortcut = view.findViewById(R.id.rvShortcut);
        rvRecent = view.findViewById(R.id.rvRecent);
        rvResult = view.findViewById(R.id.rvResult);
        etSearch = view.findViewById(R.id.etSearch);
        ivBack = view.findViewById(R.id.ivBack);
        tvSimulate = view.findViewById(R.id.tvSimulate);

        webServiceHelper = WebServiceHelper.getInstance();
        searchDatabase = SearchDatabase.getInstance(getContext());
        recentSearchAdapter = new RecentSearchAdapter(this);
        rvRecent.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvRecent.setAdapter(recentSearchAdapter);

        new GetRecentSearches().execute((Void) null);

        searchAdapter = new SearchAdapter();
        RecyclerView.LayoutManager resultLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvResult.setLayoutManager(resultLayoutManager);
        rvResult.setAdapter(searchAdapter);

        List<Shortcut> data = new ArrayList<>();
        data.add(new Shortcut("پمپ بنزین", R.drawable.ic_gas_station, R.color.colorGas));
        data.add(new Shortcut("پمپ گاز", R.drawable.ic_cng_station, R.color.colorCNG));
        data.add(new Shortcut("سرویس بهداشتی", R.drawable.ic_wc, R.color.colorWC));
        data.add(new Shortcut("پایگاه امداد", R.drawable.ic_helal_ahmar, R.color.colorEmergency));
        data.add(new Shortcut("پارکینگ", R.drawable.ic_parking, R.color.colorParking));
        data.add(new Shortcut("مسجد", R.drawable.ic_mosque, R.color.colorMosque));
        data.add(new Shortcut("رستوران", R.drawable.ic_restaurant, R.color.colorRestaurant));
        data.add(new Shortcut("خودپرداز", R.drawable.ic_atm, R.color.colorATM));
        data.add(new Shortcut("فست فود", R.drawable.ic_fastfood, R.color.colorFastFood));
        data.add(new Shortcut("مرکز خرید", R.drawable.ic_store, R.color.colorStore));
        data.add(new Shortcut("داروخانه", R.drawable.ic_pharmacy, R.color.colorPharmacy));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false);
        rvShortcut.setVerticalScrollBarEnabled(false);
        rvShortcut.setHasFixedSize(true);
        rvShortcut.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rvShortcut.setLayoutManager(layoutManager);
        ShortcutAdapter shortcutAdapter = new ShortcutAdapter(data, getContext(), this);
        rvShortcut.setAdapter(shortcutAdapter);

        etSearch.addTextChangedListener(this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvResult.getVisibility() == View.VISIBLE) {
                    etSearch.setText("");
                } else {
                    getActivity().onBackPressed();
                }
            }
        });

        tvSimulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchActions.onSimulate();
            }
        });

        return view;
    }

    private void search() {
        webServiceHelper.search(etSearch.getText().toString(), focus, new WebServiceHelper.SearchListener() {
            @Override
            public void onSearchResult(List<Result> results) {
                searchAdapter.setData(results, focus, new SearchAdapter.PlaceListener() {
                    @Override
                    public void onPlaceSelection(Place place) {
                        new InsertRecentSearches().execute(new RecentSearch(place.getTitle(), place.getAddress(), place.getLocation(), new Date()));
                        searchListener.onSelect(place.getTitle(), place.getLocation());
                    }
                });
            }

            @Override
            public void onSearchFailed(Throwable throwable) {
                Log.d("NESHANTEST", throwable.getMessage());
            }
        });
    }

    @Override
    public void onShortcutClick(final String title) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (etSearch.getText().toString().isEmpty()) {
                            etSearch.setText(title);
                            etSearch.setSelection(etSearch.getText().length());
                        }
                    }
                });
            }
        }, 500);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(final Editable editable) {
        counter++;
        final long co = counter;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (counter == co) {
                    search();
                }
            }
        }, 1500);

        if (editable.toString().length() == 0) {
            rvResult.setVisibility(View.GONE);
            ivBack.setImageResource(R.drawable.ic_back_circle);
            searchAdapter.clear();
        } else {
            rvResult.setVisibility(View.VISIBLE);
            ivBack.setImageResource(R.drawable.ic_clear_circle);

        }
    }

    @Override
    public void onPlaceSelection(Place result) {
        searchListener.onSelect(result.getTitle(), result.getLocation());
    }

    public interface SearchListener {
        void onSelect(String title, LngLat pos);
    }

    private class GetRecentSearches extends AsyncTask<Void, Void,List<RecentSearch>>
    {
        @Override
        protected List<RecentSearch> doInBackground(Void... voids) {
            return searchDatabase.recentSearchDao().getRecent();
        }

        @Override
        protected void onPostExecute(List<RecentSearch> recentSearches) {
            super.onPostExecute(recentSearches);
            recentSearchAdapter.setData(recentSearches);
        }
    }

    private class InsertRecentSearches extends AsyncTask<RecentSearch, Void,Void>
    {
        @Override
        protected Void doInBackground(RecentSearch... recentSearches) {
            searchDatabase.recentSearchDao().insert(recentSearches[0]);
            return null;
        }
    }

    public interface SearchActions{
        void onSimulate();
    }
}
