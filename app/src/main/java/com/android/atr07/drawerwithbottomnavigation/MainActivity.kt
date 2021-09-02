package com.android.atr07.drawerwithbottomnavigation

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.atr07.drawerwithbottomnavigation.databinding.ActivityMainBinding
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    lateinit var materialToolbar: MaterialToolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout)
        materialToolbar = findViewById(R.id.main_toolbar)

        navController = findNavController(R.id.main_nav_host) //Initialising navController

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.offlineFragment, R.id.homeFragment,
            R.id.awaitingReprot, R.id.newMedicalRecords)
            .setOpenableLayout(binding.mainDrawerLayout)
            .build()



        setSupportActionBar(binding.mainToolbar) //Set toolbar
        binding.mainBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        setupActionBarWithNavController(navController, appBarConfiguration) //Setup toolbar with back button and drawer icon according to appBarConfiguration
        binding.mainBottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#3F51B5")));

        //visibilityNavElements(navController) //If you want to hide drawer or bottom navigation configure that in this function
       // binding.mainBottomNavigationView.menu.getItem(R.id.main_bottom_navigation_view).setChecked(true)
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var click : Int = 0
        val s = SpannableString("offline Fragment")
        s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
        item.title = s


        when (item.itemId) {
            R.id.homeFragment -> {
               // navController.graph.label ="dd"

                collapsingToolbarLayout.isTitleEnabled = false
                materialToolbar.title = "dd"
                item.setIcon(R.drawable.ic_dashboard)
                val s = SpannableString("Home Fragment")
                s.setSpan(ForegroundColorSpan(Color.GREEN), 0, s.length, 0)
                item.title = s
                navController.navigate(R.id.homeFragment)

            }
            R.id.offlineFragment ->{

                item.setIcon(R.drawable.ic_home)
               // navController.graph.label ="title"
                collapsingToolbarLayout.isTitleEnabled = false
                materialToolbar.title ="Title"
                navController.navigate(R.id.offlineFragment)
            }


        }
        false

    }
            /*R.id.homeFragment-> {

                fragment = HomeFragment()
                //item.setIcon(R.drawable.ic_tab_trading_active)
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }*/
            /*R.id.accountsFragment -> {
                collapsingToolbarLayout.title = "Title"
                fragment = AccountsFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }*/
            /* R.id.navigation_trading -> {
                 message.setText(R.string.title_trading)
                 item.setIcon(R.drawable.ic_tab_trading_active)
                 return@OnNavigationItemSelectedListener true
             }
             R.id.navigation_wallet-> {
                 message.setText(R.string.title_wallet)
                 item.setIcon(R.drawable.ic_tab_wallet_active)
                 return@OnNavigationItemSelectedListener true
             }*/



    private fun visibilityNavElements(navController: NavController) {

        //Listen for the change in fragment (navigation) and hide or show drawer or bottom navigation accordingly if required
        //Modify this according to your need
        //If you want you can implement logic to deselect(styling) the bottom navigation menu item when drawer item selected using listener

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
               // R.id.profileFragment -> hideBothNavigation()
                R.id.settingsFragment -> hideBottomNavigation()
                else -> showBothNavigation()
            }
        }

    }

    private fun hideBothNavigation() { //Hide both drawer and bottom navigation bar
        binding.mainBottomNavigationView.visibility = View.GONE
        binding.mainNavigationView.visibility = View.GONE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED) //To lock navigation drawer so that it don't respond to swipe gesture
    }

    private fun hideBottomNavigation() { //Hide bottom navigation
        binding.mainBottomNavigationView.visibility = View.GONE
        binding.mainNavigationView.visibility = View.VISIBLE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED) //To unlock navigation drawer

        binding.mainNavigationView.setupWithNavController(navController) //Setup Drawer navigation with navController
    }

    private fun showBothNavigation() {
        binding.mainBottomNavigationView.visibility = View.VISIBLE
        binding.mainNavigationView.visibility = View.VISIBLE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setupNavControl() //To configure navController with drawer and bottom navigation
    }

    private fun setupNavControl() {
        binding.mainNavigationView.setupWithNavController(navController) //Setup Drawer navigation with navController
        binding.mainBottomNavigationView.setupWithNavController(navController) //Setup Bottom navigation with navController
    }

    fun exitApp() { //To exit the application call this function from fragment
        this.finishAffinity()
    }

    override fun onSupportNavigateUp(): Boolean { //Setup appBarConfiguration for back arrow
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        when { //If drawer layout is open close that on back pressed
            binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }
            else -> {
                super.onBackPressed() //If drawer is already in closed condition then go back
            }
        }
    }
}
