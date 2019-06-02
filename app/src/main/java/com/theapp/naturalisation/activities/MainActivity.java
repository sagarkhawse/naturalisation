package com.theapp.naturalisation.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.fragments.DocumentsFragment;
import com.theapp.naturalisation.fragments.PlusFragment;
import com.theapp.naturalisation.fragments.QuestionsFragment;
import com.theapp.naturalisation.helpers.CommonTools;
import com.theapp.naturalisation.helpers.LocaleHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Fragment mSelectedFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int itemId = item.getItemId();
        showPage(itemId);
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        if (CommonTools.isLiteVersion()) {
            getSupportActionBar().setTitle(getResources().getString(R.string.action_bar_title_lite));
        } else if (CommonTools.isFullVersion()) {
            getSupportActionBar().setTitle(getResources().getString(R.string.action_bar_title_pro));
        }

        navView.setSelectedItemId(R.id.navigation_questions);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // instantiate the first shown fragment
        if (savedInstanceState == null) {
            Fragment fragmentByTag = findFragmentByTagOtherwiseCreate(QuestionsFragment.class);
            replaceFragment(fragmentByTag, QuestionsFragment.class.getName());
        }
    }

    private Fragment getOrCreate(int navigationId) {

        // try to get it
        Fragment selected;
        switch (navigationId) {
            case R.id.navigation_questions:
                selected = findFragmentByTagOtherwiseCreate(QuestionsFragment.class);
                break;
            case R.id.navigation_documents:
                selected = findFragmentByTagOtherwiseCreate(DocumentsFragment.class);
                break;
            case R.id.navigation_settings:
                selected = findFragmentByTagOtherwiseCreate(PlusFragment.class);
                break;
            default:
                selected = findFragmentByTagOtherwiseCreate(QuestionsFragment.class);
                break;
        }
        return selected;
    }

    private Fragment findFragmentByTagOtherwiseCreate(Class<?> fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass.getName());
        if (fragment == null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                fragmentManager.beginTransaction()
                        .add(R.id.activity_content, fragment, fragment.getClass()
                                .getName())
                        .hide(fragment)
                        .commit();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }

    /**
     * Shows the page associated to the input nav id.
     *
     * @param itemId id of the bottom tab to focus
     */
    public void showPage(int itemId) {
        Fragment foundFragment = getOrCreate(itemId);
        // replace fragment
        replaceFragment(foundFragment, foundFragment.getClass().getName());
    }

    private void replaceFragment(@NonNull Fragment fragment, @NonNull String tag) {
        if (fragment.equals(mSelectedFragment)) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideActiveFragment(fragmentTransaction);
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
        mSelectedFragment = fragment;
    }

    /**
     * This method hides the current fragment. It can apply specific logic in case of particular cases. Ex: Fragment
     * containing ViewPager with fragments inside.
     *
     * @param fragmentTransaction fragment transaction to update
     */
    private void hideActiveFragment(FragmentTransaction fragmentTransaction) {
        if (mSelectedFragment == null) {
            return;
        }
        // hide current
        fragmentTransaction.hide(mSelectedFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (CommonTools.isFullVersion()) {
            MenuItem item = menu.findItem(R.id.menu_get_full);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_terms:
                // User chose the "Settings" item, show the app settings UI...
                Intent termsIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://docs.google.com/document/d/e/2PACX-1vTHUThxL3g4AsJO2aFALoYaAoBbvBRVzqSkQi9MT1_78pr_5jBtOzGmXVLSh0mXCjf0B8tkglApy1aJ/pub"));
                startActivity(termsIntent);
                break;
            case R.id.menu_language:
                if (LocaleHelper.getLanguage(MainActivity.this).equals(LocaleHelper.FRENCH_LABEL)) {
                    LocaleHelper.setLocale(MainActivity.this, LocaleHelper.ENGLISH_LABLE);
                } else {
                    LocaleHelper.setLocale(MainActivity.this, LocaleHelper.FRENCH_LABEL);
                }
                finish();
                startActivity(getIntent());
                break;
            case R.id.menu_get_full:
                if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) { // Checks that Google Play is available
                    Intent fullVersionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.theapp.naturalisation.full"));
                    startActivity(fullVersionIntent);
                } else {
                    Intent fullVersionIntent = new Intent(Intent.ACTION_VIEW);
                    fullVersionIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.theapp.naturalisation.full"));
                    fullVersionIntent.setPackage("com.android.vending");
                    startActivity(fullVersionIntent);
                }
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "fr"));
    }
}
