package cu.video.app.streamingcuba.ui.fragments.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import cu.video.app.streamingcuba.databinding.FragmentStartBinding
import cu.video.app.streamingcuba.ui.base.BaseFragment
import cu.video.app.streamingcuba.ui.fragments.live.LiveFragment
import cu.video.app.streamingcuba.ui.fragments.upcoming.UpcomingFragment
import kotlin.math.abs

class StartFragment : BaseFragment(), View.OnClickListener, AppBarLayout.OnOffsetChangedListener,
    RadioGroup.OnCheckedChangeListener {

    private lateinit var viewModel: StartViewModel
    private lateinit var fragmentTransaction: FragmentTransaction

    //views
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var mainToolbarLayout: MaterialToolbar
    private lateinit var radioGroup: RadioGroup
    private lateinit var tabLive: RadioButton
    private lateinit var tabEvents: RadioButton
    private lateinit var topImage: ImageView
    private lateinit var scroll: NestedScrollView
    private lateinit var container: FrameLayout

    private var mMaxScrollSize = 0
    private var mIsBarHidden = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configView(binding)
    }

    override fun configView(vb: ViewBinding) {
        super.configView(vb)

        (vb as FragmentStartBinding).apply {
            appBarLayout = appBar
            mainToolbarLayout = mainToolbar
            radioGroup = radio
            tabLive = checkboxTabLive
            tabEvents = checkboxTabEvents
            topImage = topImageView
            scroll = content.scrollVP
            container = content.fragmentContainer
        }
        showLiveFragment()

        radioGroup.setOnCheckedChangeListener(this)

        appBarLayout.addOnOffsetChangedListener(this)

        // to change orientation
        // requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onClick(v: View) {
        val root: FragmentStartBinding = (binding as FragmentStartBinding)
        when (v) {

        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, i: Int) {
        appBarLayout?.let {
            if (mMaxScrollSize == 0) mMaxScrollSize = it.totalScrollRange

            val currentScrollPercentage: Int = (abs(i) * 100 / mMaxScrollSize)

            if (currentScrollPercentage >= PERCENTAGE_TO_SHOW) {
                if (!mIsBarHidden) {
                    mIsBarHidden = true
                    ViewCompat.animate(topImage).scaleY(0f).scaleX(0f).start()
                }
            }

            if (currentScrollPercentage < PERCENTAGE_TO_SHOW) {
                if (mIsBarHidden) {
                    mIsBarHidden = false
                    ViewCompat.animate(topImage).scaleY(1f).scaleX(1f).start()
                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            tabLive.id -> showLiveFragment()
            tabEvents.id -> showUpcomingFragment()
            else -> showLiveFragment()
        }
        scroll.scrollY = 0
    }

    private fun showLiveFragment() {
        hideUpcomingFragment()
        fragmentTransaction = childFragmentManager.beginTransaction()
        var liveFragment: LiveFragment? =
            childFragmentManager.findFragmentByTag(TAG_LIVE) as LiveFragment?

        if (liveFragment == null) {
            liveFragment = LiveFragment()
            fragmentTransaction.add(container.id, liveFragment, TAG_LIVE).commit()
        } else {
            fragmentTransaction.show(liveFragment).commit()
        }
    }

    private fun hideLiveFragment() {
        fragmentTransaction = childFragmentManager.beginTransaction()
        val liveFragment: LiveFragment? =
            childFragmentManager.findFragmentByTag(TAG_LIVE) as LiveFragment?
        if (liveFragment != null) fragmentTransaction.hide(liveFragment).commit()
    }

    private fun showUpcomingFragment() {
        hideLiveFragment()
        fragmentTransaction = childFragmentManager.beginTransaction()
        var upcomingFragment: UpcomingFragment? =
            childFragmentManager.findFragmentByTag(TAG_EVENTS) as UpcomingFragment?

        if (upcomingFragment == null) {
            upcomingFragment = UpcomingFragment()
            fragmentTransaction.add(container.id, upcomingFragment, TAG_EVENTS).commit()
        } else {
            fragmentTransaction.show(upcomingFragment).commit()
        }
    }

    private fun hideUpcomingFragment() {
        fragmentTransaction = childFragmentManager.beginTransaction()
        val upcomingFragment: UpcomingFragment? =
            childFragmentManager.findFragmentByTag(TAG_EVENTS) as UpcomingFragment?
        if (upcomingFragment != null) fragmentTransaction.hide(upcomingFragment).commit()
    }

    companion object {
        private const val PERCENTAGE_TO_SHOW = 65
        const val TAG_LIVE = "live"
        const val TAG_EVENTS = "events"
    }

}