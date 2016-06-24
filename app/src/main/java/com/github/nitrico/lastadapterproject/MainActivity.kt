package com.github.nitrico.lastadapterproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.nitrico.lastadapterproject.item.Header
import com.github.nitrico.lastadapterproject.item.Point
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pager.adapter = ViewPagerAdapter(supportFragmentManager)

        Data.items.add(Header("Header 1"))
        Data.items.add(Point(1,1))
        Data.items.add(Header("Header 2"))
        Data.items.add(Point(2,1))
        Data.items.add(Point(2,2))
        Data.items.add(Header("Header 3"))
        Data.items.add(Point(3,1))
        Data.items.add(Point(3,2))
        Data.items.add(Point(3,3))
        Data.items.add(Header("Header 4"))
        Data.items.add(Point(4,1))
        Data.items.add(Point(4,2))
        Data.items.add(Point(4,3))
        Data.items.add(Point(4,4))
        Data.items.add(Header("Header 5"))
        Data.items.add(Point(5,1))
        Data.items.add(Point(5,2))
        Data.items.add(Point(5,3))
        Data.items.add(Point(5,4))
        Data.items.add(Point(5,5))
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getCount() = 2
        override fun getItem(i: Int): ListFragment = if (i == 0) KotlinListFragment() else JavaListFragment()
    }

}
