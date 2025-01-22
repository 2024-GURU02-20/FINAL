package com.android.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bookListFragment: BookListFragment = BookListFragment()
        val archiveFragment: ArchiveFragment = ArchiveFragment()
        val recommendFragment: RecommendFragment = RecommendFragment()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.rootlayout, bookListFragment)
        fragmentTransaction.commit()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (item.itemId == R.id.bookList) {
                fragmentTransaction.replace(R.id.rootlayout, bookListFragment).commit()
                true
            } else if (item.itemId == R.id.archive) {
                fragmentTransaction.replace(R.id.rootlayout, archiveFragment).commit()
                true
            } else if (item.itemId == R.id.recommend) {
                fragmentTransaction.replace(R.id.rootlayout, recommendFragment).commit()
                true
            } else {
                false
            }
        }
    }
}