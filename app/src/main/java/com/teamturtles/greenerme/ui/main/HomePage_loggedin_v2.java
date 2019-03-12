package com.teamturtles.greenerme.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teamturtles.greenerme.search.HighlightRenderer;
import com.teamturtles.greenerme.search.HighlightedResult;
import com.teamturtles.greenerme.R;
import com.teamturtles.greenerme.io.SearchResultsJsonParser;
import com.teamturtles.greenerme.model.Item;
import com.teamturtles.greenerme.ui.account.ViewAccountPage;
import com.teamturtles.greenerme.ui.findItem.CategoriesPage;
import com.teamturtles.greenerme.ui.findItem.ItemDetailsPage;
import com.teamturtles.greenerme.ui.points.CheckPointsPage;
import com.teamturtles.greenerme.ui.points.LeaderboardPage;
import com.teamturtles.greenerme.ui.quiz.TakeQuizPage;

import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

public class HomePage_loggedin_v2 extends AppCompatActivity implements AbsListView.OnScrollListener, View.OnClickListener {
    // algolia info
    private final String ALGOLIA_APP_ID = "XXXXXX";
    private final String ALGOLIA_API_KEY = "XXXXXX";
    private final String INDEX_NAME = "items";

    // buttons
    private ImageButton item_btn;
    private TextView item_txt;
    private ImageButton quiz_btn;
    private TextView quiz_txt;
    private ImageButton points_btn;
    private TextView points_txt;
    private ImageButton acct_btn;
    private TextView acct_txt;
    private ImageButton about_btn;
    private TextView about_txt;
    private Group group_of_icons;
    private ImageView algolia_logo;
    private View rectangle;
    private Group group_loggedIn;

    // user authentication
    private TextView textView_greeting;
    private TextView textView_msg;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String username;
    private boolean isLoggedIn;

    // dialog for verification
    private AlertDialog.Builder mBuilder;
    private View mView;
    private AlertDialog dialog;

    // verification
    private Button resendEmail_btn;
    private Button logoutEmail_btn;
    private Button verified_btn;
    private Button homepagelogout_btn;

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

    // search results and adapters
    private ListView items_list;
    private ItemAdapter itemsList_adapter;
    private SearchView searchView;
    private HighlightRenderer highlightRenderer;
    private List<HighlightedResult<Item>> results;

