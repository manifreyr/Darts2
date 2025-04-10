package `is`.hi.darts2.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import `is`.hi.darts2.viewmodel.LeaderboardViewModel
import `is`.hi.darts2.R


class LeaderboardFragment : Fragment() {

    private val leaderboardViewModel: LeaderboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout (fragment_leaderboard.xml)
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tableLayout = view.findViewById<TableLayout>(R.id.leaderboardTable)

        // Observe the leaderboard data from the ViewModel
        leaderboardViewModel.leaderboardEntries.observe(viewLifecycleOwner) { leaderboardEntries ->
            // Remove any existing rows before populating new data
            tableLayout.removeAllViews()


            // Create and add a header row (optional)
            val headerRow = TableRow(context)
            headerRow.addView(createTextView("Rank", isHeader = true))
            headerRow.addView(createTextView("Username", isHeader = true))
            headerRow.addView(createTextView("Win %", isHeader = true))
            tableLayout.addView(headerRow)

            val sortedEntries = leaderboardEntries.sortedByDescending { entry ->
                entry["winPercentage"]?.toString()?.toDoubleOrNull() ?: 0.0
            }

            var count = 1
            // Create a new TableRow for each leaderboard entry
            sortedEntries.forEach { entry ->
                val row = TableRow(context)

                // Assuming your map contains keys "rank", "username", and "score"
                val rank = count++
                val username = entry["name"]?.toString() ?: "-"
                val score = entry["winPercentage"]?.toString() ?: "-"

                row.addView(createTextView(rank.toString()))
                row.addView(createTextView(username))
                row.addView(createTextView(score))

                tableLayout.addView(row)
            }
        }

        // Fetch the leaderboard data
        leaderboardViewModel.fetchLeaderboardStats()
    }

    // Helper function to create a TextView with optional header styling
    private fun createTextView(text: String, isHeader: Boolean = false): TextView {
        return TextView(context).apply {
            this.text = text
            val padding = if (isHeader) 16 else 8
            setPadding(padding, padding, padding, padding)
            if (isHeader) {
                setTypeface(null, android.graphics.Typeface.BOLD)
            }
        }
    }
}
