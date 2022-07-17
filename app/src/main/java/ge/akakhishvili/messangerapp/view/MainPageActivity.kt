package ge.akakhishvili.messangerapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import ge.akakhishvili.messangerapp.R

class MainPageActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    private lateinit var homeButtonView: AppCompatImageView
    private lateinit var settingsButtonView: AppCompatImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        initViews()
    }

    private fun initViews() {
        viewPager = findViewById(R.id.main_pages_fragments_container)
        homeButtonView = findViewById(R.id.main_page_home_button)
        settingsButtonView = findViewById(R.id.main_page_settings_button)


        viewPager.adapter =
            ViewPagerAdapter(this, arrayListOf(MessageListFragment(), ProfilePageFragment(this)))

        homeButtonView.setOnClickListener {
            viewPager.setCurrentItem(0, true)
        }

        settingsButtonView.setOnClickListener {
            viewPager.setCurrentItem(1, true)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) {
                    colorIcons(R.color.black, R.color.selected_fragment_icon_color)
                }
                if (position == 0) {
                    colorIcons(R.color.selected_fragment_icon_color, R.color.black)
                }
            }
        })

    }

    private fun colorButton(button: AppCompatImageView, color: Int) {
        button.setColorFilter(ContextCompat.getColor(baseContext, color))
    }

    private fun colorIcons(homeButtonColor: Int, settingsButtonColor: Int) {
        colorButton(homeButtonView, homeButtonColor)
        colorButton(settingsButtonView, settingsButtonColor)
    }
}