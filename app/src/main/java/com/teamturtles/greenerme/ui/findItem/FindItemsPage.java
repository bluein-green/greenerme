package com.teamturtles.greenerme.ui.findItem;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.adapters.SearchViewBindingAdapter;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.teamturtles.greenerme.HighlightRenderer;
import com.teamturtles.greenerme.HighlightedResult;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.io.SearchResultsJsonParser;
import com.teamturtles.greenerme.model.Item;
import com.teamturtles.greenerme.ui.findItem.CategoriesPage;

import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

public class FindItemsPage extends AppCompatActivity implements AbsListView.OnScrollListener {
    // BL:
    private Client client;
    private Index index;
    private Query query;
    private SearchResultsJsonParser resultsParser = new SearchResultsJsonParser();
    private int lastSearchedSeqNo;
    private int lastDisplayedSeqNo;
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;

    private List<HighlightedResult<Item>> results;

    // UI views
    private Button category_btn;
    private ListView items_list;
    private ItemAdapter itemsList_adapter;
    private SearchView searchView;
    private HighlightRenderer highlightRenderer;

    // Constants

    private static final int HITS_PER_PAGE = 20;
    /**
     * Number of items before the end of the list past which we start loading more content.
     */
    private static final int LOAD_MORE_THRESHOLD = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_items_page);

        // set view refs
        setViewRefs();

        // bind search results UI components
        items_list.setAdapter(itemsList_adapter = new ItemAdapter(this, R.layout.cell_item_result));
        items_list.setOnScrollListener(this);

        // init algolia
        // TODO: pull info from database, here we hardcode the data
        client = new Client("JGKTHVHF1X", "4894d0b97f60eaae8c82c3986a2bebb7");
        index = client.getIndex("Items_original");

        // Pre-build query.
        query = new Query();
        query.setAttributesToRetrieve("name", "category");
        query.setAttributesToHighlight("name");
        query.setHitsPerPage(HITS_PER_PAGE);

        configureSearchView();

        highlightRenderer = new HighlightRenderer(this);


        // click listeners for categories button
        category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryPage();
            }
        });
    }

    private void openCategoryPage() {
        Intent intent = new Intent(this, CategoriesPage.class);
        startActivity(intent);
    }

    private void setViewRefs() {
        category_btn = (Button) findViewById(R.id.FI_categories_btn);
        items_list = (ListView) findViewById(R.id.FI_listView);
    }

    private void configureSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.FI_searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Nothing to do: the search has already been performed by `onQueryTextChange()`.
                // We do try to close the keyboard, though.
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    System.out.println("not going to search");
                    itemsList_adapter.clear();
                    if (items_list.getVisibility() == View.VISIBLE) {
                        items_list.setVisibility(View.INVISIBLE);
                    }
                    return true;
                }

                if (items_list.getVisibility() == View.INVISIBLE) {
                    items_list.setVisibility(View.VISIBLE);
                }

                search();
                System.out.println("searching la");
                return true;
            }
        });
    }

    private void setListClickListeners() {
        items_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                System.out.println("id = " + id);
                Intent intent = new Intent(FindItemsPage.this, ItemDetailsPage.class);
                intent.putExtra("ITEM_ID", (int) id);
                startActivity(intent);
            }
        });
    }

    // Actions

    private void search() {
        final int currentSearchSeqNo = ++lastSearchedSeqNo;
        query.setQuery(searchView.getQuery().toString());
        lastRequestedPage = 0;
        lastDisplayedPage = -1;
        endReached = false;
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                if (content != null && error == null) {
                    // NOTE: Check that the received results are newer that the last displayed results.
                    //
                    // Rationale: Although TCP imposes a server to send responses in the same order as
                    // requests, nothing prevents the system from opening multiple connections to the
                    // same server, nor the Algolia client to transparently switch to another server
                    // between two requests. Therefore the order of responses is not guaranteed.
                    if (currentSearchSeqNo <= lastDisplayedSeqNo) {
                        return;
                    }


                    results = resultsParser.parseResults(content);
                    if (results.isEmpty()) {
                        endReached = true;
                        System.out.println("no results...");
                    } else {
                        itemsList_adapter.clear();
                        itemsList_adapter.addAll(results);
                        itemsList_adapter.notifyDataSetChanged();
                        setListClickListeners();
                        lastDisplayedSeqNo = currentSearchSeqNo;
                        lastDisplayedPage = 0;
                        System.out.println("we have results");
                    }

                    // Scroll the list back to the top.
                    items_list.smoothScrollToPosition(0);
                }
            }
        });
    }

    private void loadMore() {
        Query loadMoreQuery = new Query(query);
        loadMoreQuery.setPage(++lastRequestedPage);
        final int currentSearchSeqNo = lastSearchedSeqNo;
        index.searchAsync(loadMoreQuery, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                if (content != null && error == null) {
                    // Ignore results if they are for an older query.
                    if (lastDisplayedSeqNo != currentSearchSeqNo) {
                        return;
                    }

                    List<HighlightedResult<Item>> results = resultsParser.parseResults(content);
                    if (results.isEmpty()) {
                        endReached = true;
                    } else {
                        itemsList_adapter.addAll(results);
                        itemsList_adapter.notifyDataSetChanged();
                        lastDisplayedPage = lastRequestedPage;
                    }
                }
            }
        });
    }


    // data sources
    private class ItemAdapter extends ArrayAdapter<HighlightedResult<Item>> {
        public ItemAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup cell = (ViewGroup) convertView;
            if (cell == null) {
                cell = (ViewGroup) getLayoutInflater().inflate(R.layout.cell_item_result, null);
            }

            TextView itemNameTextView = (TextView) cell.findViewById(R.id.item_name);
            TextView catNameTextView = (TextView) cell.findViewById(R.id.category_name);

            HighlightedResult<Item> result = itemsList_adapter.getItem(position);

            // imageLoader.displayImage(result.getResult().getImage(), posterImageView, displayImageOptions);
            itemNameTextView.setText(highlightRenderer.renderHighlights(result.getHighlight("name").getHighlightedValue()));
            catNameTextView.setText(result.getResult().getCategory());

            return cell;
        }

        @Override
        public void addAll(Collection<? extends HighlightedResult<Item>> items) {
            for (HighlightedResult<Item> item : items) {
                add(item);
            }
        }

        @Override
        public long getItemId(int position) {
            return results.get(position).getResult().getId();
        }
    }


    // AbsListView.OnScrollListener

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Nothing to do.
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // Abort if list is empty or the end has already been reached.
        if (totalItemCount == 0 || endReached) {
            return;
        }

        // Ignore if a new page has already been requested.
        if (lastRequestedPage > lastDisplayedPage) {
            return;
        }

        // Load more if we are sufficiently close to the end of the list.
        int firstInvisibleItem = firstVisibleItem + visibleItemCount;
        if (firstInvisibleItem + LOAD_MORE_THRESHOLD >= totalItemCount) {
            loadMore();
        }
    }


}
