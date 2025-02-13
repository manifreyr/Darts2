package `is`.hi.darts2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import `is`.hi.darts2.R
import `is`.hi.darts2.ui.start.StartFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load the initial fragment (e.g., StartFragment) if this is the first creation
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StartFragment())
                .commit()
        }
    }
}