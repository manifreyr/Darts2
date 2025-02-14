package `is`.hi.darts2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import `is`.hi.darts2.R
import `is`.hi.darts2.network.Network
import `is`.hi.darts2.ui.start.StartFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Network.init(applicationContext)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StartFragment())
                .commit()
        }
    }

    fun onNavigationItemSelected(item: Int) {
        when (item) {
            1 -> { /* Navigate to screen 1 */
            }

            2 -> { /* Navigate to screen 2 */
            }

            3 -> { /* Navigate to screen 3 */
            }

            4 -> { /* Navigate to screen 4 */
            }
        }
    }
}