    // Constants
    private static final int HITS_PER_PAGE = 20;
    /**
     * Number of items before the end of the list past which we start loading more content.
     */
    private static final int LOAD_MORE_THRESHOLD = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_loggedin_v2);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setViewRefs();
        setClickListeners();

        if (user != null) {
            loadUserInformation();
            isLoggedIn = false;
        } else {
            isLoggedIn = true;
            String greeting_guest = getString(R.string.hi_greeting, "Guest");
            textView_greeting.setText(greeting_guest);
            textView_greeting.setVisibility(View.VISIBLE);

            String not_registered = "Login / Register to Enjoy More Features!";
            textView_msg.setText(not_registered);

            homepagelogout_btn.setVisibility(View.VISIBLE);
            rectangle.setVisibility(View.VISIBLE);
        }

        // ----- SEARCH
        // bind search results UI components
        items_list.setAdapter(itemsList_adapter = new ItemAdapter(this, R.layout.cell_item_result));
        items_list.setOnScrollListener(this);

        // init algolia
        // TODO: pull info from database, here we hardcode the data
        // TODO: somehow not possible to use a final variable as arguments
        client = new Client(ALGOLIA_APP_ID, ALGOLIA_API_KEY);
        index = client.getIndex("items");

        // Pre-build query.
        query = new Query();
        query.setAttributesToRetrieve("name", "category");
        query.setAttributesToHighlight("name");
        query.setHitsPerPage(HITS_PER_PAGE);

        configureSearchView();

        highlightRenderer = new HighlightRenderer(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (user != null) {
            isLoggedIn = true;
            loadUserInformation();
            group_loggedIn.setVisibility(View.GONE);
            // rectangle.setVisibility(View.GONE);
            // homepagelogout_btn.setVisibility(View.GONE);
        } else {
            isLoggedIn = false;
            String greeting_guest = getString(R.string.hi_greeting, "Guest");
            textView_greeting.setText(greeting_guest);
            textView_greeting.setVisibility(View.VISIBLE);

            String not_registered = "Login to Enjoy More Features!";
            textView_msg.setText(not_registered);

            // homepagelogout_btn.setVisibility(View.VISIBLE);
            group_loggedIn.setVisibility(View.VISIBLE);
        }

        searchView.setQuery("", false);
        searchView.clearFocus();

        if (group_of_icons.getVisibility() != View.VISIBLE) {
            group_of_icons.setVisibility(View.VISIBLE);
            algolia_logo.setVisibility(View.GONE);

            if (!isLoggedIn) {
                group_loggedIn.setVisibility(View.VISIBLE);
            }
        }
    }


    private void setViewRefs() {
        // SEARCH
        items_list = (ListView) findViewById(R.id.FI_listView);

        // VIEW ALL ITEMS
        item_btn = (ImageButton) findViewById(R.id.search_button);
        item_txt = (TextView) findViewById(R.id.find_item_text);

        // QUIZ
        quiz_btn = (ImageButton) findViewById(R.id.quiz_button);
        quiz_txt = (TextView) findViewById(R.id.take_quiz_text);

        // POINTS
        points_btn = (ImageButton) findViewById(R.id.points_button);
        points_txt = (TextView) findViewById(R.id.check_points_text);

        // ACCOUNT SETTINGS
        acct_btn = (ImageButton) findViewById(R.id.settings_button);
        acct_txt = (TextView) findViewById(R.id.view_account_text);

        // ABOUT APP
        about_btn = (ImageButton) findViewById(R.id.about_button);
        about_txt = (TextView) findViewById(R.id.about_app_text);

        // ICON GROUP
        group_of_icons = (Group) findViewById(R.id.group_icons);

        // ALGOLIA LOGO
        algolia_logo = (ImageView) findViewById(R.id.home_algolia_logo);

        // HI_NAME && WELCOME MESSAGE
        textView_greeting = (TextView) findViewById(R.id.Hi_name);
        textView_greeting.setVisibility(View.INVISIBLE);
        textView_msg = (TextView) findViewById(R.id.welcome_message);

        // HOMEPAGE LOGGED OUT BUTTON
        homepagelogout_btn = (Button) findViewById(R.id.homepagelogout_btn);
        rectangle = (View) findViewById(R.id.home_covered_bar);

        // LOGGED OUT GROUP
        group_loggedIn = (Group) findViewById(R.id.group_notLoggedIn);
    }


    private void loadUserInformation() { // added
        username = user.getDisplayName();
        String greeting_result = getString(R.string.hi_greeting, username);
        textView_greeting.setText(greeting_result);
        textView_greeting.setVisibility(View.VISIBLE);

        if (!user.isEmailVerified() && dialog == null) {
            emailVerifiedPop();
        } else if (!user.isEmailVerified() && dialog != null) {
            dialog.show();
        }
    }

    private void emailVerifiedPop() {
        System.out.println("POP UP!");
        mBuilder = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.dialog_email_not_verified_pop, null);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        resendEmail_btn = (Button) mView.findViewById(R.id.resendEmail_btn);
        logoutEmail_btn = (Button) mView.findViewById(R.id.logoutEmail_btn);
        verified_btn = (Button) mView.findViewById(R.id.verified_btn);

        resendEmail_btn.setOnClickListener(this);
        logoutEmail_btn.setOnClickListener(this);
        verified_btn.setOnClickListener(this);

    }

    private void resendEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successfully Resend Email", Toast.LENGTH_SHORT).show();
                } else { // no idea what may this case be!!!
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    //restart this activity
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                }
            }
        });
    }


    // search related (from findItemsPage)
    private void configureSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.FI_searchView);
        setSearchFocusListener();
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
                if (group_of_icons.getVisibility() == View.VISIBLE) {
                    group_of_icons.setVisibility(View.GONE);
                    algolia_logo.setVisibility(View.VISIBLE);


                    if (!isLoggedIn) {
                        group_loggedIn.setVisibility(View.GONE);
                    }

                }

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
                return true;
            }
        });
    }

    private void setListClickListeners() {
        items_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                System.out.println("id = " + id);
                Intent intent = new Intent(HomePage_loggedin_v2.this, ItemDetailsPage.class);
                intent.putExtra("ITEM_ID", (int) id);
                startActivity(intent);
                itemsList_adapter.clear();

            }
        });
    }


    // TODO: focus listener doesn't work
    private void setSearchFocusListener() {
        /*
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                System.out.println("clicked");
                if (hasFocus) {
                    System.out.println("is focused");
                } else {
                    searchView.clearFocus();
                }
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICKED");
                if (group_of_icons.getVisibility() == View.VISIBLE) {
                    group_of_icons.setVisibility(View.GONE);

                }
            }
        });
        */
    }


    @Override
    public void onBackPressed() {
        if (group_of_icons.getVisibility() == View.GONE) {
            searchView.clearFocus();
            group_of_icons.setVisibility(View.VISIBLE);
            algolia_logo.setVisibility(View.GONE);
            System.out.println("CLEARING STUFF");
            itemsList_adapter.clear();
            searchView.setQuery("", false);

            if (!isLoggedIn && group_of_icons.getVisibility() == View.VISIBLE) {
                group_loggedIn.setVisibility(View.VISIBLE);
                System.out.println("SHOWING NOW");
            }
        } else {
            super.onBackPressed();
        }

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
                        itemsList_adapter.clear();
                        itemsList_adapter.notifyDataSetChanged();
                    } else {
                        itemsList_adapter.clear();
                        itemsList_adapter.addAll(results);
                        itemsList_adapter.notifyDataSetChanged();
                        setListClickListeners();
                        lastDisplayedSeqNo = currentSearchSeqNo;
                        lastDisplayedPage = 0;
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

    @Override
    public void onClick(View view) {

        if (view == item_btn || view == item_txt){ // SEARCH
            startActivity(new Intent(this, CategoriesPage.class));
        }
        if (view == quiz_btn || view == quiz_txt){ // QUIZ
            if (user == null) {
                Toast.makeText(getApplicationContext(), "Please login to use the feature!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, TakeQuizPage.class));
            }
        }
        if (view == points_btn || view == points_txt){ // POINTS
            if (user == null) {
                Toast.makeText(getApplicationContext(), "Please login to use the feature!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, CheckPointsPage.class));
            }
        }
        if (view == acct_btn || view == acct_txt) { // ACCOUNT SETTINGS
            if (user == null) {
                Toast.makeText(getApplicationContext(), "Please login to use the features!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, ViewAccountPage.class));
            }
        }
        if (view == about_btn || view == about_txt) { // ABOUT APP
            startActivity(new Intent(this, AboutAppPage.class));
        }
        if (view == resendEmail_btn) {
            resendEmail();
        }
        if (view == logoutEmail_btn) {
            finish();
            mAuth.signOut();
            startActivity(new Intent(HomePage_loggedin_v2.this, HomePage_loggedin_v2.class));
        }
        if (view == verified_btn) {
            user.reload();

            if (!user.isEmailVerified()) {
                Toast.makeText(HomePage_loggedin_v2.this, "Email is not verified! Please try again! ", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
            }
        }
        if (view == homepagelogout_btn) {
            startActivity(new Intent(HomePage_loggedin_v2.this, HomePage_loggedout.class));
        }

    }

    private void setClickListeners() {
        if (user != null) {

            quiz_btn.setOnClickListener(this);
            quiz_txt.setOnClickListener(this);
            points_btn.setOnClickListener(this);
            points_txt.setOnClickListener(this);
            acct_btn.setOnClickListener(this);
            acct_txt.setOnClickListener(this);
        } else {
            homepagelogout_btn.setOnClickListener(this);
        }

        item_btn.setOnClickListener(this);
        item_txt.setOnClickListener(this);
        about_btn.setOnClickListener(this);
        about_txt.setOnClickListener(this);
    }
}
