package com.students.chatapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.students.chatapp.R
import com.students.chatapp.databinding.ActivityMainBinding
import com.students.chatapp.ui.fragments.chats.ChatsFragment
import com.students.chatapp.ui.fragments.contacts.ContactsFragment
import com.students.chatapp.ui.fragments.groups.GroupsFragment


class MainActivity : AppCompatActivity() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup Actionbar
        if (supportActionBar == null) setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = resources?.getString(R.string.app_name)

        setupViewPagerAndTabs()

    }

    private fun setupViewPagerAndTabs() {

        //Setup the ViewPager
        viewPagerAdapter = MainViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPagerAdapter.addFragment(ChatsFragment())
        viewPagerAdapter.addFragment(GroupsFragment())
        viewPagerAdapter.addFragment(ContactsFragment())

        binding.viewPager.adapter = viewPagerAdapter

        binding.tabs.addTab(binding.tabs.newTab().setText("Chats"))
        binding.tabs.addTab(binding.tabs.newTab().setText("Groups"))
        binding.tabs.addTab(binding.tabs.newTab().setText("Contacts"))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabs.selectTab(binding.tabs.getTabAt(position))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.find_friends_item_menu -> {

                true
            }

            R.id.settings_item_menu -> {


                true
            }

            R.id.logout_item_menu -> {

                firebaseAuth.signOut()

                sendToLoginActivity()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun sendToLoginActivity() {

        Intent(this, LoginActivity::class.java).apply {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }.also { startActivity(it) }

    }

}