package com.students.chatapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.students.chatapp.R
import com.students.chatapp.databinding.ActivityMainBinding
import com.students.chatapp.ui.fragments.chats.ChatsFragment
import com.students.chatapp.ui.fragments.contacts.ContactsFragment
import com.students.chatapp.ui.fragments.groups.GroupsFragment

private lateinit var binding: ActivityMainBinding
private lateinit var viewPagerAdapter: MainViewPagerAdapter

class MainActivity : AppCompatActivity() {

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

}