package com.github.nitrico.lastadapter_sample.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.nitrico.lastadapter_sample.R
import com.github.nitrico.lastadapter_sample.data.Data
import com.github.nitrico.lastadapter_sample.data.Header
import com.google.android.material.tabs.TabLayout
import java.util.*

class MainActivity : AppCompatActivity() {

    private val random = Random()

    private var randomPosition: Int = 0
        get() = random.nextInt(Data.items.size-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val pager = findViewById<ViewPager>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)

        setSupportActionBar(toolbar)
        pager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu) = consume { menuInflater.inflate(R.menu.main, menu) }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.addFirst -> consume {
            Data.items.add(0, Header("New Header"))
        }
        R.id.addLast -> consume {
            Data.items.add(Data.items.size, Header("New header"))
        }
        R.id.addRandom -> consume {
            Data.items.add(randomPosition, Header("New Header"))
        }
        R.id.removeFirst -> consume {
            if (Data.items.isNotEmpty()) Data.items.removeAt(0)
        }
        R.id.removeLast -> consume {
            if (Data.items.isNotEmpty()) Data.items.removeAt(Data.items.size-1)
        }
        R.id.removeRandom -> consume {
            if (Data.items.isNotEmpty()) Data.items.removeAt(randomPosition)
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount() = 2
        override fun getItem(i: Int) = if (i == 0) KotlinListFragment() else JavaListFragment()
        override fun getPageTitle(i: Int) = if (i == 0) "Kotlin" else "Java"
    }

}
