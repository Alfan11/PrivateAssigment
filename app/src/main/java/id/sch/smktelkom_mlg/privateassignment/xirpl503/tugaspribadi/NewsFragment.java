package id.sch.smktelkom_mlg.privateassignment.xirpl503.tugaspribadi;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class NewsFragment extends Fragment {
    private static final String URL_DATA = "https://api.nytimes.com/svc/news/v3/content/all/all.json?api-key=02e2cdc337bb40fa8f13cd78622eeaa5";
    List<ListItem> listItems;
    private RecyclerView recyclerViewNews;
    private RecyclerView.Adapter adapter;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerViewNews = (RecyclerView) view.findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setHasFixedSize(true);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();
        loadRecyclerViewData();
        return view;
    }

    private void loadRecyclerViewData() {
        final ProgressDialog progessDialog = new ProgressDialog(getActivity());
        progessDialog.setMessage("Memuat Berita...");
        progessDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String n) {
                        progessDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(n);
                            JSONArray array = jsonObject.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                ListItem item = new ListItem(
                                        o.getString("title"),
                                        o.getString("abstract"),
                                        o.getString("thumbnail_standard")
                                );
                                listItems.add(item);
                            }
                            adapter = new MainAdapter(listItems, getActivity().getApplicationContext());
                            recyclerViewNews.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progessDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
