package appdev.sapir.maor.passkeeper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import appdev.sapir.maor.passkeeper.Database.PassItem;
import appdev.sapir.maor.passkeeper.Database.PassItemArrayAdapter;
import appdev.sapir.maor.passkeeper.Database.PasswordsDataSource;

public class PassKeeperMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener, SearchView.OnQueryTextListener {

    public static PasswordsDataSource dataSource = null;

    public static ListView mListView = null;

    private Intent intent;

    public static EditText password = null;

    private List<PassItem> values;

    private PassItemArrayAdapter originalAdapter = null;

    private SearchView mSearchView;

    private SharedPreferences data;

    private NavigationView navMenu;

    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_keeper_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data = getSharedPreferences(getString(R.string.data_uri_name), Context.MODE_PRIVATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navMenu = (NavigationView) findViewById(R.id.nav_view);

        navMenu.setNavigationItemSelectedListener(this);

        updateNavigationMenuTitles(navMenu);

        dataSource = new PasswordsDataSource(this);

        dataSource.open();

        values = dataSource.getAllPassItems();

        originalAdapter = new PassItemArrayAdapter(this, R.layout.pass_item_row_layout, values);

        mListView = (ListView) findViewById(R.id.passListView);

        mListView.setAdapter(originalAdapter);

        mListView.setTextFilterEnabled(true);

        mListView.setOnItemLongClickListener(this);

        mListView.setOnItemClickListener(this);

        setupSearchView();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void updateNavigationMenuTitles(NavigationView navMenu) {
        if ((data.getBoolean(getString(R.string.data_vname_have_entry_pass), false))) {
            navMenu.getMenu().findItem(R.id.action_enable_password_entry).setVisible(false);
            navMenu.getMenu().findItem(R.id.action_disable_password_entry).setVisible(true);
            navMenu.getMenu().findItem(R.id.action_change_password_entry).setVisible(true);
        } else {
            navMenu.getMenu().findItem(R.id.action_enable_password_entry).setVisible(true);
            navMenu.getMenu().findItem(R.id.action_disable_password_entry).setVisible(false);
            navMenu.getMenu().findItem(R.id.action_change_password_entry).setVisible(false);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_enable_password_entry) {
            if (!(data.getBoolean(String.valueOf(R.string.data_vname_have_entry_pass), false))) {
                intent = new Intent(this, AddEntryPasswordActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Already have Login Password", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.action_disable_password_entry) {
            intent = new Intent(this, DisableEntryPasswordActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_change_password_entry) {
            intent = new Intent(this, ChangeOldEntryPasswordActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view) {
        PassItemArrayAdapter adapter = (PassItemArrayAdapter) mListView.getAdapter();
        switch (view.getId()) {
            case R.id.btnAdd:
                intent = new Intent(this, AddNewPassActivity.class);
                this.startActivity(intent);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        originalAdapter.restoreOriginList();
        updateNavigationMenuTitles(navMenu);
        mSearchView.setQuery("", false);
        mSearchView.clearFocus();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        originalAdapter.restoreOriginList();
        originalAdapter.filterSearch(newText);
        originalAdapter.notifyDataSetChanged();

        return true;

    }

    private void setupSearchView() {
        mSearchView = (SearchView) findViewById(R.id.actvSearch);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconified(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, (getString(R.string.msg_toast_show_pass) + ((PassItem) mListView.getAdapter().getItem(position)).getPassValue())+"  ", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        setEditAskDialog();
        this.position = position;
        return false;
    }

    private void moveToEditMode(int position) {
        intent = new Intent(this, ViewPassItemActivity.class);
        intent.putExtra("position", position);
        this.startActivity(intent);
    }

    private void setEditAskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.err_msg_title_edit_pass);
        builder.setMessage(R.string.err_msg_toast_edit_pass);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                moveToEditMode(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
