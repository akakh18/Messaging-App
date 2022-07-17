package ge.akakhishvili.messangerapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.transition.Visibility
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import ge.akakhishvili.messangerapp.R

class MainPageActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    private lateinit var homeButtonView: AppCompatImageView
    private lateinit var settingsButtonView: AppCompatImageView
    private lateinit var topBar: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        initViews()
    }

    private fun initViews() {
        viewPager = findViewById(R.id.main_pages_fragments_container)
        homeButtonView = findViewById(R.id.main_page_home_button)
        settingsButtonView = findViewById(R.id.main_page_settings_button)
//        topBar = findViewById(R.id.main_page_top_bar)


        viewPager.adapter = ViewPagerAdapter(this, arrayListOf(MessageListFragment(), ProfilePageFragment(this)))

        homeButtonView.setOnClickListener{
            viewPager.setCurrentItem(0, true)
        }
        
        settingsButtonView.setOnClickListener {
            viewPager.setCurrentItem(1, true)
        }

//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                if (position == 1) {
//                    findViewById<AppBarLayout>(R.id.main_page_top_bar).visibility = View.GONE
//                }
//                if (position == 0) {
//                    findViewById<AppBarLayout>(R.id.main_page_top_bar).visibility = View.VISIBLE
//                }
//            }
//        })

    }
}