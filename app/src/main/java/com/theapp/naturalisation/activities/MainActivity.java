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

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.fragments.DocumentsDecreeFragment;
import com.theapp.naturalisation.fragments.PlusFragment;
import com.theapp.naturalisation.fragments.QuestionsFragment;
import com.theapp.naturalisation.helpers.CommonTools;
import com.theapp.naturalisation.helpers.LocaleHelper;

import static com.theapp.naturalisation.helpers.CommonTools.FULL_VERSION;
import static com.theapp.naturalisation.helpers.CommonTools.LITE_VERSION;

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
        setTheme(R.style.AppTheme);
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

        buildSmartRatingDilaog();
    }

    private void buildSmartRatingDilaog() {
        String url = LITE_VERSION;

        if (CommonTools.isFullVersion()) {
            url = FULL_VERSION;
        }

        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .session(7)
                .threshold(3)
                .title(getResources().getString(R.string.rating_title))
                .ratingBarBackgroundColor(R.color.grey_500)
                .positiveButtonText(getResources().getString(R.string.rating_later))
                .negativeButtonText(getResources().getString(R.string.rating_no))
                .positiveButtonTextColor(R.color.white)
                .negativeButtonTextColor(R.color.white)
                .negativeButtonBackgroundColor(R.color.grey_400)
                .positiveButtonBackgroundColor(R.color.grey_400)
                .formTitle(getResources().getString(R.string.rating_feedback))
                .formHint(getResources().getString(R.string.rating_hint))
                .formSubmitText(getResources().getString(R.string.submit))
                .formCancelText(getResources().getString(R.string.cancel))
                .ratingBarColor(R.color.colorPrimaryDark)
                .playstoreUrl(url)
                .build();

        ratingDialog.show();
    }

    private Fragment getOrCreate(int navigationId) {

        // try to get it
        Fragment selected;
        switch (navigationId) {
            case R.id.navigation_questions:
                selected = findFragmentByTagOtherwiseCreate(QuestionsFragment.class);
                break;
            case R.id.navigation_documents:
                selected = findFragmentByTagOtherwiseCreate(DocumentsDecreeFragment.class);
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
                        Uri.parse(CommonTools.TERMS));
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
                    Intent fullVersionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FULL_VERSION));
                    startActivity(fullVersionIntent);
                } else {
                    Intent fullVersionIntent = new Intent(Intent.ACTION_VIEW);
                    fullVersionIntent.setData(Uri.parse(FULL_VERSION));
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
