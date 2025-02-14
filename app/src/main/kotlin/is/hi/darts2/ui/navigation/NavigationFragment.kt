package `is`.hi.darts2.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import `is`.hi.darts2.R

class NavigationFragment : Fragment() {


    companion object {
        fun newInstance(): NavigationFragment {
            return NavigationFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }

    interface NavigationListener {
        /**
         * Called when a navigation item is selected.
         * @param item The selected item index (1 through 4)
         */
        fun onNavigationItemSelected(item: Int)
    }

    var listener: NavigationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navItem1 = view.findViewById<ImageView>(R.id.nav_item1)
        val navItem2 = view.findViewById<ImageView>(R.id.nav_item2)
        val navItem3 = view.findViewById<ImageView>(R.id.nav_item3)
        val navItem4 = view.findViewById<ImageView>(R.id.nav_item4)

        navItem1.setOnClickListener { listener?.onNavigationItemSelected(1) }
        navItem2.setOnClickListener { listener?.onNavigationItemSelected(2) }
        navItem3.setOnClickListener { listener?.onNavigationItemSelected(3) }
        navItem4.setOnClickListener { listener?.onNavigationItemSelected(4) }
    }
}
