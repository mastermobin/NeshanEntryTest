package ir.mobin.test.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.neshan.core.LngLat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.mobin.test.adapter.SearchAdapter;
import ir.mobin.test.adapter.ShortcutAdapter;
import ir.mobin.test.helper.WebServiceHelper;
import ir.mobin.test.model.Result;
import ir.mobin.test.model.Shortcut;
import ir.mobin.test.R;

public class SearchFragment extends Fragment implements ShortcutAdapter.ShortcutListener {

    private EditText etSearch;
    private RecyclerView rvShortcut, rvResult, rvRecent;
    private ImageView ivBack;

    private WebServiceHelper webServiceHelper;

    private long counter = 0;
    private LngLat focus;
    private SearchListener searchListener;
    private SearchAdapter searchAdapter;

    public static SearchFragment newInstance(LngLat pos, SearchListener searchListener) {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        fragment.setFocus(pos);
        fragment.setSearchListener(searchListener);
        return fragment;
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

        webServiceHelper = WebServiceHelper.getInstance();
        searchAdapter = new SearchAdapter();
        RecyclerView.LayoutManager resultLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
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

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
        rvShortcut.setVerticalScrollBarEnabled(false);
        rvShortcut.setHasFixedSize(true);
        rvShortcut.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rvShortcut.setLayoutManager(layoutManager);
        ShortcutAdapter shortcutAdapter = new ShortcutAdapter(data, getContext(), this);
        rvShortcut.setAdapter(shortcutAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
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
        });

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

        return view;
    }

    private void search() {
        webServiceHelper.search(etSearch.getText().toString(), focus, new WebServiceHelper.SearchListener() {
            @Override
            public void onSearchResult(List<Result> results) {
                for (int i = 0; i < results.size(); i++) {
                    Log.d("NESHANTEST", results.get(i).getTitle());
                }
                searchAdapter.setData(results, focus, new SearchAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Result result) {
                        searchListener.onSelect(result.getTitle(), new LngLat(result.getLocation().getX(), result.getLocation().getY()));
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

    public interface SearchListener {
        void onSelect(String title, LngLat pos);
    }
}
