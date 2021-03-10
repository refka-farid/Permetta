package com.bravedroid.permetta.permission.drawer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bravedroid.permetta.R
import com.bravedroid.permetta.databinding.ActivityMasterDetailsBinding
import com.google.android.material.navigation.NavigationView

private const val TAG_MASTER_FRAGMENT = "TAG_MASTER_FRAGMENT"
private const val TAG_DETAIL_FRAGMENT = "TAG_DETAIL_FRAGMENT"

class MasterDetailsActivity : AppCompatActivity(), MasterFragment.Callbacks {

    var binding: ActivityMasterDetailsBinding? = null

    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // setup toolbar
        setSupportActionBar(binding?.masterDetailsToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        // setup drawer view
        drawerLayout = binding?.drawerLayout as? DrawerLayout
        val navigationView = binding?.masterFragmentContainer as? NavigationView
        navigationView?.setNavigationItemSelectedListener {
            true
        }

        val actionBar = actionBar;
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_person)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val detailFragment = DetailsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.detail_fragment_container, detailFragment, TAG_DETAIL_FRAGMENT)
            .commit();

        // insert master fragment into master container (i.e. nav view)
        val masterFragment = MasterFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.master_fragment_container, masterFragment, TAG_MASTER_FRAGMENT)
            .commit();
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout?.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMasterItemClicked(masterItemId: Int) {
        val detailFragment: DetailsFragment? =
            supportFragmentManager.findFragmentByTag(TAG_DETAIL_FRAGMENT) as DetailsFragment?
        detailFragment?.onMasterItemClicked(masterItemId)

        // Close the navigation drawer
        if (drawerLayout != null) {
            drawerLayout!!.closeDrawers()
        }
    }
}
